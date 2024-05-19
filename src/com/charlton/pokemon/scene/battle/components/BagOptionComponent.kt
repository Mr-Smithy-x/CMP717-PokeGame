package com.charlton.pokemon.scene.battle.components

import com.charlton.gameengine.camera.GlobalCamera2D
import com.charlton.gameengine.contracts.Renderable
import com.charlton.pokemon.Global
import com.charlton.pokemon.scene.battle.Item
import com.charlton.pokemon.sound.GlobalSoundEffect
import java.awt.Color
import java.awt.Graphics
import kotlin.math.roundToInt

class BagOptionComponent : Renderable {

    val width get() = GlobalCamera2D.windowWidth
    val height get() = GlobalCamera2D.windowHeight

    var rowSelection = 0
        private set

    private var _options = Global.player.bag.map { BagOption(it, false) }.toMutableList()
    val options: List<BagOption> get() = _options


    override fun render(g: Graphics) {

        val heightOfBox = height / 3
        val base_y = height - heightOfBox


        g.color = Color.decode("0xAAAAAA")

        val redWidth = (width / 2.5).toInt()
        val remainder = (width - redWidth)

        g.fillRoundRect(remainder, base_y + 10, (redWidth - (redWidth * 0.01)).roundToInt(), heightOfBox - 20, 30, 30)
        drawSelection(g)

        for(i in options.indices) {
            drawString(g, i, options[i].item.name)
        }

    }

    private fun drawSelection(g: Graphics) {
        val option = options[rowSelection]
        if (option.selected) {
            val heightOfBox = height / 3
            val base_y = height - heightOfBox - (heightOfBox / 8)
            val redWidth = (width / 2.5).toInt()
            val remainder = (width - redWidth)

            g.color = Color(0x99, 0x66, 0x66, 0x66)
            val strHeight = g.fontMetrics.height
            val strWidth = g.fontMetrics.stringWidth(option.item.name)
            val x = (WINDOW_WIDTH - WINDOW_WIDTH / 4) - strWidth / 2
            val y = (base_y * 1.1175).toInt() + (rowSelection * strHeight) + (rowSelection * heightOfBox / 8)
            g.fillRoundRect(x, y, (strWidth * 1.3).toInt(), (strHeight * 1.3).toInt(), 10, 10)
            g.color = Color(0x99, 0x66, 0x66, 0xFF)
            g.drawRoundRect(x, y, (strWidth * 1.3).toInt(), (strHeight * 1.3).toInt(), 10, 10)
        }
    }


    private fun drawString(g: Graphics, row: Int, text: String) {

        val heightOfBox = height / 3
        val base_y = height - heightOfBox - (heightOfBox / 8)
        val redWidth = (width / 2.5).toInt()
        val remainder = (width - redWidth)

        g.color = Color.WHITE
        val strHeight = g.fontMetrics.height
        val stringWidth = g.fontMetrics.stringWidth(text)
        val x = (WINDOW_WIDTH - WINDOW_WIDTH / 4) - stringWidth / 2
        val y = (base_y * 1.1175).toInt() + strHeight + (row * strHeight) + (row * heightOfBox / 8)
        g.drawString(
            text,
            x,
            y
        )

    }


    fun setSelection(row: Int) {

        this.rowSelection = row

        if(rowSelection < 0) {
            rowSelection = 0
            //GlobalSoundEffect.play(GlobalSoundEffect.SOUND.CANTGO)
        } else if(rowSelection > 1) {
            rowSelection = 1
            //GlobalSoundEffect.play(GlobalSoundEffect.SOUND.CANTGO)
        }  else  {
            GlobalSoundEffect.play(GlobalSoundEffect.SOUND.SELECTION)
        }

        for (i in options.indices) {
            options[i].selected = false
        }
        options[rowSelection].selected = true
    }

    fun getSelection(): Item {
        return options[rowSelection].item
    }


    data class BagOption(val item: Item, var selected: Boolean = false)

}