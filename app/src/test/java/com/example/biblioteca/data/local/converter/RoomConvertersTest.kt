package com.example.biblioteca.data.local.converter

import com.example.biblioteca.domain.model.ReadingStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RoomConvertersTest {

    private val converters = RoomConverters()

    // Conversão de List<String> para String

    @Test
    fun `deve converter lista de strings para string`() {
        val input = listOf("Programação", "Arquitetura", "Kotlin")

        val result = converters.fromStringList(input)

        assertEquals("Programação|||Arquitetura|||Kotlin", result)
    }

    @Test
    fun `deve converter lista vazia para string vazia`() {
        val result = converters.fromStringList(emptyList())

        assertEquals("", result)
    }

    @Test
    fun `deve preservar null ao converter lista nula`() {
        val result = converters.fromStringList(null)

        assertNull(result)
    }

    // Conversão de String para List<String>

    @Test
    fun `deve converter string para lista de strings`() {
        val input = "Programação|||Arquitetura|||Kotlin"

        val result = converters.toStringList(input)

        assertEquals(
            listOf("Programação", "Arquitetura", "Kotlin"),
            result
        )
    }

    @Test
    fun `deve converter string vazia para lista vazia`() {
        val result = converters.toStringList("")

        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun `deve converter string com espaços para lista vazia`() {
        val result = converters.toStringList("   ")

        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun `deve converter uma string sem separador para lista com um elemento`() {
        val result = converters.toStringList("Kotlin")

        assertEquals(listOf("Kotlin"), result)
    }

    // Conversão de ReadingStatus para String

    @Test
    fun `deve converter status WANT_TO_READ para string`() {
        val result = converters.fromReadingStatus(ReadingStatus.WANT_TO_READ)

        assertEquals("WANT_TO_READ", result)
    }

    @Test
    fun `deve converter status READING para string`() {
        val result = converters.fromReadingStatus(ReadingStatus.READING)

        assertEquals("READING", result)
    }

    @Test
    fun `deve converter status COMPLETED para string`() {
        val result = converters.fromReadingStatus(ReadingStatus.COMPLETED)

        assertEquals("COMPLETED", result)
    }

    // Conversão de String para ReadingStatus

    @Test
    fun `deve converter string WANT_TO_READ para status`() {
        val result = converters.toReadingStatus("WANT_TO_READ")

        assertEquals(ReadingStatus.WANT_TO_READ, result)
    }

    @Test
    fun `deve converter string READING para status`() {
        val result = converters.toReadingStatus("READING")

        assertEquals(ReadingStatus.READING, result)
    }

    @Test
    fun `deve converter string COMPLETED para status`() {
        val result = converters.toReadingStatus("COMPLETED")

        assertEquals(ReadingStatus.COMPLETED, result)
    }

    @Test
    fun `deve retornar WANT_TO_READ para status desconhecido`() {
        val result = converters.toReadingStatus("STATUS_INVALIDO")

        assertEquals(ReadingStatus.WANT_TO_READ, result)
    }

    @Test
    fun `deve retornar WANT_TO_READ para string vazia`() {
        val result = converters.toReadingStatus("")

        assertEquals(ReadingStatus.WANT_TO_READ, result)
    }

    @Test
    fun `deve preservar status ao converter e reconverter`() {
        ReadingStatus.values().forEach { status ->
            val serialized = converters.fromReadingStatus(status)
            val restored = converters.toReadingStatus(serialized)

            assertEquals(status, restored)
        }
    }
}
