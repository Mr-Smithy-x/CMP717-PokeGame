package com.charlton.network.contracts

import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.net.Socket

abstract class Client(protected open val socket: Socket): ClientStreamReadWriter {

    val isConnected get() = socket.isConnected && !socket.isInputShutdown

    val isClosed get() = socket.isClosed

    private val inputStream: InputStream get() = socket.getInputStream()

    private val outputStream: OutputStream get() = socket.getOutputStream()

    override fun recvBytes(): ByteArray {
        val readAllBytes = inputStream.readBytes()
        println("read: ${String(readAllBytes)}")
        return readAllBytes
    }

    override fun writeBytes(bytes: ByteArray) {
        println("writing ${String(bytes)}")
        outputStream.write(bytes)
        outputStream.flush()
    }

    override fun <T> readObject(): T {
        val str = ObjectInputStream(inputStream)
        return str.readObject() as T
    }


    override fun recvInt(): Int {
        return inputStream.read()
    }

    override fun writeInt(int: Int) {
        return outputStream.write(int)
    }


    override fun<T> writeObject(obj: T) {
        val os = ObjectOutputStream(outputStream)
        os.writeObject(obj)
        os.flush()
    }

    fun close(){
        try {
            socket.close()
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

}