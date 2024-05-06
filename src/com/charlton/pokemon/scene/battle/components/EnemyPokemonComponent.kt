package com.charlton.pokemon.scene.battle.components

import com.charlton.gameengine.camera.GlobalCamera2D
import com.charlton.gameengine.contracts.Renderable
import com.charlton.pokemon.models.EnemyPlayer
import java.awt.Graphics

data class EnemyPokemonComponent(val enemy: EnemyPlayer): Renderable {

    val width get() = GlobalCamera2D.windowWidth
    val height get() = GlobalCamera2D.windowHeight

    val pokemon
        get() = enemy.currentSelectedPokemon

    override fun render(g: Graphics) {
        val frontEnemy = pokemon.getFrontSprite()

        val frontPkmnWidth = frontEnemy.getWidth(null) * 2
        val frontPkmnHeight = frontEnemy.getHeight(null) * 2

        g.drawImage(frontEnemy, (width * 0.65).toInt(), (height * 0.08).toInt(), frontPkmnWidth, frontPkmnHeight,null)


    }

}