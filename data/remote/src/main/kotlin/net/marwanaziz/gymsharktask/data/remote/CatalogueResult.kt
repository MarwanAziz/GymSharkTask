package net.marwanaziz.gymsharktask.data.remote

sealed interface CatalogueResult {
    data class Success(val catalogue: CatalogueResponse) : CatalogueResult

    data object TransportFailure : CatalogueResult

    data class HttpFailure(val code: Int) : CatalogueResult

    data object UnreadableBody : CatalogueResult
}
