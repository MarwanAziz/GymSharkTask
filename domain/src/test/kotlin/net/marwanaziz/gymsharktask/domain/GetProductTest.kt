package net.marwanaziz.gymsharktask.domain

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetProductTest {
    @Test
    fun returnsTheMatchingProduct() = runTest {
        val id = 1L
        val expected = product(id = id)
        val repository = FakeProductRepository(products = listOf(expected, product(id = 2L)))
        val useCase = ProductUseCase(repository)

        val result = useCase.getProduct(id)

        assertEquals(DomainResult.Success(expected), result)
        assertEquals(id, repository.requestedId)
    }

    @Test
    fun returnsProductNotFoundWhenTheIdIsAbsent() = runTest {
        val id = 99L
        val repository = FakeProductRepository(products = listOf(product(id = 1L)))
        val useCase = ProductUseCase(repository)

        val result = useCase.getProduct(id)

        assertEquals(DomainResult.Failure(ProductNotFound(id)), result)
        assertEquals(id, repository.requestedId)
    }

    @Test
    fun returnsCatalogueUnavailableWhenTheCatalogueCannotBeLoaded() = runTest {
        val id = 1L
        val repository = FakeProductRepository(
            products = listOf(product(id = id)),
            catalogueUnavailable = true,
        )
        val useCase = ProductUseCase(repository)

        val result = useCase.getProduct(id)

        assertEquals(DomainResult.Failure(CatalogueUnavailable), result)
        assertEquals(id, repository.requestedId)
    }
}
