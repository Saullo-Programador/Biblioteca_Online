package com.example.biblioteca.data.remote.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

@OptIn(ExperimentalSerializationApi::class)
object DescriptionSerializer : KSerializer<String?> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            serialName = "Description",
            kind = PrimitiveKind.STRING
        )

    override fun deserialize(decoder: Decoder): String? {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException(
                "DescriptionSerializer só suporta JSON"
            )

        return when (
            val element = jsonDecoder.decodeJsonElement()
        ) {
            is JsonPrimitive -> element.contentOrNull

            is JsonObject -> {
                element["value"]
                    ?.jsonPrimitive
                    ?.contentOrNull
            }

            else -> null
        }
    }

    override fun serialize(
        encoder: Encoder,
        value: String?
    ) {
        if (value == null) {
            encoder.encodeNull()
        } else {
            encoder.encodeString(value)
        }
    }
}