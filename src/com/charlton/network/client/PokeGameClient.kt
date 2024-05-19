package com.charlton.network.client

import com.charlton.gameengine.world.scenes.SceneType
import com.charlton.network.cmds.Battle
import com.charlton.network.cmds.CMD
import com.charlton.network.cmds.NetworkItem
import com.charlton.network.models.NetworkState
import com.charlton.pokemon.Global
import com.charlton.pokemon.models.ClientEnemyPlayer
import com.charlton.pokemon.models.MainPlayer
import com.charlton.pokemon.scene.SandSceneOne
import com.charlton.pokemon.scene.battle.BattleScene
import com.charlton.network.cmds.Offense
import com.charlton.pokemon.scene.battle.BattleState
import com.charlton.pokemon.scene.gym.GymScene
import com.charlton.pokemon.sound.GlobalSoundTrack
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
            client.writeInt(CMD.IS_POKE_CLIENT.ordinal)
            val readInt = client.recvInt()
            if (readInt == CMD.GET_CLIENT_POKE_INFO.ordinal) {
                client.writeObject(Global.player.toMultiPlayer())
                handle()
            }
        }
    }.start()

    private fun handle() {
        while (client.isConnected) {
            val cmd = client.recvCommand()
            val battleScene = Global.game.world.manager.getScene(SceneType.Battle) as? BattleScene
            when (cmd) {
                CMD.GET_CLIENT_POKE_INFO -> {
                    PokeGameClient.client.writeObject(Global.player.toMultiPlayer())
                }
                CMD.UPDATE_CLIENT -> {
                    val info: NetworkState.PokePlayerState = client.readObject()
                    if (!players.containsKey(info.id)) {
                        players[info.id] = info.toPlayer()
                    } else {
                        players[info.id]?.update(info)
                    }
                }
                CMD.INITIATE_BATTLE -> {
                    val recvOffer: Battle.RecvOffer = client.readObject()
                    if (players.containsKey(recvOffer.from)) {
                        val enemy = players[recvOffer.from]!!
                        val currentScene = Global.game.world.manager.getCurrent()
                        when (currentScene) {
                            is SandSceneOne -> currentScene.handleOffer(enemy)
                            is GymScene -> currentScene.handleOffer(enemy)
                        }
                    }
                }
                CMD.RECV_BATTLE_ANSWER -> {
                    val recvAnswer: Battle.RecvAnswer = client.readObject()
                    if (recvAnswer.answer == Battle.Answer.NO) {
                        //Rejected
                        continue
                    }
                    if (players.containsKey(recvAnswer.from)) {
                        val enemy = players[recvAnswer.from]!!
                        val currentScene = Global.game.world.manager.getCurrent()
                        when (currentScene) {
                            is SandSceneOne -> currentScene.fight(enemy)
                            is GymScene -> currentScene.fight(enemy)
                        }
                    }
                }
                CMD.RECV_ATTACK -> {
                    val recvOffense: Offense.Recv = client.readObject()
                    update(Global.player)
                    Global.player.currentSelectedPokemon.damage(recvOffense.damage)
                    if (Global.player.currentSelectedPokemon.health <= 0) {

                        GlobalSoundTrack.setTrack(GlobalSoundTrack.Track.HELP_ME)
                        battleScene?.state =
                            BattleState.LOSE
                    } else {
                        battleScene?.state =
                            BattleState.START
                    }
                }
                CMD.RECV_ITEM -> {
                    val recvItem: NetworkItem.Recv = client.readObject()
                    battleScene?.getEnemy()?.currentSelectedPokemon?.consume(recvItem)
                    battleScene?.state = BattleState.START
                }
                CMD.CLIENT_DISCONNECTED -> {
                    val key: NetworkState.PokePlayerState = client.readObject()
                    players.remove(key.id)
                }
                else -> {
                    println("Unknown Command: $cmd")
                }
            }
        }
    }


    fun update(mainPlayer: MainPlayer) =
        Thread {
            synchronized(client) {
                client.writeInt(CMD.UPDATE_CLIENT.ordinal)
                client.writeObject(mainPlayer.toMultiPlayer())
            }
            Thread.currentThread().interrupt()
        }.start()

    fun sendOffense(sendOffense: Offense.Send) = Thread {
        synchronized(client) {
            client.writeInt(CMD.SEND_ATTACK.ordinal)
            client.writeObject(sendOffense)
        }
        Thread.currentThread().interrupt()
    }.start()

    fun sendBattleOffer(enemy: ClientEnemyPlayer) = Thread {
        synchronized(client) {
            client.writeInt(CMD.INITIATE_BATTLE.ordinal)
            client.writeObject(Battle.SendOffer(enemy.id))
        }

        Thread.currentThread().interrupt()
    }.start()

    fun sendBattleAnswer(enemy: ClientEnemyPlayer,answer: Battle.Answer ) = Thread {
        synchronized(client) {
            client.writeInt(CMD.SEND_BATTLE_ANSWER.ordinal)
            client.writeObject(Battle.SendAnswer(enemy.id, answer))
        }

        Thread.currentThread().interrupt()
    }.start()

    fun sendItem(send: NetworkItem.Send) = Thread {
        synchronized(client) {
            client.writeInt(CMD.SEND_ITEM.ordinal)
            client.writeObject(send)
        }
        Thread.currentThread().interrupt()
    }.start()


}