package com.charlton.pokemon.scene.battle.components

import com.charlton.gameengine.camera.GlobalCamera2D
import com.charlton.gameengine.contracts.Renderable
import com.charlton.gameengine.helpers.FontHelper
import com.charlton.pokemon.models.EnemyPlayer
import java.awt.Color
import java.awt.Graphics

//TODO: Add Pokemon name, Pokemon Level, HP txt, maybe a slanted hud effect.
class EnemyPokemonHudComponent(val enemy: EnemyPlayer) : Renderable {

    val width get() = GlobalCamera2D.windowWidth
    val height get() = GlobalCamera2D.windowHeight

    val pokemon
        get() = enemy.currentSelectedPokemon


    override fun render(g: Graphics) {


        g.color = Color.decode("0xCCCCCC")

        val containerHeight = height / 6
        val containerWidth = (width / 2.5).toInt()


        val containerStartOfX = (width * 0.04).toInt()
        val containerStartOfY = (height * 0.04).toInt()


        val x = (containerWidth * (.40)).toInt()
        val lifeWidth = (containerWidth * (.75)).toInt() - x / 2

        val lifeBarStart = containerStartOfX + x
        val lifeBarEnd = lifeWidth - 3
        val base_percentage =
            (1 - ((pokemon.stats.maxHealth.toFloat() - pokemon.health.toFloat()) / pokemon.stats.maxHealth.toFloat()))
        val newLifeBarWidth = (lifeBarEnd * base_percentage).toInt()
        //region Ignore
        g.fillRoundRect(containerStartOfX, containerStartOfY, containerWidth, containerHeight, 10, 10)
        g.color = Color.decode("0x888888")
        g.drawRoundRect(containerStartOfX, containerStartOfY, containerWidth, containerHeight, 10, 10)
        g.color = Color.decode("0x444444")
        g.fillRoundRect(
            containerStartOfX + x / 2,
            (containerHeight * (1 - .13)).toInt(),
            lifeWidth + x / 2 + 1,
            15,
            10,
            10
        )
        g.color = Color.decode("0xFFFFFF")
        g.fillRoundRect(lifeBarStart - 1, (containerHeight * (1 - .11)).toInt(), lifeWidth, 13, 10, 10)

        g.color = Color.decode("0x666666")
        g.drawRoundRect(lifeBarStart, (containerHeight * (1 - .10)).toInt(), lifeBarEnd, 10, 10, 10)
        //endregion

        when {
            base_percentage > 0.5f -> g.color = Color.decode("0x009900")
            base_percentage > 0.2f -> g.color = Color.decode("0xFFDD00")
            else -> g.color = Color.decode("0xDD0000")
        }
        g.fillRoundRect(
            /* x = */  lifeBarStart,
            /* y = */ (containerHeight * (1 - .10)).toInt(),
            /* width = */ newLifeBarWidth,
            /* height = */ 10,
            /* arcWidth = */ 10,
            /* arcHeight = */ 10
        )

        when {
            base_percentage > 0.5f -> g.color = Color.decode("0x006600")
            base_percentage > 0.2f -> g.color = Color.decode("0x996600")
            else -> g.color = Color.decode("0x660000")
        }
        g.drawRoundRect(
            /* x = */ lifeBarStart,
            /* y = */ (containerHeight * (1 - .10)).toInt(),
            /* width = */ newLifeBarWidth,
            /* height = */ 10,
            /* arcWidth = */ 10,
            /* arcHeight = */ 10
        )


        g.font = FontHelper.get("font.ttf", 14)
        var fontWidth = g.fontMetrics.stringWidth("HP")
        g.color = Color.decode("0xFF8800")

        g.drawString(
            "HP",
            containerStartOfX + x / 2 + (fontWidth / 3),
            (containerHeight * (1 - .065)).toInt() + g.fontMetrics.height / 2
        )

        g.color = Color.BLACK
        g.font = FontHelper.get("font.ttf", 20)
        g.drawString(
            pokemon.name,
            (containerStartOfX + (containerWidth * 0.05)).toInt(),
            containerStartOfY + g.fontMetrics.height
        )

        g.color = Color.BLACK
        fontWidth = g.fontMetrics.stringWidth("Lv${pokemon.level}")
        g.drawString(
            "Lv${pokemon.level}",
            (containerWidth + containerStartOfX / 2 - fontWidth).toInt(),
            containerStartOfY + g.fontMetrics.height
        )
        /*
        g.fillRoundRect(10, 10, containerWidth, containerHeight, 10, 10)

        g.color = Color.decode("0x888888")
        g.drawRoundRect(10, 10, containerWidth, containerHeight, 10, 10)


        g.color = Color.decode("0x444444")
        g.fillRoundRect(50,containerHeight-12, containerWidth-50, 15, 10, 10)

        g.color = Color.decode("0xFFFFFF")
        g.fillRoundRect(79,containerHeight-11, containerWidth-80, 13, 10, 10)

        g.color = Color.decode("0x666666")
        g.drawRoundRect(80,containerHeight-10, containerWidth-83, 10, 10, 10)


        g.color = Color.decode("0x009900")
        g.fillRoundRect(80,containerHeight-10, containerWidth-83, 10, 10, 10)

        g.color = Color.decode("0x006600")
        g.drawRoundRect(80,containerHeight-10, containerWidth-83, 10, 10, 10)


        g.font = FontHelper.get("font.ttf", 14)
        val fontWidth = g.fontMetrics.stringWidth("HP")
        g.color = Color.decode("0xFF8800")

        g.drawString("HP", 50 + (80-50)/2 - (fontWidth / 2), containerHeight - 8 + g.fontMetrics.height/2)*/
    }

}