package com.charlton.pokemon.scene.battle

import com.charlton.gameengine.huds.EnergyHud
import com.charlton.gameengine.huds.HudManager
import com.charlton.gameengine.huds.LifeHud
import com.charlton.pokemon.models.Pokemon
import com.charlton.gameengine.utils.TransitionManager
import com.charlton.gameengine.world.scenes.Scene
import com.charlton.gameengine.world.scenes.SceneManager
import com.charlton.gameengine.world.scenes.SceneType
import com.charlton.network.client.PokeGameClient
import com.charlton.pokemon.Global
import com.charlton.pokemon.models.ClientEnemyPlayer
import com.charlton.pokemon.models.EnemyPlayer
import com.charlton.pokemon.scene.battle.components.*
import com.charlton.pokemon.sound.GlobalSoundEffect
import com.charlton.pokemon.sound.GlobalSoundTrack
import java.awt.Graphics
import java.awt.event.KeyEvent
import java.io.Serializable

data class SendOffense(val to: Int, val move: Pokemon.Move, val damage: Int = 0): Serializable
data class RecvOffense(val from: Int, val move: Pokemon.Move, val damage: Int = 0): Serializable

class BattleScene private constructor(sceneable: SceneManager.Sceneable, private val enemy: EnemyPlayer) : Scene(sceneable) {

    val isClient get() = enemy is ClientEnemyPlayer

    companion object {
        fun create(sceneable: SceneManager.Sceneable, enemy: EnemyPlayer): BattleScene {
            return BattleScene(sceneable, enemy)
        }
    }

    fun resetState() {
        state = BattleState.START
        box.setText("What should\n${Global.player.name} do?")
    }

    fun initialize(enemy: EnemyPlayer) {
        enemyLifeHudComponent = EnemyPokemonHudComponent(enemy)
        enemyPkmnSprite = EnemyPokemonComponent(enemy)
        myPokemonHudComponent = MyPokemonHudComponent(Global.player.currentSelectedPokemon)
        pkmnSprite = PokemonComponent(Global.player.currentSelectedPokemon)
        attackOptions = AttackOptionComponent(Global.player.currentSelectedPokemon)
    }

    private var enemyLifeHudComponent = EnemyPokemonHudComponent(enemy)
    private var enemyPkmnSprite = EnemyPokemonComponent(enemy)
    private var myPokemonHudComponent = MyPokemonHudComponent(Global.player.currentSelectedPokemon)
    private var pkmnSprite = PokemonComponent(Global.player.currentSelectedPokemon)
    private var attackOptions = AttackOptionComponent(Global.player.currentSelectedPokemon)

    private val box = BattleBoxMessage()
    private val options = BattleBoxOption()
    private val inventoryComponent = InventoryComponent()
    private val pkmnSelectionComponent = InventoryComponent()
    var state: BattleState = BattleState.START

    override val name = SceneType.Battle

    init {
        initialize(enemy)
        resetState()
    }

    override fun onDebugDraw(g: Graphics) {

    }

