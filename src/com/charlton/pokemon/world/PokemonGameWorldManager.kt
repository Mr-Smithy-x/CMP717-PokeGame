package com.charlton.pokemon.world

import com.charlton.gameengine.huds.EnergyHud
import com.charlton.gameengine.huds.HudManager
import com.charlton.gameengine.huds.LifeHud
import com.charlton.gameengine.utils.TransitionManager
import com.charlton.gameengine.world.BaseGameWorldManager
import com.charlton.gameengine.world.scenes.SceneManager
import com.charlton.gameengine.world.scenes.SceneType
import com.charlton.network.client.PokeGameClient
import com.charlton.pokemon.Global
import com.charlton.pokemon.PokemonContainer
import com.charlton.pokemon.models.Pokemon
import com.charlton.pokemon.scene.SandSceneOne
import com.charlton.pokemon.scene.IntroScene
import com.charlton.pokemon.scene.SandSceneTwo
import java.awt.Graphics
import kotlin.random.Random

class PokemonGameWorldManager(container: PokemonContainer, startScene: SceneType = SceneType.Intro) :
    BaseGameWorldManager<PokemonContainer>(container = container),
    SceneManager.Sceneable {

    override val manager: SceneManager = SceneManager()

    init {
        val enemies = PokemonGenerator.generateEnemy(24, 52)
        val n = enemies.size / 2

        HudManager.addHud(LifeHud)
        HudManager.addHud(EnergyHud)

        val random = Random(System.currentTimeMillis())
        val id = random.nextInt(1, 387)
        val level = random.nextInt(35, 45)

        Global.player.addPokemon(Pokemon(id, Global.pokemonNames[id], level))
        Global.player.gravityRate = 0f
        manager.addScene(IntroScene(this))
        manager.addScene(SandSceneOne(this, arrayListOf(*enemies.take(n).toTypedArray())))
        manager.addScene(SandSceneTwo(this, arrayListOf(*enemies.take(n).toTypedArray())))
        manager.setCurrentScene(startScene)
        TransitionManager.tile = manager.getCurrent()
        Global.player.location
        PokeGameClient.connect()
    }

    override fun renderGame(g: Graphics) {
        super.renderGame(g)
        Global.player.render(g)
        //Global.enemy.render(g)
    }

    override fun renderGlobalSounds() {

    }

    override fun renderPause(g: Graphics) {

    }


    override fun manual(keys: BooleanArray, typedKey: BooleanArray) {
        manager.getCurrent()?.manual(keys, typedKey)
    }


    override fun automate() {
        manager.getCurrent()?.automate()
        Global.player.update()
    }

}