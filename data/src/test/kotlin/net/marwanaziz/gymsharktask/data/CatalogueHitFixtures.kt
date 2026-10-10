package net.marwanaziz.gymsharktask.data

import net.marwanaziz.gymsharktask.data.remote.CatalogueAvailableSize
import net.marwanaziz.gymsharktask.data.remote.CatalogueHit
import net.marwanaziz.gymsharktask.data.remote.CatalogueMedia

internal fun catalogueHit(
    id: Long = 6732609257571L,
    handle: String? = "gymshark-speed-leggings-navy-ss22",
    title: String? = "Speed Leggings",
    description: String? = "<p>RUN WITH IT</p>",
    type: String? = "Womens Leggings",
    gender: List<String>? = listOf("f"),
    fit: String? = "compressive",
    colour: String? = "Navy",
    price: Int? = 1000,
    compareAtPrice: Int? = 1500,
    discountPercentage: Double? = 33.0,
    labels: List<String>? = null,
    featuredMedia: CatalogueMedia? = CatalogueMedia(src = FEATURED_SRC),
    media: List<CatalogueMedia>? = listOf(CatalogueMedia(src = MEDIA_SRC)),
    availableSizes: List<CatalogueAvailableSize>? = listOf(availableSize()),
): CatalogueHit = CatalogueHit(
    id = id,
    handle = handle,
    title = title,
    description = description,
    type = type,
    gender = gender,
    fit = fit,
    colour = colour,
    price = price,
    compareAtPrice = compareAtPrice,
    discountPercentage = discountPercentage,
    labels = labels,
    featuredMedia = featuredMedia,
    media = media,
    availableSizes = availableSizes,
)

internal fun availableSize(
    id: Long = 39814344835171L,
    inStock: Boolean? = true,
    price: Int? = 1000,
    size: String? = "xs",
    sku: String? = "B3A3E-UBCY-XS",
): CatalogueAvailableSize = CatalogueAvailableSize(
    id = id,
    inStock = inStock,
    price = price,
    size = size,
    sku = sku,
)

internal const val FEATURED_SRC = "https://cdn.example/featured.jpg"
internal const val MEDIA_SRC = "https://cdn.example/gallery.jpg"
