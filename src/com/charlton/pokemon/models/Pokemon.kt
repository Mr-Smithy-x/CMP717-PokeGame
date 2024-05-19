package com.charlton.pokemon.models

import com.charlton.gameengine.models.ImageObject
import com.charlton.network.cmds.NetworkItem
import com.charlton.network.models.NetworkState.PokemonState
import com.charlton.pokemon.scene.battle.Item
import java.awt.Color
import java.awt.Graphics
import java.awt.Image
import java.awt.Toolkit
import java.io.Serializable


data class Pokemon(
    val id: Int,
    val name: String,
    private var _level: Int = 5
) : ImageObject(100, 100) {

    fun toSerializable(): PokemonState {
        return PokemonState(id, name, level, stats, learnedMoves.toList())
    }

    fun getBackSprite(): Image {
        if (currentFrame == null)
            currentFrame = Toolkit.getDefaultToolkit().getImage("./assets/sprites/firered/pkmn/back_facing/$id.png")
        return currentFrame!!
    }

    fun getFrontSprite(): Image {
        if (currentFrame == null)
            currentFrame = Toolkit.getDefaultToolkit().getImage("./assets/sprites/firered/pkmn/front_facing/$id.png")
        return currentFrame!!
    }

    val health get() = if (stats.hp < 0) 0 else stats.hp
    val level get() = if (_level > 100) 100 else _level
    val stats = Stat()
    val experience = Experience(points = 0)

    val learnedMoves: Array<Move> = Array(4) { Move() }
    val learnableMoves: Array<Move> = emptyArray()

    init {
        stats.hp = calcHealth(level)
        stats.attack = 0
        stats.specialAttack = 0
        stats.defense = 0
        stats.specialDefense = 0
        stats.speed = 0
        learnedMoves[0] = Move("Tackle", MoveType.NORMAL, 40, true, false)
        learnedMoves[1] = Move("Fury", MoveType.NORMAL, 15, true, false)
        learnedMoves[2] = Move("Take Down", MoveType.NORMAL, 95, true, false)
    }

    private fun calc(level: Int): Int {
        return ((level * (level + 1) * (level + 2)) / 1.65).toInt()
    }

    private fun calcHealth(level: Int): Int {
        return ((level * (level + 3)) / 30) + 20
    }

    override fun render(g: Graphics) {
        super.render(g)
        if (inDebuggingMode()) {
            g.color = Color.GREEN
            g.drawRect(getX() - (getWidth() / 2), getY() - (getHeight() / 2), getWidth(), getHeight())
        }
    }

    override fun toString(): String {
        return "Level: $level\nBase Experience: ${this.experience.baseLevelExp}\nNext Level Exp: ${this.experience.nextLevelExp}\nCurrent Exp: $experience"
    }

    fun gainExperience(exp: Int) {
        this.experience.points += exp
    }

    fun canGoToNextLevel() {
        if (this.experience.truePoints > this.experience.nextLevelExp) {
            experience.points = experience.truePoints - this.experience.nextLevelExp
            _level = (level + 1)
            if (inDebuggingMode()) {
                println("Going to level $level")
            }
            if (_level < 100) {
                canGoToNextLevel()
            }
        }
    }

    fun damage(health: Int) {
        this.stats.hp -= health
    }

    fun heal(health: Int) {
        damage(-health)
    }

    fun Pokemon.setFullHealth() {
        this.stats.hp = this.stats.maxHealth
    }

    fun setLevel(level: Int) {
        this._level = level
    }

    fun consume(recvItem: NetworkItem.Recv): String {
        return consume(recvItem.item)
    }

    fun consume(item: Item): String {
        return when (item.type) {
            Item.ItemType.HP -> {
                heal(item.points)
                "$name healed up by\n${item.points}!"
            }
            Item.ItemType.ATTACK -> {
                stats.attack += item.points
                "$name increased att by\n${item.points}!"
            }
            Item.ItemType.DEFENSE -> {
                stats.defense += item.points
                "$name increased def by\n${item.points}!"
            }
            Item.ItemType.SP_ATTACK -> {
                stats.specialAttack += item.points
                "$name increased sp att by\n${item.points}!"
            }
            Item.ItemType.SP_DEFENSE -> {
                stats.specialDefense += item.points
                "$name increased sp def by\n${item.points}!"
            }
            Item.ItemType.SPEED -> {
                stats.speed += item.points
                "$name increased speed by\n${item.points}!"
            }
        }
    }


    inner class Experience(var points: Int): Serializable {
        val truePoints
            get() = points + baseLevelExp

        val baseLevelExp get() = calc(level % 101)
        val nextLevelExp get() = calc((level + 1) % 101)

        override fun toString(): String {
            return "Points: $points, True Points: $truePoints"
        }
    }

    inner class Stat(
        var hp: Int = 0,
        var attack: Int = 0,
        var defense: Int = 0,
        var specialAttack: Int = 0,
        var specialDefense: Int = 0,
        var speed: Int = 0
    ): Serializable {
        val maxHealth get() = calcHealth(level)

        fun setTempAttack(attack: Int) {

        }

        fun setTempDefense(defense: Int) {

        }

        fun setTempSpAttack(specialAttack: Int) {

        }

        fun setTempSpDefense(specialDefense: Int) {

        }

        fun setTempSpeed(speed: Int) {

        }
    }

    data class Move(
        val name: String = "-",
        val type: MoveType = MoveType.NORMAL,
        val damage: Int = 0,
        val useable: Boolean = false,
        val special: Boolean = false
    ): Serializable

    enum class Effective(
        val strengths: Array<MoveType> = emptyArray(),
        val weaknesses: Array<MoveType> = emptyArray()
    ): Serializable {

        BUG(
            strengths = arrayOf(MoveType.GRASS, MoveType.DARK, MoveType.PSYCHIC),
            weaknesses = arrayOf(MoveType.FIRE, MoveType.FLYING, MoveType.ROCK)
        ),
        DARK(
            strengths = arrayOf(MoveType.PSYCHIC, MoveType.GHOST),
            weaknesses = arrayOf(MoveType.BUG, MoveType.FAIRY, MoveType.FIGHTING)
        ),
        DRAGON(
            strengths = arrayOf(MoveType.DRAGON),
            weaknesses = arrayOf(MoveType.DRAGON, MoveType.FAIRY, MoveType.ICE)
        ),
        ELECTRIC(
            strengths = arrayOf(MoveType.FLYING, MoveType.WATER),
            weaknesses = arrayOf(MoveType.GROUND)
        ),
        FAIRY(
            strengths = arrayOf(MoveType.FIGHTING, MoveType.DARK, MoveType.DRAGON),
            weaknesses = arrayOf(MoveType.POISON, MoveType.STEEL)
        ),
        FIGHTING(
            strengths = arrayOf(MoveType.DARK, MoveType.ICE, MoveType.NORMAL, MoveType.ROCK, MoveType.STEEL),
            weaknesses = arrayOf(MoveType.FAIRY, MoveType.FLYING, MoveType.PSYCHIC)
        ),
        FIRE(
            strengths = arrayOf(MoveType.BUG, MoveType.ICE, MoveType.NORMAL, MoveType.ROCK, MoveType.STEEL),
            weaknesses = arrayOf(MoveType.GROUND, MoveType.ROCK, MoveType.WATER)
        ),
        FLYING(
            strengths = arrayOf(MoveType.BUG, MoveType.FIGHTING, MoveType.GRASS),
            weaknesses = arrayOf(MoveType.ELECTRIC, MoveType.ICE, MoveType.ROCK)
        ),
        GHOST(
            strengths = arrayOf(MoveType.GHOST, MoveType.PSYCHIC),
            weaknesses = arrayOf(MoveType.DARK, MoveType.GHOST)
        ),
        GRASS(
            strengths = arrayOf(MoveType.GROUND, MoveType.ROCK, MoveType.WATER),
            weaknesses = arrayOf(MoveType.BUG, MoveType.FIRE, MoveType.FLYING, MoveType.ICE, MoveType.POISON)
        ),
        GROUND(
            strengths = arrayOf(MoveType.ELECTRIC, MoveType.FIRE, MoveType.POISON, MoveType.ROCK, MoveType.STEEL),
            weaknesses = arrayOf(MoveType.GRASS, MoveType.ICE, MoveType.WATER)
        ),
        ICE(
            strengths = arrayOf(MoveType.DRAGON, MoveType.FLYING, MoveType.GRASS, MoveType.GROUND),
            weaknesses = arrayOf(MoveType.FIGHTING, MoveType.FIRE, MoveType.ROCK, MoveType.STEEL)
        ),
        NORMAL(
            weaknesses = arrayOf(MoveType.FIGHTING)
        ),
        POISON(
            strengths = arrayOf(MoveType.FAIRY, MoveType.GRASS),
            weaknesses = arrayOf(MoveType.GROUND, MoveType.PSYCHIC)
        ),
        PSYCHIC(
            strengths = arrayOf(MoveType.FIGHTING, MoveType.POISON),
            weaknesses = arrayOf(MoveType.BUG, MoveType.DARK, MoveType.GHOST)
        ),
        ROCK(
            strengths = arrayOf(MoveType.BUG, MoveType.FLYING, MoveType.FLYING, MoveType.ICE),
            weaknesses = arrayOf(MoveType.FIGHTING, MoveType.GROUND, MoveType.GROUND, MoveType.STEEL, MoveType.WATER)
        ),
        STEEL(
            strengths = arrayOf(MoveType.FAIRY, MoveType.ICE, MoveType.ROCK),
            weaknesses = arrayOf(MoveType.FIGHTING, MoveType.FIRE, MoveType.GROUND)
        ),
        WATER(
            strengths = arrayOf(MoveType.FIRE, MoveType.GROUND, MoveType.ROCK),
            weaknesses = arrayOf(MoveType.ELECTRIC, MoveType.GRASS)
        ),
    }

    enum class MoveType(val effective: Effective): Serializable {
        DRAGON(Effective.DRAGON),
        ELECTRIC(Effective.ELECTRIC),
        FAIRY(Effective.FAIRY),
        FIRE(Effective.FIRE),
        FLYING(Effective.FLYING),
        GHOST(Effective.GHOST),
        GRASS(Effective.GRASS),
        GROUND(Effective.GROUND),
        ICE(Effective.ICE),
        NORMAL(Effective.NORMAL),
        FIGHTING(Effective.FIGHTING),
        POISON(Effective.POISON),
        PSYCHIC(Effective.PSYCHIC),
        ROCK(Effective.ROCK),
        STEEL(Effective.STEEL),
        WATER(Effective.WATER),
        DARK(Effective.DARK),
        BUG(Effective.BUG);
    }
}

