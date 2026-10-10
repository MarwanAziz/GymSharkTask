package net.marwanaziz.gymsharktask.data

import kotlinx.coroutines.test.runTest
import net.marwanaziz.gymsharktask.data.remote.CatalogueResponse
import net.marwanaziz.gymsharktask.data.remote.CatalogueResult
import net.marwanaziz.gymsharktask.domain.CatalogueUnavailable
import net.marwanaziz.gymsharktask.domain.DomainResult
import org.junit.Assert.assertEquals
import org.junit.Test

class RemoteProductRepositoryTest {
    private val mapper = ProductMapper()

    @Test
    fun returnsCatalogueUnavailableWhenTheRemoteFails() = runTest {
        val failures = listOf(
            CatalogueResult.TransportFailure,
            CatalogueResult.HttpFailure(500),
            CatalogueResult.UnreadableBody,
        )

        failures.forEach { failure ->
            val repository = repository(failure)

            assertEquals(DomainResult.Failure(CatalogueUnavailable), repository.getProducts())
        }
    }

    @Test
    fun returnsMappedProductsWhenTheCatalogueSucceeds() = runTest {
        val hits = listOf(catalogueHit(id = 1L), catalogueHit(id = 2L))
        val repository = repository(
            CatalogueResult.Success(CatalogueResponse(hits = hits)),
        )

        val result = repository.getProducts()

        assertEquals(DomainResult.Success(hits.map(mapper::map)), result)
    }

    private fun repository(result: CatalogueResult): RemoteProductRepository {
        return RemoteProductRepository(
            catalogueRemote = FakeCatalogueRemote(result),
            productMapper = mapper,
        )
    }
}
