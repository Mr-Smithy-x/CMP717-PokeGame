package com.charlton.gameengine

import com.charlton.gameengine.helpers.FontHelper
import com.charlton.gameengine.helpers.Lookup
import java.awt.*
import java.awt.event.*
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.Timer
import kotlin.math.cos

abstract class GameContainer : Runnable, KeyListener, MouseListener, MouseMotionListener, Debuggable {

    private lateinit var offScreenImage: Image
    private var isRunning: Boolean = true
    private var runGameLoop: Boolean = true
    private lateinit var t: Thread

    protected val logger: Logger = Logger.getLogger("GameContainer", null)
    @JvmField
    protected val pressedKey = BooleanArray(65535)
    protected val typedKey = BooleanArray(65535)
    protected val paused get() = !playing
    protected val font: Font = FontHelper.get("font.ttf", 20)
    protected lateinit var offScreenGraphics: Graphics

    val width: Int get() = container.width
    val height: Int get() = container.height
    val container: Component
    var playing: Boolean = true
    val focused get() = container.isFocusOwner


    protected constructor(frame: JFrame, panel: JComponent) {
        this.container = panel
        this.container.requestFocus()
        this.container.addKeyListener(this)
        this.container.addMouseListener(this)
        this.container.addMouseMotionListener(this)
    }

    fun start() {
        onInitialize()
        onCreateImages()
        t = Thread(this)
        t.start()
    }

    @Throws(IOException::class)
    abstract fun onInitialize()

    private fun onCreateImages() {
        offScreenImage = container.createImage(width, height)
        offScreenGraphics = offScreenImage.graphics
        val rh = RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        rh[RenderingHints.KEY_RENDERING] = RenderingHints.VALUE_RENDER_QUALITY
        (offScreenGraphics as Graphics2D).setRenderingHints(rh)
        offScreenGraphics.font = font
    }


    protected abstract fun onPaint(g: Graphics)
    abstract fun onPlay()
    open fun onStart() = Unit
    open fun onPause() = Unit
    open fun onStop() {
        isRunning = false
        if (this::t.isInitialized) {
            t.interrupt()
        }
    }

    fun onRepaint() {
        update(container.graphics)
    }


    open protected fun onPausePaint(g: Graphics) {
        g.color = Color(0f, 0f, 0f, 0.3f)
        g.fillRect(0, 0, width, height)
        val paused = "Paused"
        drawTextCentered(g, paused)
    }

    protected fun update(g: Graphics) {
        offScreenGraphics.clearRect(0, 0, width, height)
        if (playing) {
            onPaint(offScreenGraphics)
        } else {
            onPaint(offScreenGraphics)
            onPausePaint(offScreenGraphics)
        }
        g.drawImage(offScreenImage, 0, 0, width, height, null)
        g.dispose()
    }


    override fun run() {
        onStart()
        while (isRunning) {
            onRepaint()
            if (runGameLoop) {
                if (playing && focused) onPlay()
                else onPause()
            }
            try {
                Thread.sleep(16) // should result in 60FPS.
            } catch (x: InterruptedException) {
                logger.log(Level.SEVERE, x.message)
                Thread.currentThread().interrupt()
                break
            }
        }
        onStop()
    }

    fun pauseGameLoop(delay: Int) {
        runGameLoop = false
        val pauseTimer = Timer(
            delay
        ) { e: ActionEvent? -> runGameLoop = true }
        pauseTimer.isRepeats = false
        pauseTimer.start()
    }

    fun isPaused(): Boolean {
        return !playing
    }

    fun setPlaying() {
        this.playing = true
    }


    fun drawTextCentered(g: Graphics, text: String) {
        drawTextCenteredOffset(g, text, 0, 0)
    }


    fun drawTextCenteredOffset(g: Graphics, text: String, offsetX: Int, offsetY: Int) {
        val strWidth = g.fontMetrics.stringWidth(text)
        g.drawString(
            text,
            (width / 2) - (strWidth / 2) - offsetX,
            (height / 2) + (g.fontMetrics.height / 2) - offsetY
        )
    }


    fun getMidX(): Int {
        return width / 2
    }

    fun getMidY(): Int {
        return height / 2
    }

    fun getComponentMidX(): Int {
        return container.width / 2
    }

    fun getComponentMidY(): Int {
        return container.height / 2
    }

    override fun keyPressed(e: KeyEvent) {
        pressedKey[e.keyCode] = true
    }


    override fun keyReleased(e: KeyEvent) {
        pressedKey[e.keyCode] = false

        if (e.keyCode == KeyEvent.VK_P) {
            playing = !playing
        }
    }


    override fun keyTyped(e: KeyEvent?) {
        if (e != null) {
            typedKey[e.extendedKeyCode] = true
        }
    }

    override fun mouseClicked(e: MouseEvent?) {

    }

    override fun mousePressed(e: MouseEvent?) {

    }

    override fun mouseReleased(e: MouseEvent?) {

    }

    override fun mouseEntered(e: MouseEvent?) {

    }

    override fun mouseExited(e: MouseEvent?) {

    }

    override fun mouseDragged(e: MouseEvent?) {

    }

    override fun mouseMoved(e: MouseEvent?) {

    }

    companion object {

    }

}