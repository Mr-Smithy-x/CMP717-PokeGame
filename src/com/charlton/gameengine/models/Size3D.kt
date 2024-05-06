package com.charlton.gameengine.models

import java.io.Serializable

data class Size3D<T>(var width: T, var height: T, var depth: T): Serializable
