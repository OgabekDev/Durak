package dev.ogabek.durak.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.ogabek.durak.R
import dev.ogabek.durak.ui.theme.DurakTheme
import dev.ogabek.durak.viewmodel.HomeViewModel
import dev.ogabek.durak.viewmodel.PlayViewModel
import dev.ogabek.durak.views.LoadingView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {

    var gameId by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.bg),
                contentScale = ContentScale.FillBounds
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.durak),
                contentDescription = "",
                modifier = Modifier
                    .padding(25.dp),
                contentScale = ContentScale.FillBounds
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {

            TextField(
                value = gameId,
                onValueChange = {
                    gameId = it
                },
                label = {
                    Text(text = "Game ID")
                },
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .padding(horizontal = 25.dp)
                    .fillMaxWidth()
            )

            Button(
                onClick = {
                    // TODO: Join Game onClick
                          viewModel.isGameHave(gameId)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
                    .padding(bottom = 10.dp)
                    .height(50.dp)
            ) {
                Text(text = "Join Game", fontSize = 14.sp, fontFamily = FontFamily.SansSerif)
            }

            Spacer(
                modifier = Modifier.height(50.dp)
            )

            if (viewModel.isGameHave.value == true){
                Button(
                    onClick = {
                        // TODO: Create Game onClick
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp)
                        .padding(bottom = 10.dp)
                        .height(50.dp)
                ) {
                    Text(
                        text = "Create Game",
                        fontSize = 14.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }
        }

        if (viewModel.isLoading.value) {
            LoadingView(title = "Loading...", subTitle = "Please wait!")
        }
        if (viewModel.isError.value) {
            Toast.makeText(LocalContext.current, viewModel.errorMessage.value, Toast.LENGTH_SHORT).show()
        }
    }

}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    DurakTheme {
        MainScreen()
    }
}