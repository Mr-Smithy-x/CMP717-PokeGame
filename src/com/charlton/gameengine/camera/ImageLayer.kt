package com.charlton.gameengine.camera

import com.charlton.gameengine.contracts.Renderable
import java.awt.Graphics
import java.awt.Toolkit

class ImageLayer(var filename: String, var x:Int, var y:Int, var z: Int): Renderable {
    val image = Toolkit.getDefaultToolkit().getImage(filename)


    override fun render(g: Graphics) {
        
    }

}