package com.charlton.gameengine.models.contracts

import kotlin.math.sqrt


interface CollisionObject : Rectable {

    fun collisionDetection(r: CollisionObject) = r.pushOutOf(this)
    // How to detect collision?
    // Check if it overlaps first
    // Next detect if  where it's colliding from...


    fun isOverlapping(box: Rectable): Boolean {
        return ((box.getX() + box.getWidth() >= getX().toDouble()) &&
                (getX().toDouble() + getWidth().toDouble() >= box.getX()) &&
                (box.getY() + box.getHeight() >= getY().toDouble()) &&
                (getY().toDouble() + getHeight().toDouble() >= box.getY()))
    }


    /**
     * Push objects, use this so that we can push objects to the side.
     *
     * @param contract - The object being pushed.
     */
    fun pushOutOf(r: Rectable) {
        if (cameFromLeftOf(r)) {
            pushbackLeftFrom(r)
        }
        if (cameFromRightOf(r)) {
            pushbackRightFrom(r)
        }
        if (cameFromAbove(r)) {
            pushbackUpFrom(r)
        }
        if (cameFromBelow(r)) {
            pushbackDownFrom(r)
        }
    }

    fun cameFromLeftOf(r: Rectable): Boolean {
        return getOldX() + getWidth() > r.getX()
    }

    fun cameFromRightOf(r: Rectable): Boolean {
        return r.getX() + r.getWidth() < getOldX()
    }

    fun cameFromAbove(r: Rectable): Boolean {
        return getOldY() + getHeight() < r.getY()
    }

    fun cameFromBelow(r: Rectable): Boolean {
        return r.getY() + r.getHeight() < getOldY()
    }

    fun pushbackLeftFrom(r: Rectable) {

        setX((r.left - getWidth() - 1).toFloat())
    }

    fun pushbackRightFrom(r: Rectable) {
        setX((r.right + 1).toFloat())
    }

    fun pushbackUpFrom(r: Rectable) {
        setY((r.top - getHeight() - 1).toFloat())
    }

    fun pushbackDownFrom(r: Rectable) {
        setY((r.bottom + 1).toFloat())
    }


    fun pushCircle(contract: Rectable) {

        val dx: Double = getX().toDouble() - contract.getX()
        val dy: Double = getY().toDouble() - contract.getY()
        val d = sqrt(dx * dx + dy * dy)
        val ux = dx / d
        val uy = dy / d
        val ri: Double = getWidth().toDouble() / 2 + contract.getWidth() / 2
        val p = ri - d

        setX(
            (getX().toFloat() + ux * p / 2).toFloat()
        )
        setY(
            (getY().toFloat() + uy * p / 2).toFloat()
        )
        val setPosX: Double = contract.getX() - (ux * p / 2)
        val setPosY: Double = contract.getY() - (uy * p / 2)
        contract.setX(setPosX.toFloat())
        contract.setY(setPosY.toFloat())
    }

    fun willOverlap(r: Rectable, dx: Int, dy: Int): Boolean {
        return !(getX() + dx > r.getDiagonalX() || getY() + dy > r.getDiagonalY()
                || r.getX() > getDiagonalX() + dx || r.getY()
                > getDiagonalY() + dy)
    }


    /**
     * How far is the object from you?
     *
     * @param movable - The object being checked against.
     * @return The distance between objects.
     */
    fun distanceBetween(movable: Rectable): Double {
        val dx: Double = movable.getX() - getX().toDouble()
        val dy: Double = movable.getY() - getY().toDouble()
        return sqrt(dx * dx + dy * dy)
    }

}