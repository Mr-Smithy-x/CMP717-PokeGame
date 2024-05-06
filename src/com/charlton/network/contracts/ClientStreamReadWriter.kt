package com.charlton.network.contracts

interface ClientStreamReadWriter {

    fun <T>readObject(): T

    fun readString() = String(recvBytes())

    fun recvInt(): Int

    fun writeString(value: String) = writeBytes(value.toByteArray())

    fun recvBytes(): ByteArray

    fun writeBytes(bytes: ByteArray)

    fun writeInt(int: Int)

    fun <T> writeObject(obj: T)
}