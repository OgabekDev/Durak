package dev.ogabek.durak.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily.Companion.SansSerif
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.ogabek.durak.R
import dev.ogabek.durak.model.Card
import dev.ogabek.durak.model.CardType
import dev.ogabek.durak.ui.theme.DurakTheme
import dev.ogabek.durak.viewmodel.PlayViewModel
import dev.ogabek.durak.views.CardPack
import dev.ogabek.durak.views.MyButton
import dev.ogabek.durak.views.PlayingCard

@Destination
@Composable
fun PlayScreen(
    navController: DestinationsNavigator,
    gameId: String
) {


    val viewModel: PlayViewModel = hiltViewModel()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.bg),
                contentScale = ContentScale.FillBounds
            )
    ) {

        GameView()

    }

}

@Composable
fun GameView() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.bg),
                contentScale = ContentScale.FillBounds
            )
    ) {

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.TopStart)
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                var padding = 0.0
                for (i in 0..20) {
                    Image(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = padding.dp)
                            .size(70.dp, 105.dp),
                        contentScale = ContentScale.FillBounds
                    )
                    padding += 15
                }
            } // Opponent Side

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CardPack(mainCard = Card(code = 14, type = CardType.HEARTS), count = 23)
            } // Card Pack
        } // Top Section

        Box(
            modifier = Modifier
                .padding(top = 50.dp)
                .align(Alignment.Center)
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                columns = GridCells.Fixed(3)
            ) {
                items(6) {
                    PlayingCard(
                        Card(code = 9, type = CardType.CLUBS),
                        Card(code = 13, type = CardType.CLUBS)
                    )
                }
            } // Playing Card
        } // Center Section

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MyButton(text = "I take", height = 35, width = 75, color = Color(144, 64, 58)) {
                    // TODO: I take onClick
                }
                MyButton(text = "I pass", height = 35, width = 75, color = Color(58, 144, 144)) {
                    // TODO: I pass onClick
                }
                MyButton(text = "Bat", height = 35, width = 75, color = Color(66, 52, 155)) {
                    // TODO: Bat onClick
                }
                MyButton(text = "Play", height = 35, width = 100, color = Color(58, 144, 62)) {
                    // TODO: Play onClick
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (i in 0 until 3) {
                        if (i == 2) {
                            item {
                                AsyncImage(
                                    Card(code = 14, type = CardType.DIAMONDS).image(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(70.dp, 105.dp)
                                        .border(
                                            BorderStroke(
                                                0.5.dp,
                                                Color.Black
                                            ),
                                            RectangleShape
                                        )
                                        .clickable {
                                            // TODO: Card onClick
                                        },
                                    contentScale = ContentScale.FillHeight,
                                    alignment = Alignment.CenterStart
                                )
                            }
                        } else {
                            item {
                                AsyncImage(
                                    Card(code = 14, type = CardType.DIAMONDS).image(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(42.dp, 105.dp)
                                        .border(
                                            BorderStroke(
                                                0.5.dp,
                                                Color.Black
                                            ),
                                            RectangleShape
                                        )
                                        .clickable {
                                            // TODO: Card onClick
                                        },
                                    contentScale = ContentScale.FillHeight,
                                    alignment = Alignment.CenterStart
                                )
                            }
                        }
                    }
                }
            }

        }

    }
}

@Preview(
    showBackground = true
)
@Composable
fun PlayScreenPreview() {
    DurakTheme {
        GameView()
    }
}