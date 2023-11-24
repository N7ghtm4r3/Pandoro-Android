package com.tecknobit.pandoro.ui.theme

import androidx.compose.ui.graphics.Color
import com.tecknobit.pandoro.helpers.ui.BACKGROUND_COLOR
import com.tecknobit.pandoro.helpers.ui.GREEN_COLOR
import com.tecknobit.pandoro.helpers.ui.PRIMARY_COLOR
import com.tecknobit.pandoro.helpers.ui.RED_COLOR
import com.tecknobit.pandoro.helpers.ui.YELLOW_COLOR

/**
 * the primary color of the application
 */
val PrimaryLight = fromHexToColor(PRIMARY_COLOR)

/**
 * the error color of the application
 */
val ErrorLight = fromHexToColor(RED_COLOR)

/**
 * the background color of the application
 */
val BackgroundLight = fromHexToColor(BACKGROUND_COLOR)

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
val GREEN_COLOR: Color = fromHexToColor(GREEN_COLOR)

/**
 * the yellow color value
 */
val YELLOW_COLOR: Color = fromHexToColor(YELLOW_COLOR)

/**
 * Method to create a [Color] from an hex [String]
 * @param hex: hex value to transform
 *
 * @return color as [Color]
 */
fun fromHexToColor(hex: String): Color {
    return Color(("ff" + hex.removePrefix("#").lowercase()).toLong(16))
}
