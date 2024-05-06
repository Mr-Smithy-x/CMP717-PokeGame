package com.charlton.network.models

import com.charlton.gameengine.models.Direction
import com.charlton.gameengine.models.ImageObject
import com.charlton.gameengine.models.Location
import com.charlton.pokemon.models.ClientEnemyPlayer
import com.charlton.pokemon.models.Pokemon
import java.io.Serializable

sealed class NetworkState(open val id: Int): Serializable {

    data class SceneState(
        override val id: Int
    ): NetworkState(id), Serializable

    data class PokemonState(
        override val id: Int,
        val name: String,
        val level: Int,
        val stats: Pokemon.Stat,
        val moves: List<Pokemon.Move>
    ): NetworkState(id), Serializable {
        fun toPokemon(): Pokemon {
            val pokemon = Pokemon(id, name, level)
            pokemon.stats.hp = stats.hp
            pokemon.stats.attack = stats.attack
            pokemon.stats.defense = stats.defense
            pokemon.stats.speed = stats.speed
            pokemon.stats.specialAttack = stats.specialAttack
            pokemon.stats.specialDefense = stats.specialDefense
            moves.forEachIndexed { index, move ->
                pokemon.learnedMoves[index] = move
            }
            return pokemon
        }
    }

    data class PokePlayerState(
        override val id: Int,
        val name: String,
        val sceneState: SceneState,
        val pokemon: List<PokemonState>,
        val selectedPokemon: Int,
        val direction: Direction,
        val pose: ImageObject.Pose,
        val location: Location
    ): NetworkState(id), Serializable {
        fun toPlayer(): ClientEnemyPlayer {
            val enemyPlayer = ClientEnemyPlayer(id, name)
            pokemon.forEach {
                enemyPlayer.addPokemon(it.toPokemon())
            }
            enemyPlayer.currentPose = pose
            enemyPlayer.setX(location.x)
            enemyPlayer.setY(location.y)
            enemyPlayer.setZ(location.z)
            enemyPlayer.direction = direction
            enemyPlayer.setSelectedPokemon(selectedPokemon)
            return enemyPlayer
        }
    }

}
