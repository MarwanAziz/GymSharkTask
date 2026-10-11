package net.marwanaziz.gymsharktask.feature.products

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

internal fun formatPrice(minorUnits: Int, locale: Locale): String {
    val format = NumberFormat.getCurrencyInstance(locale)
    format.minimumFractionDigits = 2
    format.maximumFractionDigits = 2
    return format.format(BigDecimal.valueOf(minorUnits.toLong()).movePointLeft(2))
}
