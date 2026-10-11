package net.marwanaziz.gymsharktask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import net.marwanaziz.gymsharktask.domain.Product
import net.marwanaziz.gymsharktask.feature.products.detail.ProductDetailScreen
import net.marwanaziz.gymsharktask.feature.products.list.ProductListScreen
import net.marwanaziz.gymsharktask.feature.products.list.ProductListState
import net.marwanaziz.gymsharktask.feature.products.list.ProductListViewModel
import net.marwanaziz.gymsharktask.ui.theme.GymSharkTaskTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val coordinator = ProductCoordinator()
    private var restoredProductId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restoredProductId = savedInstanceState
            ?.takeIf { it.containsKey(KEY_PRODUCT_ID) }
            ?.getLong(KEY_PRODUCT_ID)
        enableEdgeToEdge()
        setContent {
            GymSharkTaskTheme {
                CatalogueRoute(coordinator)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val destination = coordinator.destination.value
        if (destination is ProductDestination.Detail) {
            outState.putLong(KEY_PRODUCT_ID, destination.product.id)
        }
    }

    @Composable
    private fun CatalogueRoute(coordinator: ProductCoordinator) {
        val listViewModel: ProductListViewModel = hiltViewModel()
        val listState by listViewModel.state.collectAsState()
        val destination by coordinator.destination.collectAsState()
        val pendingProductId = restoredProductId

        LaunchedEffect(listState, pendingProductId) {
            if (pendingProductId == null || destination !is ProductDestination.List) {
                return@LaunchedEffect
            }
            val products = (listState as? ProductListState.Success)?.products ?: return@LaunchedEffect
            restoredProductId = null
            products.find { it.id == pendingProductId }?.let(coordinator::onProductSelected)
        }

        AnimatedContent(
            targetState = destination,
            modifier = Modifier.fillMaxSize(),
            transitionSpec = {
                if (targetState is ProductDestination.Detail) {
                    slideInHorizontally(navigationSpec) { width -> width } togetherWith
                        slideOutHorizontally(navigationSpec) { width -> -width }
                } else {
                    slideInHorizontally(navigationSpec) { width -> -width } togetherWith
                        slideOutHorizontally(navigationSpec) { width -> width }
                }
            },
            label = "catalogueNavigation",
        ) { current ->
            when (current) {
                ProductDestination.List -> ProductListRoute(
                    viewModel = listViewModel,
                    onProductSelected = coordinator::onProductSelected,
                )
                is ProductDestination.Detail -> {
                    BackHandler(onBack = coordinator::onBack)
                    ProductDetailScreen(
                        product = current.product,
                        onBack = coordinator::onBack,
                    )
                }
            }
        }
    }

    private companion object {
        const val KEY_PRODUCT_ID = "product_id"
        val navigationSpec = tween<IntOffset>(durationMillis = 300)
    }
}

@Composable
private fun ProductListRoute(
    viewModel: ProductListViewModel,
    onProductSelected: (Product) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(viewModel) {
        viewModel.productSelected.collect(onProductSelected)
    }
    ProductListScreen(
        state = state,
        onRetry = viewModel::retry,
        onProductTapped = viewModel::onProductTapped,
    )
}