    private fun handleBattleStateStart(keys: BooleanArray, typedKey: BooleanArray): BattleState? {
        if ((keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A])) {

            options.setSelection(options.rowSelection, options.columnSelection - 1)
        }
        if ((keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D])) {

            options.setSelection(options.rowSelection, options.columnSelection + 1)
        }

        if ((keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W])) {

            options.setSelection(options.rowSelection - 1, options.columnSelection)
        }
        if ((keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S])) {

            options.setSelection(options.rowSelection + 1, options.columnSelection)
        }

        if (keys[KeyEvent.VK_ENTER] && typedKey[KeyEvent.VK_ENTER]) {

            val selection = options.getSelection()
            return BattleState.Choice(selection.text, 0)
        }
        return null
    }

    private fun handleBattleOptionFight(keys: BooleanArray, typedKey: BooleanArray): BattleState? {

        if ((keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A])) {

            attackOptions.setSelection(attackOptions.rowSelection, attackOptions.columnSelection - 1)
        }
        if ((keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D])) {

            attackOptions.setSelection(attackOptions.rowSelection, attackOptions.columnSelection + 1)
        }

        if ((keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W])) {

            attackOptions.setSelection(attackOptions.rowSelection - 1, attackOptions.columnSelection)
        }
        if ((keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S])) {

            attackOptions.setSelection(attackOptions.rowSelection + 1, attackOptions.columnSelection)
        }

        if ((keys[KeyEvent.VK_ESCAPE] || keys[KeyEvent.VK_DELETE])) {

            return BattleState.START
        }

        if (keys[KeyEvent.VK_ENTER] && typedKey[KeyEvent.VK_ENTER]) {

            val selection: Pokemon.Move = attackOptions.getSelection()
            box.setText("${myPokemonHudComponent.pokemon.name} used ${attackOptions.getSelection().name}!")
            return BattleState.Attack(selection)
        }
        return null
    }

    private fun handleBattleStateChoice(keys: BooleanArray, typedKey: BooleanArray): BattleState? {
        val choice = state as BattleState.Choice
        val state = when (choice.option) {
            BattleBoxOption.BattleOption.FIGHT -> handleBattleOptionFight(keys, typedKey)
            BattleBoxOption.BattleOption.BAG -> null
            BattleBoxOption.BattleOption.POKEMON -> null
            BattleBoxOption.BattleOption.RUN -> {
                if (keys[KeyEvent.VK_ENTER] && typedKey[KeyEvent.VK_ENTER]) {
                    TransitionManager.trigger()
                    BattleState.END
                } else null
            }
        }
        return state
    }

    private fun handleAttackAfterMath(keys: BooleanArray, typedKey: BooleanArray): BattleState {
        Thread.sleep(1000)
        GlobalSoundEffect.play(GlobalSoundEffect.SOUND.ATTACK_NORMAL)
        val move = attackOptions.getSelection()
        val healthDamage = move.damage / 2
        enemyLifeHudComponent.pokemon.damage(healthDamage)
        Thread.sleep(1000)

        if(isClient) {
            PokeGameClient.sendOffense(SendOffense((enemy as ClientEnemyPlayer).id, move, damage = healthDamage))
        }

        if (enemyLifeHudComponent.pokemon.health <= 0) {
            box.setText("${enemyLifeHudComponent.pokemon.name} Fainted!")
            Thread.sleep(1000)
            if(enemy.canFight()) {
                box.setText("${enemy.name} called out\n${myPokemonHudComponent.pokemon.name} ")
                Thread.sleep(1000)
                //box.setText("What should\n${myPokemonHudComponent.pokemon.name} do?")
                box.setText("Waiting for players turn")
                return BattleState.AWAIT
            } else {
                GlobalSoundTrack.setTrack(GlobalSoundTrack.Track.WILD_VICTORY)
                box.setText("${myPokemonHudComponent.pokemon.name} gained 234 exp!")
                Thread.sleep(1000)
                return BattleState.WIN
            }
        } else {
            //box.setText("What should\n${myPokemonHudComponent.pokemon.name} do?")
            //return BattleState.START
            box.setText("Waiting for players turn...")
            return BattleState.AWAIT
        }
    }

    override fun manual(keys: BooleanArray, typedKey: BooleanArray) {
        val state = when (state) {
            BattleState.START -> handleBattleStateStart(keys, typedKey)
            BattleState.WIN -> {
                if (keys[KeyEvent.VK_ENTER] && typedKey[KeyEvent.VK_ENTER]) {
                    TransitionManager.trigger()
                    BattleState.END
                } else null
            }
            BattleState.LOSE -> if (keys[KeyEvent.VK_ENTER] && typedKey[KeyEvent.VK_ENTER]) {
                TransitionManager.trigger()
                BattleState.END
            } else null
            BattleState.AWAIT -> handleBattleStateAwait(keys, typedKey)
            BattleState.END -> null
            is BattleState.Attack -> handleAttackAfterMath(keys, typedKey)
            is BattleState.Choice -> handleBattleStateChoice(keys, typedKey)
            is BattleState.Bag -> null
        }
        if (state != null) {
            this.state = state
        }
    }

    private fun handleBattleStateAwait(keys: BooleanArray, typedKey: BooleanArray): BattleState {
        if(isClient) {
            return BattleState.AWAIT
        }

        if (keys[KeyEvent.VK_ENTER] && typedKey[KeyEvent.VK_ENTER]) {
            box.setText("${enemy.currentSelectedPokemon.name} is setting up\nfor an attack!")
            Thread.sleep(1000)
            val move = enemy.currentSelectedPokemon.learnedMoves.random()
            box.setText("${enemy.currentSelectedPokemon.name} used\n${move.name}")
            Thread.sleep(2000)
            Global.player.currentSelectedPokemon.damage(move.damage/2)
            Thread.sleep(1000)
            return BattleState.START
        }

        return BattleState.AWAIT
    }
    override fun automate() {
        //GlobalSoundTrack.play()
    }


    override fun setHidden() {
        HudManager.setHidden(LifeHud)
        HudManager.setHidden(EnergyHud)
    }

    override fun setVisible() {

    }


    override fun render(g: Graphics) {
        pkmnSprite.render(g)
        enemyPkmnSprite.render(g)
        box.render(g)
        if(myPokemonHudComponent.pokemon.health <= 0) {
            //state = BattleState.LOSE
        }
        when (state) {
            BattleState.AWAIT -> {

            }
            BattleState.START -> {

                box.setText("What should\n${myPokemonHudComponent.pokemon.name} do?")
                options.render(g)
            }

            BattleState.WIN -> {

            }

            BattleState.LOSE -> {
                box.setText("You Lost!")
            }

            is BattleState.Attack -> {

            }

            is BattleState.Bag -> {

            }

            is BattleState.Choice -> {
                when ((state as BattleState.Choice).option) {
                    BattleBoxOption.BattleOption.FIGHT -> {
                        attackOptions.render(g)
                    }

                    BattleBoxOption.BattleOption.BAG -> {
                        inventoryComponent.render(g)
                    }

                    BattleBoxOption.BattleOption.POKEMON -> {
                        pkmnSelectionComponent.render(g)
                    }

                    BattleBoxOption.BattleOption.RUN -> {
                        box.setText("${Global.player.name} ran away safely!")
                    }
                }
            }

            BattleState.END -> {
                TransitionManager.transition = object : TransitionManager.OnTransition {
                    override fun onTransition(tile: Scene?) {
                        sceneable.manager.setCurrentScene(tile!!.name)
                    }
                }
                TransitionManager.setFor(sceneable.manager.getScene(SceneType.Default))
            }
        }
        enemyLifeHudComponent.render(g)
        myPokemonHudComponent.render(g)
    }

    override val mainTrack: GlobalSoundTrack.Track = GlobalSoundTrack.Track.WILD_BATTLE

    sealed class BattleState {
        data object START : BattleState()
        data object END : BattleState()
        data object WIN : BattleState()
        data object LOSE : BattleState()
        data object AWAIT : BattleState()

        data class Choice(val option: BattleBoxOption.BattleOption, val index: Int) : BattleState()
        data class Attack(var move: Pokemon.Move) : BattleState()
        data class Bag(var item: Item) : BattleState()
    }

    data class Item(
        val name: String = "Potion",
        val type: ItemType = ItemType.HP,
        val points: Int = 20,
        var quantity: Int = 1
    ) {

        fun use(pokemon: Pokemon): Boolean {
            if (quantity > 0) {
                quantity--
                return when (type) {
                    ItemType.HP -> {
                        pokemon.heal(points)
                        true
                    }

                    ItemType.ATTACK -> {
                        pokemon.stats.setTempAttack(points)
                        true
                    }

                    ItemType.DEFENSE -> {
                        pokemon.stats.setTempDefense(points)
                        true
                    }

                    ItemType.SP_ATTACK -> {
                        pokemon.stats.setTempSpAttack(points)
                        true
                    }

                    ItemType.SP_DEFENSE -> {
                        pokemon.stats.setTempSpDefense(points)
                        true
                    }

                    ItemType.SPEED -> {
                        pokemon.stats.setTempSpeed(points)
                        true
                    }
                }
            }
            return false
        }

        enum class ItemType {
            HP,
            ATTACK,
            DEFENSE,
            SP_ATTACK,
            SP_DEFENSE,
            SPEED
        }

    }

}