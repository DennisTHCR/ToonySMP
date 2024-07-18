package me.thcr.toonysmp.files

import java.io.File

class FileManager {
    fun read_file(file_name: String): String {
        val file = File(file_name)
        if (file.exists() && file.isFile) {
            return file.readText(Charsets.UTF_8)
        }
        file.getParentFile().mkdirs()
        file.createNewFile()
        return ""
    }

    fun write_file(file_name: String, content: String) {
        val file = File(file_name)
        if (!(file.exists() && file.isFile)) {
            file.getParentFile().mkdirs()
            file.createNewFile()
        }
        file.writeText(content, Charsets.UTF_8)
    }
}