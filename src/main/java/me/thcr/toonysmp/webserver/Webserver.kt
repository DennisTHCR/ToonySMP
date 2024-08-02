@file:Suppress("Since15")

package me.thcr.toonysmp.webserver

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.thcr.toonysmp.Main
import me.thcr.toonysmp.config.GeneralConfig
import me.thcr.toonysmp.config.ConfigWrapper
import me.thcr.toonysmp.files.FileManager
import me.thcr.toonysmp.twitch.TwitchInstance

import org.slf4j.LoggerFactory
import java.io.File
import java.security.KeyStore
import kotlin.text.toCharArray

@Suppress("LocalVariableName")
class Webserver(PLUGIN_NAME: String, plugin: Main, config_wrapper: ConfigWrapper<GeneralConfig>, file_manager: FileManager) {
    private val twitchInstance = TwitchInstance(PLUGIN_NAME, plugin, config_wrapper, file_manager)
    private val keyStoreFile = File("plugins/$PLUGIN_NAME/keystore.jks")
    private val keyStore: KeyStore = KeyStore.getInstance(keyStoreFile, "123456".toCharArray())

    private val environment = applicationEngineEnvironment {
        log = LoggerFactory.getLogger("ktor.application")
        connector {
            port = 8080
        }
        sslConnector(
            keyStore = keyStore,
            keyAlias = "toonystudios",
            keyStorePassword = { "123456".toCharArray() },
            privateKeyPassword = { "123456".toCharArray() }) {
            port = 8443
            keyStorePath = keyStoreFile
        }
        module {
            routing {
                get("/auth") {
                    call.respondText("<h1>Successfully authorized!</h1>", ContentType.Text.Html)
                    val id = twitchInstance.get_token(call.parameters["code"]!!)
                    twitchInstance.subscribe_to_topic(id)
                }
                staticFiles("/static", File("plugins/ToonySMP"))
            }
        }
    }
    private var server: NettyApplicationEngine? = null


    init {
        val enabled: Boolean = config_wrapper.get(GeneralConfig.ENABLE_TWITCH_AUTOMATION)
        if (enabled) start()
    }
    private fun start() {
        println("Starting web server...")
        server = embeddedServer(Netty, environment)
        server?.start(false)
        println("Web server started.")
    }

    fun stop() {
        println("Stopping web server...")
        server?.stop(1000, 5000)
        server = null
        println("Web server stopped.")
    }
}