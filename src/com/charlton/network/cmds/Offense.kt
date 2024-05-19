package com.charlton.network.cmds

import com.charlton.pokemon.models.Pokemon
import java.io.Serializable

sealed class Offense (open val move: Pokemon.Move, open val damage: Int = 0): Serializable {
    data class Send(val to: Int, override val move: Pokemon.Move, override val damage: Int = 0): Offense(move, damage),
        Serializable
    data class Recv(val from: Int, override val move: Pokemon.Move, override val damage: Int = 0): Offense(move, damage),
        Serializable
}