package net.marwanaziz.gymsharktask.feature.products.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import net.marwanaziz.gymsharktask.domain.Product
import net.marwanaziz.gymsharktask.feature.products.ProductImage
import net.marwanaziz.gymsharktask.feature.products.formatPrice

@Composable
fun ProductListScreen(
    state: ProductListState,
    onRetry: () -> Unit,
    onProductTapped: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when (state) {
                ProductListState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                ProductListState.Error -> {
                    CatalogueError(
                        onRetry = onRetry,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                is ProductListState.Success -> {
                    if (state.products.isEmpty()) {
                        Text(
                            text = "No products",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    } else {
                        ProductGrid(
                            products = state.products,
                            onProductTapped = onProductTapped,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductGrid(
    products: List<Product>,
    onProductTapped: (Long) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(products, key = { it.id }) { product ->
            ProductCell(
                product = product,
                onProductTapped = onProductTapped,
            )
        }
    }
}

@Composable
private fun ProductCell(
    product: Product,
    onProductTapped: (Long) -> Unit,
) {
    val locale = LocalConfiguration.current.locales[0]
    Card(onClick = { onProductTapped(product.id) }) {
        Column {
            ProductImage(
                url = product.featuredImageUrl,
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f),
            )
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = product.title.orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = product.colour.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                )
                product.price?.let { price ->
                    Text(
                        text = formatPrice(price, locale),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                ProductLabels(product.labels.orEmpty())
            }
        }
    }
}

@Composable
private fun ProductLabels(labels: List<String>) {
    if (labels.isEmpty()) return
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        labels.forEach { label ->
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .padding(top = 4.dp),
            )
        }
    }
}

@Composable
private fun CatalogueError(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Couldn't load products")
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Preview(showBackground = true, name = "Products")
@Composable
private fun ProductListScreenPreview() {
    ProductListPreview(
        ProductListState.Success(
            listOf(
                previewProduct(
                    id = 1L,
                    title = "Speed Leggings",
                    colour = "Navy",
                    price = 1_000,
                    labels = listOf("going-fast"),
                ),
                previewProduct(
                    id = 2L,
                    title = "Vital Seamless Leggings",
                    colour = "Tahoe Teal Marl",
                    price = 4_500,
                ),
            ),
        ),
    )
}

@Preview(showBackground = true, name = "Empty")
@Composable
private fun ProductListEmptyPreview() {
    ProductListPreview(ProductListState.Success(emptyList()))
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun ProductListLoadingPreview() {
    ProductListPreview(ProductListState.Loading)
}

@Preview(showBackground = true, name = "Error")
@Composable
private fun ProductListErrorPreview() {
    ProductListPreview(ProductListState.Error)
}

@Composable
private fun ProductListPreview(state: ProductListState) {
    MaterialTheme {
        ProductListScreen(
            state = state,
            onRetry = {},
            onProductTapped = {},
        )
    }
}

private fun previewProduct(
    id: Long,
    title: String,
    colour: String,
    price: Int,
    labels: List<String> = emptyList(),
): Product = Product(
    id = id,
    handle = null,
    title = title,
    description = null,
    type = null,
    gender = null,
    fit = null,
    colour = colour,
    price = price,
    compareAtPrice = null,
    discountPercentage = null,
    featuredImageUrl = null,
    imageUrls = null,
    labels = labels,
    variants = null,
)