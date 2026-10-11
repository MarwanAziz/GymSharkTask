package net.marwanaziz.gymsharktask.feature.products.list

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import net.marwanaziz.gymsharktask.domain.ProductUseCase
import net.marwanaziz.gymsharktask.feature.products.FakeProductRepository
import net.marwanaziz.gymsharktask.feature.products.runViewModelTest
import net.marwanaziz.gymsharktask.feature.products.sampleProduct
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductListViewModelTest {
    @Test
    fun loadingThenProducts() = runViewModelTest { dispatcher ->
        val product = sampleProduct()
        val repository = FakeProductRepository().apply { products = listOf(product) }
        val viewModel = ProductListViewModel(ProductUseCase(repository), dispatcher)

        assertEquals(ProductListState.Loading, viewModel.state.value)
        advanceUntilIdle()
        assertEquals(ProductListState.Success(listOf(product)), viewModel.state.value)
    }

    @Test
    fun successWithAnEmptyList() = runViewModelTest { dispatcher ->
        val viewModel = ProductListViewModel(
            ProductUseCase(FakeProductRepository()),
            dispatcher,
        )

        advanceUntilIdle()

        assertEquals(ProductListState.Success(emptyList()), viewModel.state.value)
    }

    @Test
    fun failureThenRetrySuccess() = runViewModelTest { dispatcher ->
        val product = sampleProduct()
        val repository = FakeProductRepository().apply { catalogueUnavailable = true }
        val viewModel = ProductListViewModel(ProductUseCase(repository), dispatcher)

        advanceUntilIdle()
        assertEquals(ProductListState.Error, viewModel.state.value)

        repository.catalogueUnavailable = false
        repository.products = listOf(product)
        viewModel.retry()
        advanceUntilIdle()

        assertEquals(ProductListState.Success(listOf(product)), viewModel.state.value)
    }

    @Test
    fun tapEmitsTheTappedId() = runViewModelTest { dispatcher ->
        val viewModel = ProductListViewModel(
            ProductUseCase(FakeProductRepository()),
            dispatcher,
        )
        val emitted = mutableListOf<Long>()
        val collection = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.productSelected.collect { emitted.add(it) }
        }

        viewModel.onProductTapped(7L)
        advanceUntilIdle()

        assertEquals(listOf(7L), emitted)
        collection.cancel()
    }
}
