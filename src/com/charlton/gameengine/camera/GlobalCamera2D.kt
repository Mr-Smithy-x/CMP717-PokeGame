package com.charlton.gameengine.camera

object GlobalCamera2D: BaseCamera() {

    var windowWidth: Int = 0
        private set
    var windowHeight: Int = 0
        private set

    fun setWindowDimension(windowWidth: Int, windowHeight: Int) {
        this.windowWidth = windowWidth
        this.windowHeight = windowHeight
        this.size.width = windowWidth.toFloat()
        this.size.height = windowHeight.toFloat()
    }

    fun getScaling(): Int {
        return scaling
    }

    fun setScaling(scaling: Int) {
        this.scaling = scaling
    }

    fun moveBy(dx: Int, dy: Int) {
        location.x += dx
        location.y += dy
    }


    fun moveUp(dist: Int) {
        location.y -= dist
    }

    fun moveDown(dist: Int) {
        location.y += dist
    }

    fun moveLeft(dist: Int) {
        location.x -= dist
    }

    fun moveRight(dist: Int) {
        location.x += dist
    }

    fun moveOrigin(xamt: Int, yamt: Int) {
        xOrigin += xamt
        yOrigin += yamt
    }
}

