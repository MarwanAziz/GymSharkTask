package net.marwanaziz.gymsharktask.feature.products.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.marwanaziz.gymsharktask.domain.DomainResult
import net.marwanaziz.gymsharktask.domain.Product
import net.marwanaziz.gymsharktask.domain.ProductUseCase
import net.marwanaziz.gymsharktask.feature.products.MainImmediateDispatcher

sealed interface ProductListState {
    data object Loading : ProductListState

    data class Success(val products: List<Product>) : ProductListState

    data object Error : ProductListState
}

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productUseCase: ProductUseCase,
    @param:MainImmediateDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _state = MutableStateFlow<ProductListState>(ProductListState.Loading)
    val state: StateFlow<ProductListState> = _state.asStateFlow()

    private val productSelections = MutableSharedFlow<Long>(extraBufferCapacity = 1)
    val productSelected: SharedFlow<Long> = productSelections.asSharedFlow()

    init {
        load()
    }

    fun retry() {
        load()
    }

    fun onProductTapped(id: Long) {
        productSelections.tryEmit(id)
    }

    private fun load() {
        viewModelScope.launch(dispatcher) {
            _state.value = ProductListState.Loading
            _state.value = when (val result = productUseCase.getProducts()) {
                is DomainResult.Success -> ProductListState.Success(result.value)
                is DomainResult.Failure -> ProductListState.Error
            }
        }
    }
}
