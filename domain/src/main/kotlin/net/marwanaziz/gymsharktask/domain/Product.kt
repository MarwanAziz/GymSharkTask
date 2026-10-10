package net.marwanaziz.gymsharktask.domain

data class Product(
    val id: Long,
    val handle: String?,
    val title: String?,
    val description: String?,
    val type: String?,
    val gender: List<String>?,
    val fit: String?,
    val colour: String?,
    val price: Int?,
    val compareAtPrice: Int?,
    val discountPercentage: Double?,
    val featuredImageUrl: String?,
    val imageUrls: List<String>?,
    val labels: List<String>?,
    val variants: List<ProductVariant>?,
)

data class ProductVariant(
    val id: Long,
    val size: String?,
    val sku: String?,
    val isAvailable: Boolean?,
    val price: Int?,
)
