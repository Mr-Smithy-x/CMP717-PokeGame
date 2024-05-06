package com.charlton.mapeditor.model

interface Network<N : Node<N>> {
    val nodes: Iterable<N>

    fun hasCrossDirection(): Boolean

    fun find(col: Int, row: Int): N?

    fun resetNetwork() {
        for (node in nodes) {
            node.reset()
        }
    }
}