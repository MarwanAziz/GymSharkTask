package net.marwanaziz.gymsharktask.data.remote

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogueClientTest {
    @Test
    fun returnsDecodedCatalogueFromLocalServer() = runTest {
        val body = fixture("catalogue.json")

        val result = fetch(MockResponse().setResponseCode(200).setBody(body))

        assertTrue(result is CatalogueResult.Success)
        val goingFast = (result as CatalogueResult.Success).catalogue.hits
            .single { it.id == 6693927616611L }
        assertEquals("Vital Seamless 2.0 Leggings", goingFast.title)
        assertEquals(listOf("going-fast"), goingFast.labels)
        assertEquals(1000, goingFast.price)
        assertEquals(listOf("f"), goingFast.gender)
    }

    @Test
    fun returnsHttpFailureWhenResponseIsNotSuccessful() = runTest {
        val result = fetch(MockResponse().setResponseCode(500).setBody("unavailable"))

        assertEquals(CatalogueResult.HttpFailure(500), result)
    }

    @Test
    fun returnsUnreadableBodyWhenJsonIsNotCatalogue() = runTest {
        val result = fetch(MockResponse().setResponseCode(200).setBody("""{"unexpected":true}"""))

        assertEquals(CatalogueResult.UnreadableBody, result)
    }

    @Test
    fun returnsTransportFailureWhenConnectionFails() = runTest {
        val server = MockWebServer()
        server.start()
        val catalogueUrl = server.url("/catalogue.json").toString()
        server.close()

        val result = CatalogueClient(catalogueUrl).fetchCatalogue()

        assertEquals(CatalogueResult.TransportFailure, result)
    }

    private suspend fun fetch(response: MockResponse): CatalogueResult {
        val server = MockWebServer()
        server.start()
        try {
            server.enqueue(response)
            val client = CatalogueClient(server.url("/catalogue.json").toString())
            return client.fetchCatalogue()
        } finally {
            server.close()
        }
    }
}

private fun fixture(name: String): String {
    val stream = checkNotNull(CatalogueClientTest::class.java.classLoader.getResourceAsStream(name)) {
        "Missing test resource $name"
    }
    return stream.bufferedReader().use { it.readText() }
}
