package net.marwanaziz.gymsharktask.data.remote

interface CatalogueRemote {
    suspend fun fetchCatalogue(): CatalogueResult
}
