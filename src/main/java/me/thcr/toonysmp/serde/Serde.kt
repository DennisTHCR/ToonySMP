package me.thcr.toonysmp.serde

/**
 * Class to handle everything Serialization and Deserialization related.
 */
class Serde {
    val serializer: Serializer = Serializer()
    val deserializer: Deserializer = Deserializer()
}