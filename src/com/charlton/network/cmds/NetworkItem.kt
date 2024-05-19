package com.charlton.network.cmds

import com.charlton.pokemon.scene.battle.Item
import java.io.Serializable

sealed class NetworkItem (open val item: Item, open val message: String): Serializable {
    data class Send(val id: Int, override val item: Item, override val message: String): NetworkItem(item, message),
        Serializable
    data class Recv(val id: Int, override val item: Item, override val message: String): NetworkItem(item, message),
        Serializable
}