package net.marwanaziz.gymsharktask.data

import kotlinx.coroutines.test.runTest
import net.marwanaziz.gymsharktask.data.remote.CatalogueResponse
import net.marwanaziz.gymsharktask.data.remote.CatalogueResult
import net.marwanaziz.gymsharktask.domain.CatalogueUnavailable
import net.marwanaziz.gymsharktask.domain.DomainResult
import net.marwanaziz.gymsharktask.domain.ProductNotFound
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
            assertEquals(DomainResult.Failure(CatalogueUnavailable), repository.getProduct(1L))
        }
    }

    @Test
    fun returnsProductNotFoundWithTheRequestedId() = runTest {
        val repository = repository(
            CatalogueResult.Success(CatalogueResponse(hits = listOf(catalogueHit(id = 1L)))),
        )

        val result = repository.getProduct(99L)

        assertEquals(DomainResult.Failure(ProductNotFound(99L)), result)
    }

    @Test
    fun returnsTheProductWhenTheIdIsPresent() = runTest {
        val hit = catalogueHit(id = 1L)
        val repository = repository(
            CatalogueResult.Success(CatalogueResponse(hits = listOf(catalogueHit(id = 2L), hit))),
        )

        val result = repository.getProduct(1L)

        assertEquals(DomainResult.Success(mapper.map(hit)), result)
    }

    private fun repository(result: CatalogueResult): RemoteProductRepository {
        return RemoteProductRepository(
            catalogueRemote = FakeCatalogueRemote(result),
            productMapper = mapper,
        )
    }
}
