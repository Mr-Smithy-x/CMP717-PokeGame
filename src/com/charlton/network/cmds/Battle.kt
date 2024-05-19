package com.charlton.network.cmds

import java.io.Serializable

sealed class Battle : Serializable {
    enum class Answer : Serializable {
        YES, NO
    }

    data class SendOffer(val to: Int) : Serializable
    data class RecvOffer(val from: Int) : Serializable
    data class SendAnswer(val to: Int, val answer: Answer) : Serializable
    data class RecvAnswer(val from: Int, val answer: Answer) : Serializable

}