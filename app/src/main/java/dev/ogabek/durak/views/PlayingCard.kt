package dev.ogabek.durak.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.ogabek.durak.model.Card
import dev.ogabek.durak.model.CardType
import dev.ogabek.durak.ui.theme.DurakTheme

@Composable
fun PlayingCard(firstCard: Card, secondCard: Card? = null) {
    if (secondCard == null) {
        Box(
            modifier = Modifier
                .rotate(-10F)
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            AsyncImage(
                model = firstCard.image(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(70.dp)
                    .height(105.dp)
            )
        }
    } else {
        Box(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .rotate(-10F),
                contentAlignment = Alignment.CenterStart
            ) {
                AsyncImage(
                    model = firstCard.image(),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .width(70.dp)
                        .height(105.dp)
                )
            }

            Box(
                modifier = Modifier
                    .rotate(10F)
                    .padding(top = 10.dp)
                    .padding(start = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                AsyncImage(
                    model = secondCard.image(),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .width(70.dp)
                        .height(105.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayingCardPreview() {
    DurakTheme {
        PlayingCard(
            firstCard = Card(
                code = 11,
                type = CardType.HEARTS
            ),
            secondCard = Card(
                code = 1,
                type = CardType.HEARTS
            )
        )
    }
}