package net.marwanaziz.gymsharktask.domain

interface ProductRepository {
    suspend fun getProducts(): DomainResult<List<Product>, CatalogueUnavailable>

    suspend fun getProduct(id: Long): DomainResult<Product, GetProductError>
}
