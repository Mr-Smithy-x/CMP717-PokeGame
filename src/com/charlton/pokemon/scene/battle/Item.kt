package com.charlton.pokemon.scene.battle

import com.charlton.pokemon.models.Pokemon
import java.io.Serializable

data class Item(
    val name: String = "Potion",
    val type: ItemType = ItemType.HP,
    val points: Int = 20,
    var quantity: Int = 1
): Serializable {

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

    enum class ItemType: Serializable {
        HP,
        ATTACK,
        DEFENSE,
        SP_ATTACK,
        SP_DEFENSE,
        SPEED
    }

}