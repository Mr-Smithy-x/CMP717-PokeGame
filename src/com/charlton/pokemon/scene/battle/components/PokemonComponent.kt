package com.charlton.pokemon.scene.battle.components

import com.charlton.gameengine.camera.GlobalCamera2D
import com.charlton.gameengine.contracts.Renderable
import com.charlton.pokemon.models.Pokemon
import java.awt.Graphics

data class PokemonComponent(private val pokemon: Pokemon): Renderable {

    val width get() = GlobalCamera2D.windowWidth
    val height get() = GlobalCamera2D.windowHeight

    override fun render(g: Graphics) {

        val backPkmn = pokemon.getBackSprite()
        val backPkmnWidth = backPkmn.getWidth(null) * 2
        val backPkmnHeight = backPkmn.getHeight(null) * 2
        g.drawImage(backPkmn, (width * 0.15).toInt(), (height * 0.33).toInt(),
            backPkmnWidth, backPkmnHeight,null)
    }

}