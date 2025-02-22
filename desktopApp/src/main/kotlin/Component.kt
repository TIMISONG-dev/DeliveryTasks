import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image

class Component {
    companion object {
        fun loadDrawable(name: String): ImageBitmap {
            val resourcePath = "/$name"
            val stream = object  {}.javaClass.getResourceAsStream(resourcePath)

            return Image.makeFromEncoded(stream.readBytes()).toComposeImageBitmap()
        }

        val nextStep = mutableStateOf(false)
        val clickableColor = Color(45, 98, 139)
        val nonClickableColor = Color(110, 160, 204)
    }
}