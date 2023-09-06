package dev.ogabek.durak.utils

import dev.ogabek.durak.model.Card
import dev.ogabek.durak.model.CardType
import java.util.Random

fun IntRange.random() = Random().nextInt((endInclusive + 1) - start) + start