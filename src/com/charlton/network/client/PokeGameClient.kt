package com.charlton.network.client

import com.charlton.gameengine.world.scenes.SceneType
import com.charlton.network.models.NetworkState
import com.charlton.network.server.PokeClientHandler
import com.charlton.pokemon.Global
import com.charlton.pokemon.models.ClientEnemyPlayer
import com.charlton.pokemon.models.MainPlayer
import com.charlton.pokemon.scene.DefaultScene
import com.charlton.pokemon.scene.battle.BattleScene
import com.charlton.pokemon.scene.battle.RecvOffense
import com.charlton.pokemon.scene.battle.SendOffense
import com.charlton.pokemon.scene.gym.GymScene
import java.io.Serializable
import java.net.InetSocketAddress
import java.net.Socket

object PokeGameClient {
    var id: Int = 0
    private lateinit var client: PokeClient

    val players: HashMap<Int, ClientEnemyPlayer> = hashMapOf()


    fun connect() = Thread {
        val socket = Socket()
        socket.connect(InetSocketAddress("127.0.0.1", 9009))
        id = socket.localPort
        client = PokeClient(socket)
        if (client.isConnected) {
            client.writeInt(PokeClientHandler.CMD.IS_POKE_CLIENT.ordinal)
            val readInt = client.recvInt()
            if (readInt == PokeClientHandler.CMD.GET_CLIENT_POKE_INFO.ordinal) {
                client.writeObject(Global.player.toMultiPlayer())
                println("Written...")
                while (true) {
                    when (client.recvInt()) {
                        PokeClientHandler.CMD.GET_CLIENT_POKE_INFO.ordinal -> {
                            client.writeObject(Global.player.toMultiPlayer())
                        }

                        PokeClientHandler.CMD.UPDATE_CLIENT.ordinal -> {
                            val info: NetworkState.PokePlayerState = client.readObject()
                            if (!players.containsKey(info.id)) {
                                players[info.id] = info.toPlayer()
                            } else {
                                players[info.id]?.update(info)
                            }
                        }

                        PokeClientHandler.CMD.INITIATE_BATTLE.ordinal -> {
                            val recvOffer: Battle.RecvOffer = client.readObject()
                            if (players.containsKey(recvOffer.from)) {
                                val enemy = players[recvOffer.from]!!
                                val currentScene = Global.game.world.manager.getCurrent()
                                when (currentScene) {
                                    is DefaultScene -> currentScene.handleOffer(enemy)
                                    is GymScene -> currentScene.handlerOffer(recvOffer, enemy)
                                }
                            }
                        }

                        PokeClientHandler.CMD.RECV_BATTLE_ANSWER.ordinal -> {
                            val recvAnswer: Battle.RecvAnswer = client.readObject()
                            if (recvAnswer.answer == Battle.Answer.NO) {
                                //Rejected
                                continue
                            }
                            if (players.containsKey(recvAnswer.from)) {
                                val enemy = players[recvAnswer.from]!!
                                val currentScene = Global.game.world.manager.getCurrent()
                                when (currentScene) {
                                    is DefaultScene -> currentScene.fight(enemy)
                                    is GymScene -> currentScene.fight(enemy)
                                }
                            }
                        }

                        PokeClientHandler.CMD.RECV_ATTACK.ordinal -> {
                            val recvOffense: RecvOffense = client.readObject()
                            update(Global.player)
                            Global.player.currentSelectedPokemon.damage(recvOffense.damage)
                            if (Global.player.currentSelectedPokemon.health <= 0) {
                                (Global.game.world.manager.getScene(SceneType.Battle) as? BattleScene)?.state =
                                    BattleScene.BattleState.LOSE
                            } else {
                                (Global.game.world.manager.getScene(SceneType.Battle) as? BattleScene)?.state =
                                    BattleScene.BattleState.START
                            }
                        }

                        PokeClientHandler.CMD.CLIENT_DISCONNECTED.ordinal -> {
                            players.remove(client.recvInt())
                        }
                    }
                }
            }
        }
    }.start()

    fun update(mainPlayer: MainPlayer) =
        Thread {
            synchronized(client) {
                client.writeInt(PokeClientHandler.CMD.UPDATE_CLIENT.ordinal)
                client.writeObject(mainPlayer.toMultiPlayer())
            }
            Thread.currentThread().interrupt()
        }.start()

    fun sendOffense(sendOffense: SendOffense) = Thread {
        synchronized(client) {
            client.writeInt(PokeClientHandler.CMD.SEND_ATTACK.ordinal)
            client.writeObject(sendOffense)
        }
        Thread.currentThread().interrupt()
    }.start()

    fun sendBattleOffer(enemy: ClientEnemyPlayer) = Thread {
        synchronized(client) {
            client.writeInt(PokeClientHandler.CMD.INITIATE_BATTLE.ordinal)
            client.writeObject(Battle.SendOffer(enemy.id))
        }

        Thread.currentThread().interrupt()
    }.start()

    fun sendBattleAnswer(enemy: ClientEnemyPlayer,answer: Battle.Answer ) = Thread {
        synchronized(client) {
            client.writeInt(PokeClientHandler.CMD.SEND_BATTLE_ANSWER.ordinal)
            client.writeObject(Battle.SendAnswer(enemy.id, answer))
        }

        Thread.currentThread().interrupt()
    }.start()

    sealed class Battle : Serializable {
        enum class Answer : Serializable {
            YES, NO
        }

        data class SendOffer(val to: Int) : Serializable
        data class RecvOffer(val from: Int) : Serializable
        data class SendAnswer(val to: Int, val answer: Answer) : Serializable
        data class RecvAnswer(val from: Int, val answer: Answer) : Serializable

    }


}