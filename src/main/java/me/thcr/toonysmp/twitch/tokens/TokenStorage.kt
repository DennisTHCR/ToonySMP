package me.thcr.toonysmp.twitch.tokens

import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import me.thcr.toonysmp.files.FileManager

@Suppress("PrivatePropertyName")
class TokenStorage(private val PLUGIN_NAME: String) {
    @Expose
    private var tokens: MutableMap<String, TokenContainer> = HashMap()
    private val fileManager = FileManager()
    private val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

    fun add(id: String, token: TokenContainer) {
        tokens[id] = token
        save()
    }

    fun ids(): List<String> {
        return tokens.keys.toList()
    }

    fun get(id: String): TokenContainer {
        load()
        return tokens[id]!!
    }

    fun set(id: String, token: TokenContainer) {
        tokens[id] = token
        save()
    }

    fun load() {
        this.tokens = gson.fromJson(fileManager.read_file("plugins/$PLUGIN_NAME/tokens.txt"), this::class.java).tokens
    }

    private fun save() {
        fileManager.write_file("plugins/$PLUGIN_NAME/tokens.txt", gson.toJson(this))
    }
}

class TokenContainer(@Expose var access_token: String, @Expose var refresh_token: String)