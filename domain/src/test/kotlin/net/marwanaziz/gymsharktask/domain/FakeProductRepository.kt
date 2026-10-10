package net.marwanaziz.gymsharktask.domain

internal class FakeProductRepository(
    private val products: List<Product> = emptyList(),
    private val catalogueUnavailable: Boolean = false,
) : ProductRepository {
    var requestedId: Long? = null
        private set

    override suspend fun getProducts(): DomainResult<List<Product>, CatalogueUnavailable> {
        if (catalogueUnavailable) {
            return DomainResult.Failure(CatalogueUnavailable)
        }
        return DomainResult.Success(products)
    }

    override suspend fun getProduct(id: Long): DomainResult<Product, GetProductError> {
        requestedId = id
        if (catalogueUnavailable) {
            return DomainResult.Failure(CatalogueUnavailable)
        }
        val product = products.firstOrNull { it.id == id }
            ?: return DomainResult.Failure(ProductNotFound(id))
        return DomainResult.Success(product)
    }
}
