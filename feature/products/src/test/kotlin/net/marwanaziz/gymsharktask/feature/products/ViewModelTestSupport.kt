package net.marwanaziz.gymsharktask.feature.products

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import net.marwanaziz.gymsharktask.domain.CatalogueUnavailable
import net.marwanaziz.gymsharktask.domain.DomainResult
import net.marwanaziz.gymsharktask.domain.Product
import net.marwanaziz.gymsharktask.domain.ProductRepository

internal class FakeProductRepository : ProductRepository {
    var products: List<Product> = emptyList()
    var catalogueUnavailable: Boolean = false

    override suspend fun getProducts(): DomainResult<List<Product>, CatalogueUnavailable> {
        if (catalogueUnavailable) {
            return DomainResult.Failure(CatalogueUnavailable)
        }
        return DomainResult.Success(products)
    }
}

internal fun sampleProduct(id: Long = 1L): Product = Product(
    id = id,
    handle = "speed-leggings",
    title = "Speed Leggings",
    description = "<p><strong>RUN WITH IT</strong><br>Lightweight support.</p>",
    type = "Womens Leggings",
    gender = listOf("f"),
    fit = "compressive",
    colour = "Navy",
    price = 1000,
    compareAtPrice = null,
    discountPercentage = null,
    featuredImageUrl = "https://cdn.example/featured.jpg",
    imageUrls = listOf("https://cdn.example/gallery.jpg"),
    labels = listOf("going-fast"),
    variants = emptyList(),
)

@OptIn(ExperimentalCoroutinesApi::class)
internal fun runViewModelTest(testBody: suspend TestScope.(CoroutineDispatcher) -> Unit) = runTest {
    val dispatcher = StandardTestDispatcher(testScheduler)
    Dispatchers.setMain(dispatcher)
    try {
        testBody(dispatcher)
    } finally {
        Dispatchers.resetMain()
    }
}
