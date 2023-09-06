package dev.ogabek.durak.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import dev.ogabek.durak.R
import dev.ogabek.durak.model.Card
import dev.ogabek.durak.model.CardType
import dev.ogabek.durak.ui.theme.DurakTheme

@Composable
fun CardPack(mainCard: Card, count: Int) {

    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(105.dp)
                .width(75.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            var padding = 0.0
            for (i in 0..count) {
                Image(
                        painter = painterResource(id = R.drawable.back),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = padding.dp)
                    .size(70.dp, 105.dp),
                contentScale = ContentScale.FillBounds
                )
                padding += 0.2
            }
        }

        Box(
            modifier = Modifier
                .height(70.dp)
                .width(70.dp)
                .rotate(90F)
        ) {
            AsyncImage(
                model = mainCard.image(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                alignment = Alignment.TopStart,
                contentScale = ContentScale.FillWidth,

            )
        }

    }

}

@Preview(showBackground = true)
@Composable
fun CardPackPreview() {
    DurakTheme {
        CardPack(
            mainCard = Card(
                code = 11,
                type = CardType.HEARTS
            ),
            count = 35
        )
    }
}