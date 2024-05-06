package com.charlton.pokemon.models

import com.charlton.gameengine.exts.to3dCameraF
import com.charlton.gameengine.models.*
import com.charlton.gameengine.world.scenes.SceneType
import com.charlton.network.client.PokeGameClient
import com.charlton.network.models.NetworkState
import com.charlton.network.models.NetworkState.PokePlayerState
import com.charlton.pokemon.Global
import com.charlton.spritesheeteditor.models.PoseFileFormat
import java.awt.Color
import java.awt.Graphics
import java.io.Serializable


open class Player(open val name: String = "CJ", pose: PoseFileFormat) :
    AnimatedSprite(pose, 0, 0, 1.0, 64), Serializable {

    private var _selectedPokemon = 0
    private var _pokemon = mutableListOf<Pokemon>()
    val selectedPokemon
        get() = _selectedPokemon % _pokemon.size


    fun setSelectedPokemon(index: Int) {
        this._selectedPokemon = index % _pokemon.size
    }

    val pokemon: List<Pokemon> get() = _pokemon


    val currentSelectedPokemon: Pokemon get() {
        val findNextLivingPokemon = findNextLivingPokemon()
        return if(findNextLivingPokemon > selectedPokemon) {
            _selectedPokemon = findNextLivingPokemon
            pokemon[findNextLivingPokemon]
        } else {
            pokemon[selectedPokemon]
        }
    }

    //region Init
    override fun onInitAnimations() {
        this.animDict[Pose.RIGHT]?.flipXFrames()
    }
    //endregion

    override fun render3d(g: Graphics) {
        val position3d = location.to3dCameraF(size)
        if (inDebuggingMode()) {
            g.color = Color.BLUE
            g.color = Color.RED
            g.drawRect(position3d.x.toInt(), position3d.y.toInt(), position3d.width.toInt(), position3d.height.toInt())
        }
        g.drawImage(
            pokemon.first().getFrontSprite(),
            position3d.x.toInt(),
            position3d.y.toInt(),
            position3d.width.toInt(),
            position3d.height.toInt(),
            null
        )
    }

    fun hasPokemon(): Boolean {
        return pokemon.isNotEmpty()
    }

    fun addPokemon(pokemon: Pokemon) {
        _pokemon.add(pokemon)
    }

    open fun canFight(): Boolean {
        return _pokemon.any { it.health > 0 }
    }

    private fun findNextLivingPokemon(): Int {
        return pokemon.indexOfFirst { it.health > 0 }
    }

}
