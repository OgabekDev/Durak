package dev.ogabek.durak.utils

import androidx.compose.runtime.Composable
import dev.ogabek.durak.model.Card
import dev.ogabek.durak.model.CardType
import kotlinx.coroutines.CoroutineScope
import java.util.Random

fun IntRange.random() = Random().nextInt((endInclusive + 1) - start) + start

fun CoroutineScope.allCardPack(): ArrayList<Card> {
    val cards = ArrayList<Card>()
    for (i in 6..14) {
        cards.add(
            Card(
                code = i,
                type = CardType.DIAMONDS
            )
        )
        cards.add(
            Card(
                code = i,
                type = CardType.SPADES
            )
        )
        cards.add(
            Card(
                code = i,
                type = CardType.CLUBS
            )
        )
        cards.add(
            Card(
                code = i,
                type = CardType.HEARTS
            )
        )
    }

    return cards.shuffled() as ArrayList<Card>
}