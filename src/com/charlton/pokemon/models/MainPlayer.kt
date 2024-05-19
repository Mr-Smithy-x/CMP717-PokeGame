package com.charlton.pokemon.models

import com.charlton.gameengine.world.scenes.SceneType
import com.charlton.network.client.PokeGameClient
import com.charlton.network.models.NetworkState
import com.charlton.network.models.NetworkState.PokePlayerState
import com.charlton.pokemon.Global
import com.charlton.pokemon.scene.battle.Item
import com.charlton.spritesheeteditor.models.PoseFileFormat
import java.io.Serializable

class MainPlayer(override val name: String = "Ash") : Player(name, PoseFileFormat.load("goku.pose")!!), Serializable {
    private var _bag = mutableListOf<Item>()
    val bag: List<Item>
        get() {
            cleanBag()
            return _bag
        }

    init {
        _bag.add(Item()) // adds potion
        _bag.add(Item("Super Potion", points = 50)) // adds potion
        _bag.add(Item("Hyper Potion", points = 200)) // adds potion
        _bag.add(Item("Attack X", Item.ItemType.ATTACK, points = 5)) // adds potion
        _bag.add(Item("Defense X", Item.ItemType.DEFENSE, points = 5)) // adds potion
        _bag.add(Item("Speed X", Item.ItemType.SPEED, points = 5)) // adds potion
        _bag.add(Item("Sp Defense X", Item.ItemType.SP_ATTACK, points = 5)) // adds potion
        _bag.add(Item("Sp Attack X", Item.ItemType.SP_DEFENSE, points = 5)) // adds potion
    }

    private fun cleanBag() {
        _bag = _bag.filter { it.quantity > 0 }.toMutableList()
    }

    override fun setSpritePoseAndMove(pose: Pose, x: Float) {
        super.setSpritePoseAndMove(pose, x)
        PokeGameClient.update(this)
    }

    val sceneState: NetworkState.SceneState
        get() {
            return NetworkState.SceneState(
                (Global.game.world.manager.currentScene ?: SceneType.SandScene).id
            )
        }

    fun toMultiPlayer(): PokePlayerState {
        return PokePlayerState(
            PokeGameClient.id,
            name,
            sceneState,
            pokemon.map { it.toSerializable() },
            selectedPokemon,
            direction,
            currentPose,
            location
        )
    }

}