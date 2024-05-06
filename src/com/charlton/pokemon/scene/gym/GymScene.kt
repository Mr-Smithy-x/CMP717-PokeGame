package com.charlton.pokemon.scene.gym

import com.charlton.gameengine.models.ImageObject
import com.charlton.gameengine.world.scenes.Scene
import com.charlton.gameengine.world.scenes.SceneManager
import com.charlton.gameengine.world.scenes.SceneType
import com.charlton.mapeditor.model.TileMap
import com.charlton.mapeditor.model.TileMapModel
import com.charlton.network.client.PokeGameClient
import com.charlton.pokemon.Global
import com.charlton.pokemon.models.ClientEnemyPlayer
import com.charlton.pokemon.models.EnemyPlayer
import com.charlton.pokemon.sound.GlobalSoundTrack
import java.awt.Graphics
import java.awt.event.KeyEvent
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.ObjectInputStream

class GymScene(
    sceneable: SceneManager.Sceneable,
    enemies: ArrayList<EnemyPlayer>,
    private val badge: String = "01",
    override val mainTrack: GlobalSoundTrack.Track = GlobalSoundTrack.Track.DEWFORD_TOWN
) : Scene(sceneable) {

    override val map: TileMap = loadOverlayMap("assets/tiles/gym_$badge.tilemap")!!
    override fun onDebugDraw(g: Graphics) {
        //TODO("Not yet implemented")
    }

    fun onEnter() {
        Global.player.location.x = 555.0f
        Global.player.location.y = 992.0f
    }


    override val name: SceneType
        get() = SceneType.Gym(badge)

    override fun render(g: Graphics) {
        map.render(g)
        Global.player.render(g)
        Global.player.render3d(g)

    }

    override fun manual(keys: BooleanArray, typedKey: BooleanArray) {
        if ((keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A])) {
            //Global.player.accelerationX(-1f)
            if (Global.player.location.x > 0 + Global.player.size.width / 4) {
                Global.player.setSpritePoseAndMove(ImageObject.Pose.LEFT, 5f)
            } else if (Global.player.location.x < 0 + Global.player.size.width / 4) {
                Global.player.location.x = Global.player.size.width / 4
            }
        }
        if ((keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D])) {
            if (Global.player.location.x < map.getWidth() - 32) {
                Global.player.setSpritePoseAndMove(ImageObject.Pose.RIGHT, 5f)
            } else if (Global.player.location.x > map.getWidth() - 32) {
                Global.player.location.x = map.getWidth().toFloat() - 32
            }
        }
        if ((keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W])) {
            if (Global.player.location.y > 0) {
                Global.player.setSpritePoseAndMove(ImageObject.Pose.UP, 5f)
            } else if (Global.player.location.y < 0) {
                Global.player.location.y = 0f
            }
        }
        if ((keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S])) {
            if (Global.player.location.y < map.getHeight() - 32) {
                Global.player.setSpritePoseAndMove(ImageObject.Pose.DOWN, 5f)
            } else if (Global.player.location.y > map.getHeight() - 32) {
                Global.player.location.y = map.getHeight().toFloat() - 32
            }
        }
        if ((keys[KeyEvent.VK_Z])) {
            //Global.player.accelerationY(1f)
            //Global.player.moveDown(5f)
            //GlobalCamera.moveIn(1f)
            //Global.player.location.z = minOf(20f,  Global.player.location.z + 0.25f)
        }
        if ((keys[KeyEvent.VK_X])) {
            //player.rotateX(5)
            //GlobalCamera.moveOut(1f)
            //Global.player.location.z = maxOf(1f,  Global.player.location.z - 0.25f)
        }
        if ((keys[KeyEvent.VK_Y])) {
            //player.rotateY(5)
            println(Global.player.location.x)
            println(Global.player.location.y)
        }
    }


    internal fun loadOverlayMap(overlayMapFile: String): TileMap? {
        if (File(overlayMapFile).exists()) {
            try {
                FileInputStream(overlayMapFile).use { fis ->
                    ObjectInputStream(fis).use { ois ->
                        val mapModel = ois.readObject() as TileMapModel
                        val currentMapOverlay = TileMap(mapModel)
                        currentMapOverlay.initializeMap()
                        return currentMapOverlay
                    }
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    override fun automate() {
        super.automate()
        val tileAtPoint = map.getObjectTileAtPoint(Global.player.location.x.toDouble(), Global.player.location.y.toDouble())?:return

        if(Global.player.overlaps(tileAtPoint)){
            tileAtPoint.collisionDetection(Global.player)
            //Global.player.pushOutOf(tileAtPoint)
        }
    }


    fun fight(enemy: ClientEnemyPlayer) {

    }

    fun handlerOffer(recvOffer: PokeGameClient.Battle.RecvOffer, enemy: ClientEnemyPlayer) {

    }
}