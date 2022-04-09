import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import com.miled.marvel.R
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ComicsImage(imagePath: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        GlideImage(
            imageModel = imagePath,
            contentScale = ContentScale.FillBounds,
            error = ImageBitmap.imageResource(R.drawable.error)
        )
    }

}