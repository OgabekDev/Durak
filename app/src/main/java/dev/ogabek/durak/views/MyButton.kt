package dev.ogabek.durak.views

import android.graphics.Paint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import dev.ogabek.durak.R
import dev.ogabek.durak.model.Card
import dev.ogabek.durak.model.CardType
import dev.ogabek.durak.ui.theme.DurakTheme

@Composable
fun MyButton(
    text: String,
    height: Int,
    width: Int,
    color: Color,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .height(height = height.dp)
            .width(width = width.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = color)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = FontFamily.Serif,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }

}

@Preview(showBackground = true)
@Composable
fun MyButtonPreview() {
    DurakTheme {
        MyButton(
            text = "I take",
            height = 35,
            width = 75,
            color = Color(144, 64, 58)
        ) {}
    }
}