package com.charlton.network.contracts

abstract class ClientHandler<T: ClientStreamReadWriter> {

    internal abstract fun handle(client: T)

    internal abstract fun authenticate(client: T): T

}