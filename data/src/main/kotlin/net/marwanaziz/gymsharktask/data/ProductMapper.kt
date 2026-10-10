package net.marwanaziz.gymsharktask.data

import net.marwanaziz.gymsharktask.data.remote.CatalogueHit
import net.marwanaziz.gymsharktask.domain.Product
import net.marwanaziz.gymsharktask.domain.ProductVariant

class ProductMapper {
    fun map(hit: CatalogueHit): Product {
        return Product(
            id = hit.id,
            handle = hit.handle,
            title = hit.title,
            description = hit.description,
            type = hit.type,
            gender = hit.gender,
            fit = hit.fit,
            colour = hit.colour,
            price = hit.price,
            compareAtPrice = hit.compareAtPrice,
            discountPercentage = hit.discountPercentage,
            featuredImageUrl = hit.featuredMedia?.src?.takeIf { it.isNotBlank() },
            imageUrls = hit.media.orEmpty().mapNotNull { media ->
                media.src?.takeIf { it.isNotBlank() }
            },
            labels = hit.labels.orEmpty(),
            variants = hit.availableSizes.orEmpty().map { size ->
                ProductVariant(
                    id = size.id,
                    size = size.size,
                    sku = size.sku,
                    isAvailable = size.inStock,
                    price = size.price,
                )
            },
        )
    }
}
