package com.charlton.pokemon.scene.battle.components

import com.charlton.gameengine.camera.GlobalCamera2D
import com.charlton.gameengine.contracts.Renderable
import com.charlton.pokemon.models.Pokemon
import com.charlton.pokemon.sound.GlobalSoundEffect
import java.awt.Color
import java.awt.Graphics
import kotlin.math.roundToInt

class AttackOptionComponent(var pokemon: Pokemon) : Renderable {

    val width get() = GlobalCamera2D.windowWidth
    val height get() = GlobalCamera2D.windowHeight

    var columnSelection = 0
        private set
    var rowSelection = 0
        private set

    val options = arrayOf(
        arrayOf<AttackMoveOption>(),
        arrayOf()
    )


    init {
        options[0] = pokemon.learnedMoves.take(2).map { AttackMoveOption(it, false) }.toTypedArray()
        options[1] = pokemon.learnedMoves.takeLast(2).map { AttackMoveOption(it, false) }.toTypedArray()
        options[rowSelection][columnSelection].selected = true
    }

    override fun render(g: Graphics) {

        val heightOfBox = height / 3
        val base_y = height - heightOfBox


        g.color = Color.decode("0xAAAAAA")

        val redWidth = (width / 2.5).toInt()
        val remainder = (width - redWidth)

        g.fillRoundRect(remainder, base_y + 10, (redWidth - (redWidth * 0.01)).roundToInt(), heightOfBox - 20, 30, 30)
        drawSelection(g)

        for(i in options.indices) {
            for(j in options.indices) {
                drawString(g, i, j, options[i][j].move.name)
            }
        }

    }

    private fun drawSelection(g: Graphics) {
        val option = options[rowSelection][columnSelection]
        if (option.selected) {
            val heightOfBox = height / 3
            val base_y = height - heightOfBox - (heightOfBox / 8)
            val redWidth = (width / 2.5).toInt()
            val remainder = (width - redWidth)

            g.color = Color(0x99, 0x66, 0x66, 0x66)
            val strHeight = g.fontMetrics.height
            val strWidth = g.fontMetrics.stringWidth(option.move.name)
            val x = (remainder + (remainder * .025) + (columnSelection * remainder * 0.4)).toInt()
            val y = (base_y * 1.1175).toInt() + (rowSelection * strHeight) + (rowSelection * heightOfBox / 8)
            g.fillRoundRect(x, y, (strWidth * 1.3).toInt(), (strHeight * 1.3).toInt(), 10, 10)
            g.color = Color(0x99, 0x66, 0x66, 0xFF)
            g.drawRoundRect(x, y, (strWidth * 1.3).toInt(), (strHeight * 1.3).toInt(), 10, 10)
        }
    }


    private fun drawString(g: Graphics, row: Int, column: Int, text: String) {

        val heightOfBox = height / 3
        val base_y = height - heightOfBox - (heightOfBox / 8)
        val redWidth = (width / 2.5).toInt()
        val remainder = (width - redWidth)

        g.color = Color.WHITE
        val strHeight = g.fontMetrics.height
        g.drawString(
            text,
            (remainder + (remainder * .05) + (column * remainder * 0.4)).toInt(),
            (base_y * 1.1175).toInt() + strHeight + (row * strHeight) + (row * heightOfBox / 8)
        )

    }


    fun setSelection(row: Int, column: Int) {

        this.rowSelection = row
        this.columnSelection = column

        if(rowSelection < 0) {
            rowSelection = 0
            //GlobalSoundEffect.play(GlobalSoundEffect.SOUND.CANTGO)
        } else if(columnSelection < 0) {
            columnSelection = 0
            //GlobalSoundEffect.play(GlobalSoundEffect.SOUND.CANTGO)
        } else if(rowSelection > 1) {
            rowSelection = 1
            //GlobalSoundEffect.play(GlobalSoundEffect.SOUND.CANTGO)
        } else if(columnSelection > 1) {
            columnSelection = 1
            //GlobalSoundEffect.play(GlobalSoundEffect.SOUND.CANTGO)
        } else  {
            GlobalSoundEffect.play(GlobalSoundEffect.SOUND.SELECTION)
        }

        for (i in options.indices) {
            for (j in options[i].indices) {
                options[i][j].selected = false
            }
        }
        options[rowSelection][columnSelection].selected = true
    }

    fun getSelection(): Pokemon.Move {
        return options[rowSelection][columnSelection].move
    }


    data class AttackMoveOption(val move: Pokemon.Move, var selected: Boolean = false)

}