package net.marwanaziz.gymsharktask.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit

class CatalogueClient(
    private val catalogueUrl: String,
) : CatalogueRemote {
    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()

    override suspend fun fetchCatalogue(): CatalogueResult = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url(catalogueUrl).build()
            httpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext CatalogueResult.HttpFailure(response.code)
                }
                val body = response.body.string()
                decode(body)
            }
        } catch (_: IOException) {
            CatalogueResult.TransportFailure
        }
    }

    private fun decode(body: String): CatalogueResult {
        return try {
            CatalogueResult.Success(
                catalogueJson.decodeFromString(CatalogueResponse.serializer(), body),
            )
        } catch (_: SerializationException) {
            CatalogueResult.UnreadableBody
        }
    }

    private companion object {
        const val TIMEOUT_SECONDS = 10L
    }
}
