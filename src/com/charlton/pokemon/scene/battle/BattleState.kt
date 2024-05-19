package com.charlton.pokemon.scene.battle

import com.charlton.pokemon.models.Pokemon
import com.charlton.pokemon.scene.battle.components.BattleBoxOption

sealed class BattleState {
    data object START : BattleState()
    data object END : BattleState()
    data object WIN : BattleState()
    data object LOSE : BattleState()
    data class AWAIT(val state: BattleStateEnum = BattleStateEnum.NONE) : BattleState()

    data class Choice(val option: BattleBoxOption.BattleOption, val index: Int) : BattleState()
    data class Attack(var move: Pokemon.Move) : BattleState()
    data class Bag(var item: Item) : BattleState()
}

enum class BattleStateEnum {
    NONE,
    DEFEATED
}