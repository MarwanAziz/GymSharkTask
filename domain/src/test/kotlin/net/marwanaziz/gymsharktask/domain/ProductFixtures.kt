package net.marwanaziz.gymsharktask.domain

internal fun product(
    id: Long,
    title: String? = "Crest Hoodie",
    colour: String? = "Black",
): Product = Product(
    id = id,
    handle = "crest-hoodie",
    title = title,
    description = "<p>Warm fleece hoodie.</p>",
    type = "Hoodie",
    gender = null,
    fit = null,
    colour = colour,
    price = 4500,
    compareAtPrice = 5500,
    discountPercentage = 18.0,
    featuredImageUrl = "https://example.com/featured.jpg",
    imageUrls = listOf("https://example.com/gallery.jpg"),
    labels = listOf("new"),
    variants = listOf(
        ProductVariant(
            id = 1,
            size = "M",
            sku = "sku-m",
            isAvailable = true,
            price = 4500,
        ),
    ),
)
