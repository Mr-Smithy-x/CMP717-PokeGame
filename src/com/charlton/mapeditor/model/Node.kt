package com.charlton.mapeditor.model


abstract class Node<N : Node<N>> {
    private var parent: N? = null
    var neighbours: Set<N>? = null

    //endregion
    //region Setters
    var cost: Double = 0.0

    //region Getters
    var heuristic: Double = 0.0
    var function: Double = 0.0

    //endregion
    open var isValid: Boolean = false

    fun getParent(): N? {
        return parent
    }

    //endregion
    //region Abstracts
    abstract fun calculateNearestNodes(network: Network<*>?)

    abstract fun distanceTo(dest: N): Double

    abstract fun discover(dest: N): Double

    fun setParent(parent: N) {
        this.parent = parent
    }

    fun reset() {
        this.cost = 0.0
        this.function = 0.0
        this.parent = null
        this.heuristic = 0.0
    }
}