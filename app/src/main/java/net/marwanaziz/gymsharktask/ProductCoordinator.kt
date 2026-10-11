package net.marwanaziz.gymsharktask

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import net.marwanaziz.gymsharktask.domain.Product

class ProductCoordinator {
    private val current = MutableStateFlow<ProductDestination>(ProductDestination.List)
    val destination: StateFlow<ProductDestination> = current.asStateFlow()

    fun onProductSelected(product: Product) {
        current.value = ProductDestination.Detail(product)
    }

    fun onBack() {
        current.value = ProductDestination.List
    }
}

sealed interface ProductDestination {
    data object List : ProductDestination

    data class Detail(val product: Product) : ProductDestination
}
