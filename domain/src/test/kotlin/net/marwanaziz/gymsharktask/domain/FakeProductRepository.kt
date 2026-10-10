package net.marwanaziz.gymsharktask.domain

internal class FakeProductRepository(
    private val products: List<Product> = emptyList(),
    private val catalogueUnavailable: Boolean = false,
) : ProductRepository {
    var getProductsCallCount: Int = 0
        private set

    override suspend fun getProducts(): DomainResult<List<Product>, CatalogueUnavailable> {
        getProductsCallCount += 1
        if (catalogueUnavailable) {
            return DomainResult.Failure(CatalogueUnavailable)
        }
        return DomainResult.Success(products)
    }
}
