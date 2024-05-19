package com.charlton.network.cmds

import java.io.Serializable

enum class CMD: Serializable {
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
    RECV_ATTACK,
    SEND_ITEM,
    RECV_ITEM
}