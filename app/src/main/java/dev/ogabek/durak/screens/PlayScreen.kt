package dev.ogabek.durak.screens

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import dev.ogabek.durak.utils.check
import dev.ogabek.durak.viewmodel.PlayViewModel
import dev.ogabek.durak.views.CardPack
import dev.ogabek.durak.views.LoadingView
import dev.ogabek.durak.views.MyButton
import dev.ogabek.durak.views.PlayingCard

@Destination
@Composable
fun PlayScreen(
    navController: DestinationsNavigator,
    gameId: String,
    isNew: Boolean,
    viewModel: PlayViewModel = hiltViewModel()
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.bg),
                contentScale = ContentScale.FillBounds
            )
    ) {

        if (viewModel.isWaiting.value) {
            LoadingView(
                title = "Please wait your opponent",
                subTitle = "Invite via code: $gameId"
            )
        } else {
            GameView(
                gameId = gameId,
                viewModel = viewModel,
                isNew = isNew
            )
        }

        if (viewModel.error.value) {
            Toast.makeText(LocalContext.current, viewModel.errorMessage.value, Toast.LENGTH_SHORT)
                .show()
            viewModel.closeError()
        }

        LaunchedEffect(isNew) {
            viewModel.setDatabase(gameId, isNew)
            viewModel.loadGame(gameId, isNew)
        }

    }

}

@Composable
fun GameView(
    gameId: String,
    viewModel: PlayViewModel,
    isNew: Boolean
) {

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
                    .padding(16.dp)
                    .height(110.dp),
                contentAlignment = Alignment.TopStart
            ) {
                var padding = 0.0
                for (i in 0 until isNew.check(
                    viewModel.secondPlayer.size,
                    viewModel.firstPlayer.size
                )) {
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
                CardPack(
                    mainCard = viewModel.mainCard.value,
                    count = viewModel.cards.size
                )
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
                items(viewModel.onBoardCard.size) {
                    PlayingCard(
                        viewModel.onBoardCard[it]
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
                if (viewModel.isTake.value) {
                    MyButton(text = "I take", height = 35, width = 75, color = Color(144, 64, 58)) {
                        // TODO: I take onClick
                        viewModel.iTake(gameId)
                    }
                }
                if (viewModel.isPass.value) {
                    MyButton(text = "I pass", height = 35, width = 75, color = Color(58, 144, 144)) {
                        // TODO: I pass onClick
                        viewModel.iPass(gameId)
                    }
                }
                if (viewModel.isBat.value) {
                    MyButton(text = "Bat", height = 35, width = 75, color = Color(66, 52, 155)) {
                        // TODO: Bat onClick
                        viewModel.iBat(gameId)
                    }
                }
                if (isNew && !viewModel.isWaiting.value && !viewModel.isPlaying.value) {
                    MyButton(text = "Play", height = 35, width = 100, color = Color(58, 144, 62)) {
                        // TODO: Play onClick
                        viewModel.startGame(gameId)
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (i in 0 until isNew.check(
                        viewModel.firstPlayer.size,
                        viewModel.secondPlayer.size
                    )) {
                        val size = if (i == isNew.check(
                                viewModel.firstPlayer.size,
                                viewModel.secondPlayer.size
                            ) - 1
                        ) 70.dp else 42.dp
                        item {
                            AsyncImage(
                                isNew.check(
                                    viewModel.firstPlayer,
                                    viewModel.secondPlayer
                                )[i].image(),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(size, 105.dp)
                                    .border(
                                        BorderStroke(
                                            0.5.dp,
                                            Color.Black
                                        ),
                                        RectangleShape
                                    )
                                    .clickable {
                                        // TODO: Card onClick
                                        viewModel.cardClick(
                                            gameId,
                                            isNew.check(
                                                viewModel.firstPlayer,
                                                viewModel.secondPlayer
                                            )[i],
                                            isNew
                                        )
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