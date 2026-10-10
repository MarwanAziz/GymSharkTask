package net.marwanaziz.gymsharktask.data

import net.marwanaziz.gymsharktask.data.remote.CatalogueRemote
import net.marwanaziz.gymsharktask.data.remote.CatalogueResult
import net.marwanaziz.gymsharktask.domain.CatalogueUnavailable
import net.marwanaziz.gymsharktask.domain.DomainResult
import net.marwanaziz.gymsharktask.domain.Product
import net.marwanaziz.gymsharktask.domain.ProductRepository

class RemoteProductRepository(
    private val catalogueRemote: CatalogueRemote,
    private val productMapper: ProductMapper,
) : ProductRepository {
    override suspend fun getProducts(): DomainResult<List<Product>, CatalogueUnavailable> {
        return when (val result = catalogueRemote.fetchCatalogue()) {
            is CatalogueResult.Success -> {
                DomainResult.Success(result.catalogue.hits.map(productMapper::map))
            }
            is CatalogueResult.HttpFailure,
            CatalogueResult.TransportFailure,
            CatalogueResult.UnreadableBody,
            -> DomainResult.Failure(CatalogueUnavailable)
        }
    }
}
