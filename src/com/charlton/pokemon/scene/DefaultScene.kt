package com.charlton.pokemon.scene

import com.charlton.gameengine.exts.to3dCameraF
import com.charlton.gameengine.huds.EnergyHud
import com.charlton.gameengine.huds.HudManager
import com.charlton.gameengine.huds.LifeHud
import com.charlton.gameengine.models.ImageObject
import com.charlton.gameengine.utils.TransitionManager
import com.charlton.gameengine.world.scenes.Scene
import com.charlton.gameengine.world.scenes.SceneManager
import com.charlton.gameengine.world.scenes.SceneType
import com.charlton.mapeditor.model.TileMap
import com.charlton.mapeditor.model.TileMapModel
import com.charlton.network.client.PokeGameClient
import com.charlton.pokemon.Global
import com.charlton.pokemon.models.ClientEnemyPlayer
import com.charlton.pokemon.models.EnemyPlayer
import com.charlton.pokemon.scene.battle.BattleScene
import com.charlton.pokemon.scene.gym.GymScene
import com.charlton.pokemon.sound.GlobalSoundTrack
import com.charlton.pokemon.world.takeRandom
import java.awt.Graphics
import java.awt.event.KeyEvent
import java.io.*
import kotlin.random.Random

open class DefaultScene(sceneable: SceneManager.Sceneable, private val enemies: ArrayList<EnemyPlayer>) :
    Scene(sceneable) {

    override val map: TileMap = loadOverlayMap("assets/tiles/base_map.tilemap")!!

    override val name: SceneType = SceneType.Default
    private var gymEnemies = arrayListOf<EnemyPlayer>()


    private var state: BattleQuestionState = BattleQuestionState.None

    sealed class BattleQuestionState : Serializable {
        data object None : BattleQuestionState(), Serializable
        data class Offer(val from: ClientEnemyPlayer) : BattleQuestionState(), Serializable
    }

    init {
        val random = Random(System.currentTimeMillis())
        gymEnemies = arrayListOf(enemies.takeRandom(), enemies.takeRandom(), enemies.takeRandom())
        sceneable.manager.addScene(GymScene(sceneable, gymEnemies, "01"))
        enemies.forEach {
            it.location.x = random.nextInt(map.getWidth() / 8, map.getWidth() - map.getWidth() / 8).toFloat()
            it.location.y = random.nextInt(map.getHeight() / 8, map.getHeight() - map.getHeight() / 8).toFloat()
        }
    }

    override fun onDebugDraw(g: Graphics) = Unit

    override fun setVisible() {
        HudManager.setVisible(LifeHud)
        HudManager.setVisible(EnergyHud)
    }

    override fun render(g: Graphics) {
        map.render(g)
        Global.player.render3d(g)
        enemies.forEach {
            it.render3d(g)
        }
        PokeGameClient.players.forEach { it.value.render(g) }
    }

    override val mainTrack: GlobalSoundTrack.Track = GlobalSoundTrack.Track.ROUTE119

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

    fun _2DMovement(keys: BooleanArray, typedKey: BooleanArray) {
        if ((keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A])) {
            //Global.player.accelerationX(-1f)
            if (Global.player.location.x > 0 + Global.player.size.width / 4) {
                Global.player.setSpritePoseAndMove(ImageObject.Pose.LEFT, 5f)
            } else if (Global.player.location.x < 0 + Global.player.size.width / 4) {
                Global.player.location.x = Global.player.size.width / 4
            }
        }
        if ((keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D])) {
            if (Global.player.location.x < map.getWidth() - Global.player.size.width) {
                Global.player.setSpritePoseAndMove(ImageObject.Pose.RIGHT, 5f)
            } else if (Global.player.location.x > map.getWidth() - Global.player.size.width) {
                Global.player.location.x = map.getWidth().toFloat() - Global.player.size.width
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
            if (Global.player.location.y < map.getHeight()) {
                Global.player.setSpritePoseAndMove(ImageObject.Pose.DOWN, 5f)
            } else if (Global.player.location.y > map.getHeight()) {
                Global.player.location.y = map.getHeight().toFloat()
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
            println(Global.player.getHeight())
        }
    }

    fun _3DMovement(keys: BooleanArray, typedKey: BooleanArray) {

        fun check() {
            PokeGameClient.players.values.forEach { enemy ->
                initiate(enemy)
            }
        }
        if ((keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A])) {
            //Global.player.accelerationX(-1f)
            if (Global.player.location.x > 0 + Global.player.size.width / 4) {
                Global.player.setSpritePoseAndMove(ImageObject.Pose.LEFT, 5f)
            } else if (Global.player.location.x < 0 + Global.player.size.width / 4) {
                Global.player.location.x = Global.player.size.width / 4
            }
            check()
        }
        if ((keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D])) {
            if (Global.player.location.x < map.getWidth() - Global.player.size.width) {
                Global.player.setSpritePoseAndMove(ImageObject.Pose.RIGHT, 5f)
            } else if (Global.player.location.x > map.getWidth() - Global.player.size.width) {
                Global.player.location.x = map.getWidth().toFloat() - Global.player.size.width
            }
            check()
        }
        if ((keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W])) {
            if (Global.player.location.y > 0) {
                Global.player.setSpritePoseAndMove(ImageObject.Pose.UP, 5f)
            } else if (Global.player.location.y < 0) {
                Global.player.location.y = 0f
            }
            check()
        }
        if ((keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S])) {
            if (Global.player.location.y < map.getHeight()) {
                Global.player.setSpritePoseAndMove(ImageObject.Pose.DOWN, 5f)
            } else if (Global.player.location.y > map.getHeight()) {
                Global.player.location.y = map.getHeight().toFloat()
            }
            check()
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
            println(Global.player.location)
            println(Global.player.size)
            println(Global.player.location.to3dCameraF(Global.player.size))
        }
    }

    override fun manual(keys: BooleanArray, typedKey: BooleanArray) {
        super.manual(keys, typedKey)
        //_2DMovement(keys, typedKey)
        if (state === BattleQuestionState.None) {
            _3DMovement(keys, typedKey)
        } else if (state is BattleQuestionState.Offer) {
            handleOffer((state as BattleQuestionState.Offer).from)
        }
    }

    override fun automate() {
        super.automate()
        //GlobalCamera.setOrigin(Global.player, GlobalCamera.windowWidth, GlobalCamera.windowHeight)
        val tileAtPoint =
            map.getObjectTileAtPoint(Global.player.location.x.toDouble(), Global.player.location.y.toDouble())
        if (tileAtPoint != null) {
            //if (tileAtPoint.isCollisionEnabled == true) {
            //tileAtPoint.collisionDetection(Global.player)

            val tile = sceneable.manager.getScene(SceneType.Gym("01")) ?: return

            TransitionManager.transition = object : TransitionManager.OnTransition {
                override fun onTransition(tile: Scene?) {
                    if (tile is GymScene) {
                        tile.onEnter()
                    }
                    sceneable.manager.setCurrentScene(tile!!.name)
                }
            }

            TransitionManager.setFor(tile)
            //}
        }
        if (inDebuggingMode()) {
            enemies.forEach { enemy ->
                initiate(enemy)
            }
        }
    }

    fun initiate(enemy: EnemyPlayer) {
        if (!Global.player.overlaps(enemy)) return
        if (!enemy.canFight() || enemy is ClientEnemyPlayer) return
        fight(enemy)
    }

    fun initiate(enemy: ClientEnemyPlayer) {
        if (enemy.sceneState.id != name.id) return
        if (!Global.player.overlaps(enemy)) return
        if (!enemy.canFight() || !Global.player.canFight()) return
        PokeGameClient.sendBattleOffer(enemy)
    }

    fun fight(enemy: EnemyPlayer) {
        TransitionManager.transition = object : TransitionManager.OnTransition {
            override fun onTransition(tile: Scene?) {
                if (tile is BattleScene) {
                    tile.resetState()
                }
                sceneable.manager.setCurrentScene(tile!!.name)
            }
        }
        val tile = sceneable.manager.getScene(SceneType.Battle) ?: run {
            val scene = BattleScene.create(sceneable, enemy)
            sceneable.manager.addScene(scene)
            scene
        }
        (tile as BattleScene).initialize(enemy)
        TransitionManager.setFor(tile)
    }

    fun handleOffer(enemy: ClientEnemyPlayer) {
        if (state == BattleQuestionState.None) {
            state = BattleQuestionState.Offer(enemy)
        } else if (state is BattleQuestionState.Offer) {
            state = BattleQuestionState.None
            Dialog.questionWindow("You got an offer to battle from ${enemy.name}(${enemy.id})!", "${enemy.name}(${enemy.id}) wants to battle you with ${
                enemy.pokemon.joinToString(
                    ", "
                ) { it.name + " (${it.level})" }
            }. Do you accept?", {
                PokeGameClient.sendBattleAnswer(enemy, PokeGameClient.Battle.Answer.YES)
                fight(enemy)
            }, {
                PokeGameClient.sendBattleAnswer(enemy, PokeGameClient.Battle.Answer.NO)
            }, {
                PokeGameClient.sendBattleAnswer(enemy, PokeGameClient.Battle.Answer.NO)
            })
        }
    }
}


