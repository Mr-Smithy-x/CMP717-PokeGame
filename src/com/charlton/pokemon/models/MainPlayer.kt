package com.charlton.pokemon.models

import com.charlton.gameengine.world.scenes.SceneType
import com.charlton.network.client.PokeGameClient
import com.charlton.network.models.NetworkState
import com.charlton.network.models.NetworkState.PokePlayerState
import com.charlton.pokemon.Global
import com.charlton.pokemon.scene.battle.BattleScene
import com.charlton.spritesheeteditor.models.PoseFileFormat
import java.io.Serializable

class MainPlayer(override val name: String = "Ash") : Player(name, PoseFileFormat.load("goku.pose")!!), Serializable {
    private var _bag = mutableListOf<BattleScene.Item>()
    val bag: List<BattleScene.Item>
        get() {
            cleanBag()
            return _bag
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
                (Global.game.world.manager.currentScene ?: SceneType.Default).id
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