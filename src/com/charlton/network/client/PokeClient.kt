package com.charlton.network.client

import com.charlton.network.models.NetworkState
import com.charlton.network.contracts.Client
import com.charlton.network.cmds.CMD
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

    fun sendCommand(cmd: CMD) = writeInt(cmd.ordinal)


    fun recvCommand(): CMD {
        val recvInt = recvInt()
        if(recvInt < 0) return CMD.NAN
        return CMD.entries[recvInt]
    }


    fun onPlayerDisconnected(player: NetworkState.PokePlayerState) {
        sendCommand(CMD.CLIENT_DISCONNECTED)
        writeObject(player)
    }


}