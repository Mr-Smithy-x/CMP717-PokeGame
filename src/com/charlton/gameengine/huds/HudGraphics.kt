package com.charlton.gameengine.huds

import java.awt.*
import java.awt.image.ImageObserver
import java.text.AttributedCharacterIterator

class HudGraphics(val offsetX: Int, val offsetY: Int, private val graphics: Graphics) : Graphics() {

    override fun create(): Graphics = HudGraphics(offsetX, offsetY, graphics.create())

    override fun translate(x: Int, y: Int) = graphics.translate(x+offsetX, y+offsetY)

    override fun getColor(): Color = graphics.color

    override fun setColor(c: Color?) {
        graphics.color = c
    }

    override fun setPaintMode() = graphics.setPaintMode()

    override fun setXORMode(c1: Color?) {
        graphics.setXORMode(c1)
    }

    override fun getFont(): Font = graphics.font

    override fun setFont(font: Font?) {
        graphics.font = font
    }

    override fun getFontMetrics(f: Font?): FontMetrics = graphics.getFontMetrics(f)

    override fun getClipBounds(): Rectangle = graphics.clipBounds

    override fun clipRect(x: Int, y: Int, width: Int, height: Int) = graphics.clipRect(x+offsetX, y+offsetY, width, height)

    override fun setClip(x: Int, y: Int, width: Int, height: Int) = graphics.setClip(x+offsetX, y+offsetY, width, height)

    override fun setClip(clip: Shape?) {
        graphics.clip = clip
    }

    override fun getClip(): Shape = graphics.clip

    override fun copyArea(x: Int, y: Int, width: Int, height: Int, dx: Int, dy: Int) =
        graphics.copyArea(x+offsetX, y+offsetY, width, height, dx, dy)

    override fun drawLine(x1: Int, y1: Int, x2: Int, y2: Int) = graphics.drawLine(x1, y1, x2, y2)

    override fun fillRect(x: Int, y: Int, width: Int, height: Int) = graphics.fillRect(x+offsetX, y+offsetY, width, height)

    override fun clearRect(x: Int, y: Int, width: Int, height: Int) = graphics.clearRect(x, y, width, height)

    override fun drawRect(x: Int, y: Int, width: Int, height: Int)  = super.drawRect(x+offsetX, y+offsetY, width, height)



    override fun drawRoundRect(x: Int, y: Int, width: Int, height: Int, arcWidth: Int, arcHeight: Int) =
        graphics.drawRoundRect(x+offsetX, y+offsetY, width, height, arcWidth, arcHeight)

    override fun fillRoundRect(x: Int, y: Int, width: Int, height: Int, arcWidth: Int, arcHeight: Int) =
        graphics.fillRoundRect(x+offsetX, y+offsetY, width, height, arcWidth, arcHeight)

    override fun drawOval(x: Int, y: Int, width: Int, height: Int) = graphics.drawOval(x+offsetX, y+offsetY, width, height)

    override fun fillOval(x: Int, y: Int, width: Int, height: Int) = graphics.fillOval(x+offsetX, y+offsetY, width, height)

    override fun drawArc(x: Int, y: Int, width: Int, height: Int, startAngle: Int, arcAngle: Int) =
        graphics.drawArc(x+offsetX, y+offsetY, width, height, startAngle, arcAngle)

    override fun fillArc(x: Int, y: Int, width: Int, height: Int, startAngle: Int, arcAngle: Int) =
        graphics.fillArc(x+offsetX, y+offsetY, width, height, startAngle, arcAngle)

    override fun drawPolyline(xPoints: IntArray?, yPoints: IntArray?, nPoints: Int) =
        graphics.drawPolyline(xPoints?.map { it + offsetX }?.toIntArray(), yPoints?.map { it + offsetY }?.toIntArray(), nPoints)

    override fun drawPolygon(xPoints: IntArray?, yPoints: IntArray?, nPoints: Int) =
        graphics.drawPolyline(xPoints?.map { it + offsetX }?.toIntArray(), yPoints?.map { it + offsetY }?.toIntArray(), nPoints)

    override fun fillPolygon(xPoints: IntArray?, yPoints: IntArray?, nPoints: Int) =
        graphics.fillPolygon(xPoints?.map { it + offsetX }?.toIntArray(), yPoints?.map { it + offsetY }?.toIntArray(), nPoints)

    override fun drawString(str: String, x: Int, y: Int) = graphics.drawString(str, x+offsetX, y+offsetY)

    override fun drawString(iterator: AttributedCharacterIterator?, x: Int, y: Int) =
        graphics.drawString(iterator, x+offsetX, y+offsetY)

    override fun drawImage(img: Image?, x: Int, y: Int, observer: ImageObserver?): Boolean =
        graphics.drawImage(img, x+offsetX, y+offsetY, observer)

    override fun drawImage(img: Image?, x: Int, y: Int, width: Int, height: Int, observer: ImageObserver?): Boolean =
        graphics.drawImage(img, x+offsetX, y+offsetY, width, height, observer)

    override fun drawImage(img: Image?, x: Int, y: Int, bgcolor: Color?, observer: ImageObserver?): Boolean =
        graphics.drawImage(img, x+offsetX, y+offsetY, bgcolor, observer)

    override fun drawImage(
        img: Image?,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        bgcolor: Color?,
        observer: ImageObserver?
    ): Boolean = graphics.drawImage(img, x+offsetX, y+offsetY, width, height, bgcolor, observer)

    override fun drawImage(
        img: Image?,
        dx1: Int,
        dy1: Int,
        dx2: Int,
        dy2: Int,
        sx1: Int,
        sy1: Int,
        sx2: Int,
        sy2: Int,
        observer: ImageObserver?
    ): Boolean = graphics.drawImage(img, dx1+offsetX, dy1+offsetY, dx2+offsetX,dy2+offsetY, sx1+offsetX, sy1+offsetY, sx2+offsetX, sy2+offsetY, observer)

    override fun drawImage(
        img: Image?,
        dx1: Int,
        dy1: Int,
        dx2: Int,
        dy2: Int,
        sx1: Int,
        sy1: Int,
        sx2: Int,
        sy2: Int,
        bgcolor: Color?,
        observer: ImageObserver?
    ): Boolean = graphics.drawImage(img, dx1+offsetX, dy1+offsetY, dx2+offsetX,dy2+offsetY, sx1+offsetX, sy1+offsetY, sx2+offsetX, sy2+offsetY, observer)

    override fun dispose() = graphics.dispose()

}