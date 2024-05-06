package com.charlton

import com.charlton.network.server.PokeServer

fun main() {
    Thread(PokeServer()).start()
}