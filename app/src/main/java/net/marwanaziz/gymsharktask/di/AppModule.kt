package net.marwanaziz.gymsharktask.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import net.marwanaziz.gymsharktask.data.ProductMapper
import net.marwanaziz.gymsharktask.data.RemoteProductRepository
import net.marwanaziz.gymsharktask.data.remote.CatalogueClient
import net.marwanaziz.gymsharktask.data.remote.CatalogueRemote
import net.marwanaziz.gymsharktask.domain.ProductRepository
import net.marwanaziz.gymsharktask.domain.ProductUseCase
import net.marwanaziz.gymsharktask.feature.products.MainImmediateDispatcher

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun catalogueRemote(): CatalogueRemote = CatalogueClient(CATALOGUE_URL)

    @Provides
    @Singleton
    fun productRepository(catalogueRemote: CatalogueRemote): ProductRepository {
        return RemoteProductRepository(
            catalogueRemote = catalogueRemote,
            productMapper = ProductMapper(),
        )
    }

    @Provides
    @Singleton
    fun productUseCase(repository: ProductRepository): ProductUseCase {
        return ProductUseCase(repository)
    }

    @Provides
    @MainImmediateDispatcher
    fun mainImmediateDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate

    private const val CATALOGUE_URL =
        "https://cdn.develop.gymshark.com/training/mock-product-responses/algolia-example-payload.json"
}
