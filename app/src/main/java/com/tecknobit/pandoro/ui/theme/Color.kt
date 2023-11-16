package com.tecknobit.pandoro.ui.theme

import androidx.compose.ui.graphics.Color

// TODO: IMPORTED COLORS FROM THE LIBRARY

/**
 * the primary color of the application
 */
val PrimaryLight = fromHexToColor("#07020d")

/**
 * the error color of the application
 */
val ErrorLight = fromHexToColor("#A81515")

/**
 * the background color of the application
 */
val BackgroundLight = fromHexToColor("#f9f6f0")

/**
 * the ice gray color of the application
 */
val IceGrayColor = fromHexToColor("#dae2ff")

/**
 * the dwarf white color of the application
 */
val DwarfWhiteColor = fromHexToColor("#fafdfd")

/**
 * the custom gray color of the application
 */
val CustomGrayColor = fromHexToColor("#e6e8e9")

/**
 * the green color value
 */
val GREEN_COLOR: Color = fromHexToColor("#61892f")

/**
 * the yellow color value
 */
val YELLOW_COLOR: Color = fromHexToColor("#bfae19")

/**
 * Method to create a [Color] from an hex [String]
 * @param hex: hex value to transform
 *
 * @return color as [Color]
 */
fun fromHexToColor(hex: String): Color {
    return Color(("ff" + hex.removePrefix("#").lowercase()).toLong(16))
}
