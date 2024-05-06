package com.charlton.network.client

import com.charlton.network.models.NetworkState
import com.charlton.network.contracts.Client
import com.charlton.network.server.PokeClientHandler.CMD
import java.io.Serializable
import java.net.Socket

data class PokeClient(override val socket: Socket): Client(socket), Serializable {

    val id: Int get() = socket.port

    var player: NetworkState.PokePlayerState? = null
        private set

    fun fetchPokeInfo() {
        writeInt(CMD.GET_CLIENT_POKE_INFO.ordinal)
        this.player = readObject()
    }

    fun updatePlayer(info: NetworkState.PokePlayerState) {
        this.player = info
    }

}