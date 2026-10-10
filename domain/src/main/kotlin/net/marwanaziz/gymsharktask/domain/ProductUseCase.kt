package net.marwanaziz.gymsharktask.domain

class ProductUseCase(
    private val repository: ProductRepository,
) {
    suspend fun getProducts(): DomainResult<List<Product>, CatalogueUnavailable> {
        return repository.getProducts()
    }

    suspend fun getProduct(id: Long): DomainResult<Product, GetProductError> {
        return repository.getProduct(id)
    }
}
