package com.charlton.network.server

import com.charlton.network.client.PokeClient
import com.charlton.network.client.PokeGameClient
import com.charlton.network.contracts.ClientHandler
import com.charlton.network.models.NetworkState
import com.charlton.pokemon.scene.battle.RecvOffense
import com.charlton.pokemon.scene.battle.SendOffense

class PokeClientHandler(private val pokeServer: PokeServer): ClientHandler<PokeClient>() {

    enum class CMD {
        NAN,
        IS_POKE_CLIENT,
        NOT_POKE_CLIENT,
        GET_CLIENT_POKE_INFO,
        UPDATE_CLIENT,
        CLIENT_DISCONNECTED,
        INITIATE_BATTLE,
        SEND_BATTLE_ANSWER,
        RECV_BATTLE_ANSWER,
        SEND_ATTACK,
        RECV_ATTACK
    }


    override fun handle(client: PokeClient) {
        while (client.isConnected) {
            when(client.recvInt()) {
                CMD.UPDATE_CLIENT.ordinal ->  {
                    val info: NetworkState.PokePlayerState = client.readObject()
                    pokeServer.update(info)
                }
                CMD.SEND_ATTACK.ordinal -> {
                    val obj: SendOffense = client.readObject()
                    pokeServer.client[obj.to]?.writeInt(CMD.RECV_ATTACK.ordinal)
                    pokeServer.client[obj.to]?.writeObject(RecvOffense(client.id, obj.move, obj.damage))
                }
                CMD.INITIATE_BATTLE.ordinal -> {
                    val obj: PokeGameClient.Battle.SendOffer = client.readObject()
                    pokeServer.client[obj.to]?.writeInt(CMD.INITIATE_BATTLE.ordinal)
                    pokeServer.client[obj.to]?.writeObject(PokeGameClient.Battle.RecvOffer(client.id))
                }
                CMD.SEND_BATTLE_ANSWER.ordinal -> {
                    val obj: PokeGameClient.Battle.SendAnswer = client.readObject()
                    pokeServer.client[obj.to]?.writeInt(CMD.RECV_BATTLE_ANSWER.ordinal)
                    pokeServer.client[obj.to]?.writeObject(PokeGameClient.Battle.RecvAnswer(client.id, obj.answer))
                }
            }
        }
        pokeServer.disconnected(client)
    }
    override fun authenticate(client: PokeClient): PokeClient {
        val readInt = client.recvInt()
        if (readInt == CMD.IS_POKE_CLIENT.ordinal) {
            client.fetchPokeInfo()
            println(client.player?.pokemon)
        } else {
            println("Another client is connect that is not a poke client")
        }
        return client
    }

}
