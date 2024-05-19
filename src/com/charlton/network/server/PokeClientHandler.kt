package com.charlton.network.server

import com.charlton.mapeditor.model.Network
import com.charlton.network.client.PokeClient
import com.charlton.network.cmds.Battle
import com.charlton.network.cmds.CMD
import com.charlton.network.cmds.NetworkItem
import com.charlton.network.contracts.ClientHandler
import com.charlton.network.models.NetworkState
import com.charlton.network.cmds.Offense

class PokeClientHandler(private val pokeServer: PokeServer): ClientHandler<PokeClient>() {


    override fun handle(client: PokeClient) {
        while (client.isConnected) {
            try {
                val cmd = client.recvCommand()
                when (cmd) {
                    CMD.UPDATE_CLIENT -> {
                        val info: NetworkState.PokePlayerState = client.readObject()
                        pokeServer.update(info)
                    }
                    CMD.SEND_ATTACK -> {
                        val obj: Offense.Send = client.readObject()
                        pokeServer.client[obj.to]?.writeInt(CMD.RECV_ATTACK.ordinal)
                        pokeServer.client[obj.to]?.writeObject(Offense.Recv(client.id, obj.move, obj.damage))
                    }
                    CMD.INITIATE_BATTLE -> {
                        val obj: Battle.SendOffer = client.readObject()
                        pokeServer.client[obj.to]?.writeInt(CMD.INITIATE_BATTLE.ordinal)
                        pokeServer.client[obj.to]?.writeObject(Battle.RecvOffer(client.id))
                    }
                    CMD.SEND_BATTLE_ANSWER -> {
                        val obj: Battle.SendAnswer = client.readObject()
                        pokeServer.client[obj.to]?.writeInt(CMD.RECV_BATTLE_ANSWER.ordinal)
                        pokeServer.client[obj.to]?.writeObject(Battle.RecvAnswer(client.id, obj.answer))
                    }
                    CMD.SEND_ITEM -> {
                        val obj: NetworkItem.Send = client.readObject()
                        pokeServer.client[obj.id]?.writeInt(CMD.RECV_ITEM.ordinal)
                        pokeServer.client[obj.id]?.writeObject(NetworkItem.Recv(client.id, obj.item, obj.message))
                    }
                    CMD.NAN -> {
                        println("Stream disconnected! or closed most likely due to client exiting")
                        break
                    }
                    else -> {
                        println("No command implementation for: $cmd")
                    }
                }
            }catch (e: Exception) {
                println("Disconnected: ${client.id}, ${e.message}")
                break
            }
        }
        pokeServer.disconnected(client)
    }
    override fun authenticate(client: PokeClient): PokeClient {
        val readInt = client.recvCommand()
        if (readInt == CMD.IS_POKE_CLIENT) {
            client.fetchPokeInfo()
            println(client.player?.pokemon)
        } else {
            println("Another client is connect that is not a poke client")
        }
        return client
    }

}
