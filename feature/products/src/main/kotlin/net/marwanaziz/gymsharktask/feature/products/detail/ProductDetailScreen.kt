package net.marwanaziz.gymsharktask.feature.products.detail

import android.text.Html
import android.widget.TextView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import net.marwanaziz.gymsharktask.domain.Product
import net.marwanaziz.gymsharktask.domain.ProductVariant
import net.marwanaziz.gymsharktask.feature.products.ProductImage
import net.marwanaziz.gymsharktask.feature.products.formatPrice

@Composable
fun ProductDetailScreen(
    product: Product,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            TextButton(onClick = onBack) {
                Text("Back")
            }
            ProductDetailContent(product)
        }
    }
}

@Composable
private fun ProductDetailContent(product: Product) {
    val locale = LocalConfiguration.current.locales[0]
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ProductGallery(product)
        Text(
            text = product.title.orEmpty(),
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = product.colour.orEmpty(),
            style = MaterialTheme.typography.bodyLarge,
        )
        product.type?.takeIf { it.isNotBlank() }?.let { type ->
            Text(text = type, style = MaterialTheme.typography.bodyMedium)
        }
        product.price?.let { price ->
            Text(text = formatPrice(price, locale), style = MaterialTheme.typography.titleLarge)
        }
        ProductLabels(product.labels.orEmpty())
        ProductSizes(product.variants.orEmpty())
        product.description?.takeIf { it.isNotBlank() }?.let { description ->
            HtmlDescription(description)
        }
    }
}

@Composable
private fun ProductGallery(product: Product) {
    val urls = galleryUrls(product)
    if (urls.isEmpty()) {
        ProductImage(
            url = null,
            contentDescription = product.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
                .clip(RoundedCornerShape(12.dp)),
        )
        return
    }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 4.dp),
    ) {
        items(urls) { url ->
            ProductImage(
                url = url,
                contentDescription = product.title,
                modifier = Modifier
                    .height(360.dp)
                    .aspectRatio(3f / 4f)
                    .clip(RoundedCornerShape(12.dp)),
            )
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
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
    }
}

@Composable
private fun ProductSizes(variants: List<ProductVariant>) {
    if (variants.isEmpty()) return
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = "Sizes", style = MaterialTheme.typography.titleMedium)
        variants.forEach { variant ->
            val availability = sizeAvailabilityLabel(variant.isAvailable)
            Text("${variant.size.orEmpty()} · $availability")
        }
    }
}

@Composable
private fun HtmlDescription(html: String) {
    val textColor = MaterialTheme.colorScheme.onSurface.toArgb()
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context -> TextView(context) },
        update = { textView ->
            textView.text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
            textView.setTextColor(textColor)
        },
    )
}

internal fun sizeAvailabilityLabel(isAvailable: Boolean?): String {
    return when (isAvailable) {
        true -> "In stock"
        false -> "Out of stock"
        null -> "Contact us"
    }
}

private fun galleryUrls(product: Product): List<String> {
    val gallery = product.imageUrls.orEmpty().filter { it.isNotBlank() }
    if (gallery.isNotEmpty()) {
        return gallery
    }
    return listOfNotNull(product.featuredImageUrl?.takeIf { it.isNotBlank() })
}
