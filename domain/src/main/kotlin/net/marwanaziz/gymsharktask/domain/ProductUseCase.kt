package net.marwanaziz.gymsharktask.domain

class ProductUseCase(
    private val repository: ProductRepository,
) {
    suspend fun getProducts(): DomainResult<List<Product>, CatalogueUnavailable> {
        return repository.getProducts()
    }

    suspend fun getProduct(id: Long): DomainResult<Product, GetProductError> {
        return when (val result = repository.getProducts()) {
            is DomainResult.Failure -> DomainResult.Failure(result.error)
            is DomainResult.Success -> {
                result.value.find { it.id == id }
                    ?.let { DomainResult.Success(it) }
                    ?: DomainResult.Failure(ProductNotFound(id))
            }
        }
    }
}
