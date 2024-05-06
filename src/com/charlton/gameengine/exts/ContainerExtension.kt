package com.charlton.gameengine.exts

import com.charlton.gameengine.GameContainer
import java.awt.Color
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants


fun GameContainer.Companion.make(title: String?, width: Int, height: Int): JFrame {
    val frame = JFrame(title)
    frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    frame.setLocationRelativeTo(null)
    frame.setSize(width, height)
    frame.layout = null

    val panel = JPanel()
    panel.setBounds(0, 0, width, height)
    panel.background = Color.BLACK
    frame.add(panel)
    frame.isVisible = true
    frame.setSize(width, height)
    panel.setBounds(0,0, width, height)
    panel.isFocusable = true
    panel.focusTraversalKeysEnabled = true
    panel.isFocusable = false
    panel.repaint()
    return frame
}
