package net.marwanaziz.gymsharktask.domain

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetProductsTest {
    @Test
    fun returnsProductsFromTheRepository() = runTest {
        val products = listOf(
            product(id = 1L, colour = "Black"),
            product(id = 2L, title = "Crest Hoodie", colour = "Navy"),
        )
        val repository = FakeProductRepository(products = products)
        val useCase = ProductUseCase(repository)

        val result = useCase.getProducts()

        assertEquals(DomainResult.Success(products), result)
    }

    @Test
    fun returnsCatalogueUnavailableWhenTheCatalogueCannotBeLoaded() = runTest {
        val repository = FakeProductRepository(catalogueUnavailable = true)
        val useCase = ProductUseCase(repository)

        val result = useCase.getProducts()

        assertEquals(DomainResult.Failure(CatalogueUnavailable), result)
    }
}
