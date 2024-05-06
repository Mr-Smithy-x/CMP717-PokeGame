package com.charlton.network.server

import com.charlton.network.client.PokeClient
import com.charlton.network.models.NetworkState.PokePlayerState
import java.io.IOException
import java.net.ServerSocket
import java.util.*
import kotlin.collections.HashMap

class PokeServer : Runnable {

    private lateinit var server: ServerSocket

    var client: HashMap<Int, PokeClient> = hashMapOf()
    private var pokeClientHandler = PokeClientHandler(this)
    private var timer = Timer()
    private var automatedTimer = AutomatedTimer()

    inner class AutomatedTimer : TimerTask() {
        override fun run() {
            /*if (client.size > 1) {
                client.forEach { it.value.fetchPokeInfo() }
                client.forEach { c ->
                    client.values.mapNotNull(PokeClient::player).toTypedArray().forEach {
                        if (c.key != it.id) {
                            c.value.writeInt(PokeClientHandler.CMD.UPDATE_CLIENT.ordinal)
                            c.value.writeObject(it)
                        }
                    }
                }
            }*/
        }
    }

    override fun run() {
        //timer.scheduleAtFixedRate(automatedTimer, 500, 500);
        server = ServerSocket(9009)
        while (!server.isClosed) {
            try {
                val socket = server.accept()
                Thread {
                    client[socket.port] = pokeClientHandler.authenticate(PokeClient(socket = socket))
                    println("${socket.port} connected")
                    update(client[socket.port]!!.player!!)
                    pokeClientHandler.handle(client[socket.port]!!)
                }.start()
            } catch (e: IOException) {

            }
        }

    }

    fun disconnected(pokeClient: PokeClient) {
        var id = -1
        client.forEach {
            if (it.value.id != pokeClient.id) {
                it.value.writeInt(PokeClientHandler.CMD.CLIENT_DISCONNECTED.ordinal)
                it.value.writeInt(pokeClient.id)
            } else {
                id = it.key
            }
        }
        client.remove(id)
    }

    fun update(info: PokePlayerState) {
        client[info.id]?.updatePlayer(info)
        client.values.forEach {
            if(it.id != info.id) {
                it.writeInt(PokeClientHandler.CMD.UPDATE_CLIENT.ordinal)
                it.writeObject(info)

                client[info.id]?.writeInt(PokeClientHandler.CMD.UPDATE_CLIENT.ordinal)
                client[info.id]?.writeObject(it.player)
            }
        }
        //automatedTimer.run()
    }

}