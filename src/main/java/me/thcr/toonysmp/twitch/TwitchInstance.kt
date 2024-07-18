package me.thcr.toonysmp.twitch

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser.parseString
import me.thcr.toonysmp.Main
import me.thcr.toonysmp.config.ConfigOption
import me.thcr.toonysmp.config.ConfigWrapper
import me.thcr.toonysmp.files.FileManager
import me.thcr.toonysmp.twitch.tokens.TokenContainer
import me.thcr.toonysmp.twitch.tokens.TokenStorage
import okhttp3.*
import org.bukkit.Bukkit
import java.util.*

class TwitchInstance(private val PLUGIN_NAME: String, private val plugin: Main, private val config_wrapper: ConfigWrapper, private val file_manager: FileManager) {
    private val token_storage = TokenStorage(PLUGIN_NAME)
    private val client = OkHttpClient.Builder().build()
    private val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    private var ws: WebSocket? = null
    private var is_websocket_open = false
    private val twitch_info = load_app_data()

    init {
        val enabled: Boolean = config_wrapper.get(ConfigOption.ENABLE_TWITCH_AUTOMATION)
        if (enabled) start()
        config_wrapper.subscribe(ConfigOption.ENABLE_TWITCH_AUTOMATION, "twitch_startup") {
            when (config_wrapper.get(ConfigOption.ENABLE_TWITCH_AUTOMATION) as Boolean) {
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
        val token_container = token_storage.get(id)
        val access_token = token_container.access_token
        val data = DataObject(topics, access_token)
        val listen_object = ListenObject("LISTEN", data)
        send_listen_object(listen_object)
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

        val response_object = parseString(response.body?.string()).asJsonObject
        val access_token = response_object.get("access_token").asString

        token_storage.set(userid, TokenContainer(access_token, refreshToken))
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
            val json_object: JsonObject = parseString(text).asJsonObject
            when (json_object.get("type").asString) {
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
                    handle_message(json_object)
                }
                "RESPONSE" -> {
                    val error = json_object.get("error").asString
                    if (error.isNotEmpty()) println(error)
                }
                "AUTH_REVOKED" -> {
                    json_object.get("data").asJsonObject.get("topics").asJsonArray.forEach {
                        val user_id = it.asString.split(".")[0]
                        refresh_token(user_id)
                        subscribe_to_topic(user_id)
                    }
                }
                else -> println("Unknown message type: ${json_object.get("type").asString}")
            }
        }

        private fun handle_message(jsonObject: JsonObject) {
            val data_string = jsonObject.get("data").asJsonObject
                .get("message").asString

            val data_object = parseString(data_string).asJsonObject
            val redemption_object = data_object
                .get("data").asJsonObject
                .get("redemption").asJsonObject

            val reward_json_object = redemption_object.get("reward").asJsonObject

            if (reward_json_object.get("title").asString == "Whitelist to ViewerSMP") {
                add_to_whitelist(redemption_object.get("user_input").asString)
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
