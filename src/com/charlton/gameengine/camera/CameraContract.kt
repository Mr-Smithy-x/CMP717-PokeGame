package com.charlton.gameengine.camera

import com.charlton.gameengine.models.contracts.Rectable

interface CameraContract : Rectable {
    fun canRender(camera: BaseCamera): Boolean {
        if (getX() - getWidth() < camera.getX() + camera.getWidth() && getX() + getWidth() > camera.getX()) {
            return getY() - getWidth() < camera.getY() + camera.getWidth() && getY() + getWidth() > camera.getY()
        }
        return false
    }

    fun isInsideCamera(camera: BaseCamera): Boolean {
        if (getX() < camera.getX() + camera.getWidth() && getX() > camera.getX()) {
            return getY() < camera.getY() + camera.getHeight() && getY() > camera.getY()
        }
        return false
    }

    fun isOutsideCamera(camera: BaseCamera): Boolean {
        return !isInsideCamera(camera)
    }

    fun getCameraOffsetX(camera: BaseCamera?): Number {
        if (camera == null) {
            return getX()
        }
        return getX() - camera.getX()
    }

    fun getCameraOffsetY(camera: BaseCamera?): Number {
        if (camera == null) {
            return getY()
        }
        return getY() - camera.getY()
    }

    fun getCameraOffsetX2(camera: BaseCamera?): Number {
        if (camera == null) {
            return right
        }
        return right - camera.getX()
    }

    fun getCameraOffsetY2(camera: BaseCamera?): Number {
        if (camera == null) {
            return getY()
        }
        return bottom - camera.getY()
    }
}