package com.charlton.gameengine.helpers

import java.awt.Font
import java.io.File

object FontHelper {

    const val FONT_FOLDER = "assets/fonts";

    fun get(fontString: String, size: Float): Font {
        return try {
            val fontPath = "$FONT_FOLDER/$fontString"
            val file = File(fontPath)
            val font = Font.createFont(Font.TRUETYPE_FONT, file)
            font.deriveFont(size)
        } catch (e: Exception) {
            Font(Font.MONOSPACED, Font.PLAIN, size.toInt())
        }
    }

    fun get(fontString: String, size: Int): Font = get(fontString, size.toFloat())

}
