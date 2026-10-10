package net.marwanaziz.gymsharktask.data

import net.marwanaziz.gymsharktask.data.remote.CatalogueRemote
import net.marwanaziz.gymsharktask.data.remote.CatalogueResult

internal class FakeCatalogueRemote(
    private val result: CatalogueResult,
) : CatalogueRemote {
    override suspend fun fetchCatalogue(): CatalogueResult = result
}
