package com.charlton.pokemon.scene.battle.components

import com.charlton.gameengine.camera.GlobalCamera2D
import com.charlton.gameengine.contracts.Renderable
import com.charlton.gameengine.helpers.FontHelper
import java.awt.Color
import java.awt.Graphics

class BattleBoxMessage: Renderable {

    private var text = arrayOf<String>()
    val width get() = GlobalCamera2D.windowWidth
    val height get() = GlobalCamera2D.windowHeight


    fun setText(text: String) {
        this.text = text.split("\n").toTypedArray()
    }

    override fun render(g: Graphics) {

        val heightOfBox = height / 3
        val base_y = height - heightOfBox

        g.color = Color(0f, 0f, 0f, 0.3f)
        g.fillRect(0, 0, width, height)

        g.color = Color.decode("0x484353")
        g.fillRect(0, base_y, width, heightOfBox)


        g.color = Color.decode("0xc1513a")

        val redWidth = width * 0.9935
        val remainder = ((width % redWidth) / 2).toInt()

        val tealWidth = width * 0.9765
        val remainderTealWidth = ((width % tealWidth) / 2).toInt()

        g.fillRoundRect(remainder, base_y + 10, redWidth.toInt(), heightOfBox - 20, 30, 30)

        g.color = Color.decode("0x587e82")
        g.fillRoundRect(remainderTealWidth, base_y + 12, tealWidth.toInt(), heightOfBox - 24, 24, 24)


        if(this.text.isNotEmpty()) {
            g.color = Color.WHITE
            drawText(g)
        }
    }


    fun drawText(g: Graphics) {


        for (i in text.indices) {
            val heightOfBox = height / 3
            val base_y = height - heightOfBox - (heightOfBox / 8)

            val font = FontHelper.get("font.ttf", heightOfBox / 4)
            g.font = font
            val strWidth = g.fontMetrics.stringWidth(text[i])
            val strHeight = g.fontMetrics.height


            val tealWidth = width * 0.95
            val remainderTealWidth = ((width % tealWidth) / 2).toInt()

            g.drawString(text[i], remainderTealWidth, (base_y * 1.1175).toInt()  + strHeight + (i * strHeight) + (i * heightOfBox / 8))

        }
        /*g.drawString(
            text,
            (width / 2) - (strWidth / 2) - offsetX,
            (height / 2) + (g.fontMetrics.height / 2) - offsetY
        )*/
    }

}