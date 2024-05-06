package com.charlton.pokemon.world

import com.charlton.pokemon.Global
import com.charlton.pokemon.models.EnemyPlayer
import com.charlton.pokemon.models.Pokemon
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object PokemonGenerator {

    private fun generate(minLvl: Int = 5, maxLvl: Int = 60): ArrayList<Pokemon> {
        val arr = arrayListOf<Pokemon>()
        val random = Random(System.currentTimeMillis())
        for (i in 0..<100) {
            val id = random.nextInt(1, 387)
            arr.add(
                Pokemon(id, Global.pokemonNames[id - 1], random.nextInt(minLvl, maxLvl+1))
            )
        }
        return arr
    }

    fun generateEnemy(minLvl: Int, maxLvl: Int): ArrayList<EnemyPlayer> {
        val pokemon = generate(minLvl, maxLvl)
        val enemyCount = (pokemon.size / 6) + if(pokemon.size % 6 == 0) 0 else 1
        val enemies = arrayListOf<EnemyPlayer>()
        for (i in 0..<enemyCount) {
            val element = EnemyPlayer("Random Enemy $i")
            for (j in 0..<6 % pokemon.size) {
                element.addPokemon(pokemon.takeRandom())
            }
            enemies.add(element)
        }
        return enemies
    }
}


internal fun <E> ArrayList<E>.takeRandom(): E {
    val randomIndex = Random(System.currentTimeMillis()).nextInt(size)
    val item = this[randomIndex]
    this.remove(item)
    return item
}