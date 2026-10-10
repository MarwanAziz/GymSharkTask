package net.marwanaziz.gymsharktask.domain

sealed interface DomainError

sealed interface GetProductError : DomainError

data object CatalogueUnavailable : GetProductError

data class ProductNotFound(val id: Long) : GetProductError

sealed interface DomainResult<out T, out E : DomainError> {
    data class Success<T>(val value: T) : DomainResult<T, Nothing>

    data class Failure<E : DomainError>(val error: E) : DomainResult<Nothing, E>
}
