package com.charlton.pokemon.extensions

import com.charlton.pokemon.models.Pokemon

operator fun Pokemon.minus(health: Int) = damage(health)
operator fun Pokemon.plus(health: Int) = damage(health)