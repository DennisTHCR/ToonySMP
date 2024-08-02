package me.thcr.toonysmp.twitch

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser.parseString
import me.thcr.toonysmp.Main
import me.thcr.toonysmp.config.GeneralConfig
import me.thcr.toonysmp.config.ConfigWrapper
import me.thcr.toonysmp.files.FileManager
import me.thcr.toonysmp.twitch.tokens.TokenContainer
import me.thcr.toonysmp.twitch.tokens.TokenStorage
import okhttp3.*
import org.bukkit.Bukkit
import java.util.*

class TwitchInstance(private val PLUGIN_NAME: String, private val plugin: Main, private val config_wrapper: ConfigWrapper<GeneralConfig>, private val file_manager: FileManager) {
    private val token_storage = TokenStorage(PLUGIN_NAME)
    private val client = OkHttpClient.Builder().build()
    private val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    private var ws: WebSocket? = null
    private var is_websocket_open = false
    private val twitch_info = load_app_data()

    init {
        val enabled: Boolean = config_wrapper.get(GeneralConfig.ENABLE_TWITCH_AUTOMATION)
        if (enabled) start()
        config_wrapper.subscribe(GeneralConfig.ENABLE_TWITCH_AUTOMATION, "twitch_startup") {
            when (config_wrapper.get(GeneralConfig.ENABLE_TWITCH_AUTOMATION) as Boolean) {
                true -> start()
                false -> stop()
            }
        }
    }

    private fun load_app_data(): AppInformation {
        return gson.fromJson(file_manager.read_file("plugins/$PLUGIN_NAME/twitch_info.json"), AppInformation::class.java)
    }

    private fun start() {
        token_storage.load()
        connect_websocket()
    }

    private fun stop() {
        ws?.close(1000, "Ok")
    }

    private fun connect_websocket() {
        val request = Request.Builder().url("wss://pubsub-edge.twitch.tv").build()
        ws = client.newWebSocket(request, SocketListener())
    }

    fun get_token(code: String): String {
        val response = client.newCall(Request.Builder()
            .url("https://id.twitch.tv/oauth2/token")
            .post(FormBody.Builder()
                .add("client_id", twitch_info.client_id)
                .add("client_secret", twitch_info.client_secret)
                .add("code", code)
                .add("grant_type", "authorization_code")
                .add("redirect_uri", twitch_info.redirect_uri)
                .build()
            ).build()).execute()

        val responseObject = parseString(response.body?.string()).asJsonObject
        val accessToken = responseObject.get("access_token").asString
        val refreshToken = responseObject.get("refresh_token").asString

        val response2 = client.newCall(Request.Builder()
            .url("https://api.twitch.tv/helix/users")
            .headers(Headers.Builder()
                .add("Authorization", "Bearer $accessToken")
                .add("Client-Id", twitch_info.client_id)
                .build())
            .build()).execute()

        val responseString = response2.body?.string()
        val userDataResponse = gson.fromJson(responseString, UserDataResponse::class.java)
        val id = userDataResponse.data[0].id

        token_storage.add(id, TokenContainer(accessToken, refreshToken))
        return id
    }

    fun subscribe_to_topic(id: String) {
        val topics = ArrayList<String>()
        topics.add("channel-points-channel-v1.$id")
        val tokenContainer = token_storage.get(id)
        val accessToken = tokenContainer.access_token
        val data = DataObject(topics, accessToken)
        val listenObject = ListenObject("LISTEN", data)
        send_listen_object(listenObject)
    }

    private fun refresh_token(userid: String) {
        val refreshToken = token_storage.get(userid).refresh_token

        val response = client.newCall(Request.Builder()
            .url("https://id.twitch.tv/oauth2/token")
            .post(FormBody.Builder()
                .add("client_id", twitch_info.client_id)
                .add("client_secret", twitch_info.client_secret)
                .add("refresh_token", refreshToken)
                .add("grant_type", "refresh_token")
                .build()
            ).build()).execute()

        val responseObject = parseString(response.body?.string()).asJsonObject
        val accessToken = responseObject.get("access_token").asString

        token_storage.set(userid, TokenContainer(accessToken, refreshToken))
    }

    private fun send_listen_object(listenObject: ListenObject) {
        if (is_websocket_open) {
            ws?.send(gson.toJson(listenObject))
        }
    }

    inner class SocketListener : WebSocketListener() {
        private val ping_object: JsonObject = JsonObject().apply { addProperty("type", "PING") }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            is_websocket_open = true
            webSocket.send(ping_object.toString())
            subscribe_to_topics()
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val jsonObject: JsonObject = parseString(text).asJsonObject
            when (jsonObject.get("type").asString) {
                "RECONNECT" -> {
                    is_websocket_open = false
                    webSocket.close(1001, "Ok")
                    connect_websocket()
                    subscribe_to_topics()
                }
                "PONG" -> {
                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            webSocket.send(ping_object.toString())
                        }
                    }, 4 * 60 * 1000) // 4 minutes
                }
                "MESSAGE" -> {
                    handle_message(jsonObject)
                }
                "RESPONSE" -> {
                    val error = jsonObject.get("error").asString
                    if (error.isNotEmpty()) println(error)
                }
                "AUTH_REVOKED" -> {
                    jsonObject.get("data").asJsonObject.get("topics").asJsonArray.forEach {
                        val userId = it.asString.split(".")[0]
                        refresh_token(userId)
                        subscribe_to_topic(userId)
                    }
                }
                else -> println("Unknown message type: ${jsonObject.get("type").asString}")
            }
        }

        private fun handle_message(jsonObject: JsonObject) {
            val dataString = jsonObject.get("data").asJsonObject
                .get("message").asString

            val dataObject = parseString(dataString).asJsonObject
            val redemptionObject = dataObject
                .get("data").asJsonObject
                .get("redemption").asJsonObject

            val rewardJsonObject = redemptionObject.get("reward").asJsonObject

            if (rewardJsonObject.get("title").asString == "Whitelist to ViewerSMP") {
                add_to_whitelist(redemptionObject.get("user_input").asString)
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            is_websocket_open = false
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            is_websocket_open = false
        }

        private fun add_to_whitelist(nickname: String) {
            Bukkit.getScheduler().callSyncMethod(plugin) {
                Bukkit.dispatchCommand(Bukkit.getServer().consoleSender, "whitelist add $nickname")
            }.get()
            Bukkit.reloadWhitelist()
        }

        private fun subscribe_to_topics() {
            token_storage.ids().forEach {
                refresh_token(it)
                subscribe_to_topic(it)
            }
        }
    }
}
