package com.charlton.pokemon.models

import com.charlton.gameengine.camera.BaseCamera
import com.charlton.gameengine.world.scenes.SceneType
import com.charlton.network.models.NetworkState
import com.charlton.network.models.NetworkState.PokePlayerState
import com.charlton.pokemon.Global
import com.charlton.spritesheeteditor.models.PoseFileFormat
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

data class ClientEnemyPlayer(var id: Int = -1, override val name: String = "Client") : EnemyPlayer(name) {

    private var _sceneState: NetworkState.SceneState? = null
        private set

    val sceneState: NetworkState.SceneState
        get() {
            return _sceneState ?: NetworkState.SceneState(
                (Global.game.world.manager.currentScene ?: SceneType.SandScene).id
            )
        }

    override fun canRender(camera: BaseCamera): Boolean {
        return super.canRender(camera) && sceneState.id == Global.player.sceneState.id
    }

    override fun canFight(): Boolean {
        return super.canFight() && sceneState.id != SceneType.Battle.id
    }

    fun update(info: PokePlayerState) {
        id = info.id
        setSelectedPokemon(info.selectedPokemon)
        currentPose = info.pose
        direction = info.direction
        if(info.location.x.toInt() != getX())
            isActive = true
        if(info.location.y.toInt() != getY())
            isActive = true
        if(info.location.z.toInt() != getZ().toInt())
            isActive = true
        setX(info.location.x)
        setY(info.location.y)
        setZ(info.location.z)

        _sceneState = info.sceneState
        info.pokemon.forEachIndexed { index, serializableMon ->
            pokemon[index].setLevel(serializableMon.level)
            pokemon[index].stats.hp = serializableMon.stats.hp
            pokemon[index].stats.attack = serializableMon.stats.attack
            pokemon[index].stats.defense = serializableMon.stats.defense
            pokemon[index].stats.specialAttack = serializableMon.stats.specialAttack
            pokemon[index].stats.specialDefense = serializableMon.stats.specialDefense
            pokemon[index].stats.speed = serializableMon.stats.speed

            pokemon[index].learnedMoves[0] = serializableMon.moves[0]
            pokemon[index].learnedMoves[1] = serializableMon.moves[1]
            pokemon[index].learnedMoves[2] = serializableMon.moves[2]
            pokemon[index].learnedMoves[3] = serializableMon.moves[3]
        }
    }

}

open class EnemyPlayer(override val name: String = "Gary") : Player(name, PoseFileFormat.load("goku.pose")!!) {

    override fun initializeSheet(spriteSheet: String): BufferedImage {
        return ImageIO.read(
            File(
                String.format(
                    "%s%s",
                    SHEET_DIRECTORY,
                    spriteSheet.replace("goku.png", "goku-evil.png")
                )
            )
        )
    }

}