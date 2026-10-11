package net.marwanaziz.gymsharktask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import net.marwanaziz.gymsharktask.feature.products.list.ProductListScreen
import net.marwanaziz.gymsharktask.feature.products.list.ProductListViewModel
import net.marwanaziz.gymsharktask.ui.theme.GymSharkTaskTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GymSharkTaskTheme {
                ProductListRoute()
            }
        }
    }
}

@Composable
private fun ProductListRoute() {
    val viewModel: ProductListViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    ProductListScreen(
        state = state,
        onRetry = viewModel::retry,
        onProductTapped = viewModel::onProductTapped,
    )
}