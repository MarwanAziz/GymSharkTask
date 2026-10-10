package net.marwanaziz.gymsharktask.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class CatalogueResponse(
    val hits: List<CatalogueHit>,
)

@Serializable
data class CatalogueHit(
    val id: Long,
    val sku: String? = null,
    val handle: String? = null,
    val title: String? = null,
    val description: String? = null,
    val type: String? = null,
    val colour: String? = null,
    val gender: List<String>? = null,
    val fit: String? = null,
    val labels: List<String>? = null,
    val inStock: Boolean? = null,
    val sizeInStock: List<String>? = null,
    val price: Int? = null,
    val compareAtPrice: Int? = null,
    val discountPercentage: Double? = null,
    val featuredMedia: CatalogueMedia? = null,
    val media: List<CatalogueMedia>? = null,
    val availableSizes: List<CatalogueAvailableSize>? = null,
    val objectID: String? = null,
)

@Serializable
data class CatalogueMedia(
    val src: String? = null,
)

@Serializable
data class CatalogueAvailableSize(
    val id: Long,
    val inStock: Boolean? = null,
    val inventoryQuantity: Int? = null,
    val price: Int? = null,
    val size: String? = null,
    val sku: String? = null,
)
