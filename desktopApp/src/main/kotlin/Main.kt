import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.skia.Image

fun loadDrawable(name: String): ImageBitmap {
    val resourcePath = "/$name"
    val stream = object  {}.javaClass.getResourceAsStream(resourcePath)

    return Image.makeFromEncoded(stream.readBytes()).toComposeImageBitmap()
}

fun pageLevels(level: Int): List<String> {
    return when (level) {
        1 -> listOf (
            "Type your Email",
            "Type your Name",
            "Type your Password",
            "Type Password again"
        )
        2 -> listOf (
            "Type your INN",
            "Type number card",
            "Type your phone",
            "Additional phone"
        )
        else -> listOf (
            "Error of level page",
            "Send me screen with",
            "This page",
            "Thanks."
        )
    }
}

@Composable
fun AuthContent(
    mode: String,
    notAuth: MutableState<Boolean>
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var checkPass by remember { mutableStateOf("") }

    var inn by remember { mutableStateOf("") }
    var card by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var adphone by remember { mutableStateOf("") }
    var key by remember { mutableStateOf("") }
    var pageLevel by remember { mutableIntStateOf(1) }

    var isClickedF by remember { mutableStateOf(false) }

    var isClickedS by remember { mutableStateOf(false) }

    val clickableColor = Color(45, 98, 139)
    val nonClickableColor = Color(110, 160, 204)

    val firstBox by animateColorAsState(
        targetValue = if (isClickedF) clickableColor else nonClickableColor, label = ""
    )
    val secondBox by animateColorAsState(
        targetValue = if (isClickedS) clickableColor else nonClickableColor, label = ""
    )

    var pageVis by remember { mutableStateOf(true) }

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val color1 by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.Blue,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    val color2 by infiniteTransition.animateColor(
        initialValue = Color.Blue,
        targetValue = Color.Red,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val scope = rememberCoroutineScope()

    var passwordVisibility: Boolean by remember { mutableStateOf(false) }
    var keyVisibility: Boolean by remember { mutableStateOf(false) }
    var checkPassVisibility: Boolean by remember { mutableStateOf(false) }

    @Composable
    fun getTextField(
        value: String,
        onValueChange: (String) -> Unit,
        label: String,
        keyboardType: KeyboardType,
        leadingIcon: @Composable (() -> Unit)? = null,
        trailingIcon: @Composable (() -> Unit)? = null,
        visualTransformation: VisualTransformation = VisualTransformation.None
    ) = TextField(
        modifier = Modifier.width(300.dp),
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.textFieldColors(cursorColor = clickableColor, focusedIndicatorColor = clickableColor, focusedLabelColor = clickableColor)
    )

    Text(
        buildAnnotatedString {
            append("Welcome to ")
            withStyle(style = SpanStyle(brush = Brush.linearGradient(colors = listOf(color1, color2)))) {
                append(mode)
            }
            append(" page!")
        },
        modifier = Modifier.padding(8.dp),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onSurface
    )

    Spacer(modifier = Modifier.padding(24.dp))

    AnimatedVisibility(
        visible = pageVis,
        enter = fadeIn(animationSpec = tween(500)),
        exit = fadeOut(animationSpec = tween(500))
    ) {
        Column {
            if (pageLevel < 3) {
                getTextField(
                    value = if (pageLevel == 1) email else inn,
                    onValueChange = { newText -> if (pageLevel == 1) email = newText else inn = newText },
                    label = pageLevels(pageLevel).getOrNull(0) ?: "",
                    keyboardType = if (pageLevel == 1) KeyboardType.Email else KeyboardType.Number,
                    leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = "Email") }
                )

                if (mode == "Register") {
                    getTextField(
                        value = if (pageLevel == 1) name else card,
                        onValueChange = { newText -> if (pageLevel == 1) name = newText else card = newText },
                        label = pageLevels(pageLevel).getOrNull(1) ?: "",
                        keyboardType = if (pageLevel == 1) KeyboardType.Text else KeyboardType.Number,
                        leadingIcon = {
                            Icon(Icons.Outlined.ShoppingCart, contentDescription = "Name")
                        }
                    )
                }

                getTextField(
                    value = if (pageLevel == 1) password else phone,
                    onValueChange = { newText -> if (pageLevel == 1) password = newText else phone = newText },
                    label = pageLevels(pageLevel).getOrNull(2) ?: "",
                    keyboardType = if (pageLevel == 1) KeyboardType.Password else KeyboardType.Phone,
                    leadingIcon = {
                        if (pageLevel == 1) Icon(Icons.Outlined.Lock, contentDescription = "Password")
                        else Icon(Icons.Outlined.Phone, contentDescription = "Phone")
                    },
                    trailingIcon = {
                        if (pageLevel == 1) {
                            val image = if (passwordVisibility) loadDrawable("ic_visibility.png") else loadDrawable("ic_visibility_off.png")
                            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                                Icon(image, contentDescription = "")
                            }
                        }
                    },
                    visualTransformation = if (passwordVisibility || pageLevel > 1) VisualTransformation.None else PasswordVisualTransformation()
                )

                if (mode == "Register") {
                    getTextField(
                        value = if (pageLevel == 1) checkPass else adphone,
                        onValueChange = { newText -> if (pageLevel == 1) checkPass = newText else adphone = newText },
                        label = pageLevels(pageLevel).getOrNull(3) ?: "",
                        keyboardType = if (pageLevel == 1) KeyboardType.Password else KeyboardType.Phone,
                        leadingIcon = {
                            if (pageLevel == 1) {
                                Icon(Icons.Outlined.CheckCircle, contentDescription = "Check password")
                            } else {
                                Icon(Icons.Outlined.Warning, contentDescription = "Additional phone")
                            }
                        },
                        trailingIcon = {
                            if (pageLevel == 1) {
                                val image = if (checkPassVisibility) loadDrawable("ic_visibility.png") else loadDrawable("ic_visibility_off.png")
                                IconButton(onClick = { checkPassVisibility = !checkPassVisibility }) {
                                    Icon(image, contentDescription = "")
                                }
                            }
                        },
                        visualTransformation = if (checkPassVisibility || pageLevel > 1) VisualTransformation.None else PasswordVisualTransformation()
                    )
                }

                if (pageLevel == 2 || mode == "Login") {
                    getTextField(
                        value = key,
                        onValueChange = { newText -> key = newText },
                        label = "Key (for admins)",
                        keyboardType = KeyboardType.Password,
                        leadingIcon = { Icon(Icons.Outlined.Build, contentDescription = "Admin key") },
                        trailingIcon = {
                            val image = if (keyVisibility) loadDrawable("ic_visibility.png") else loadDrawable("ic_visibility_off.png")
                            IconButton(onClick = { keyVisibility = !keyVisibility }) {
                                Icon(image, contentDescription = "")
                            }
                        },
                        visualTransformation = if (keyVisibility) VisualTransformation.None else PasswordVisualTransformation()
                    )
                }
            } else {
                Row(
                    Modifier.wrapContentSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(
                        Pair(loadDrawable("ic_footprint.png"), "Walking"),
                        Pair(loadDrawable("ic_pedal_bike.png"), "Biker")
                    ).forEachIndexed { index, (icon, label) ->
                        val isClicked = if (index == 0) isClickedF else isClickedS
                        val onClick = if (index == 0) {
                            { isClickedF = true; isClickedS = false }
                        } else {
                            { isClickedS = true; isClickedF = false }
                        }
                        Column(
                            Modifier
                                .padding(8.dp)
                                .width(200.dp)
                                .height(200.dp)
                                .weight(1F)
                                .clip(RoundedCornerShape(24.dp))
                                .background(if (index == 0) firstBox else secondBox)
                                .clickable(onClick = onClick)
                                .wrapContentSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                icon,
                                contentDescription = "",
                                Modifier.size(82.dp).padding(8.dp),
                                tint = if (isClicked) MaterialTheme.colors.surface else MaterialTheme.colors.onSurface
                            )
                            Text(
                                text = label,
                                Modifier.padding(8.dp),
                                color = if (isClicked) MaterialTheme.colors.surface else MaterialTheme.colors.onSurface,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }

    Row (
        Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ){
        if (pageLevel > 1) {
            Button(
                onClick = {
                    pageVis = !pageVis
                    scope.launch {
                        delay(500)
                        if (pageLevel != 1)
                            pageLevel -= 1
                        pageVis = true
                    }
                },
                Modifier
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(clickableColor)
            )
            {
                Text (
                    text = "Back",
                    color = MaterialTheme.colors.surface
                )
            }
        }
        Button(
            onClick = {
                if (mode == "Register") {
                    scope.launch {
                        if (pageLevel != 3) {
                            pageVis = !pageVis
                            delay(500)
                            pageLevel += 1
                            pageVis = true
                        } else {
                            /* context.startActivity(Intent(context, Workspace::class.java))
                            activity?.finish() */
                        }
                    }
                } else {
                    // Login check
                    /* context.startActivity(Intent(context, Workspace::class.java))
                    activity?.finish() */
                }
            },
            Modifier
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(clickableColor)
        )
        {
            Text (
                text = "Enter",
                color = MaterialTheme.colors.surface
            )
        }
    }
    Text (
        "Exit from Auth",
        Modifier
            .clickable{
                pageLevel = 1
                notAuth.value = true
            },
        color = Color.Gray,
        fontSize = 20.sp
    )
}

@Composable
@Preview
fun App() {

    MaterialTheme {
        var pickHeight by remember { mutableStateOf(0.dp) }
        val clickableColor = Color(45, 98, 139)
        var mode by remember { mutableStateOf("Login") }
        val notAuth = remember { mutableStateOf(true) }

        Column (
            Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Column (
                Modifier
                    .padding(8.dp)
                    .width(500.dp)
                    .height(500.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(224, 226, 232))
                    .wrapContentSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                if (notAuth.value) {
                    Text(
                        text = "Do you have account?",
                        Modifier
                            .padding(8.dp),
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Button(onClick = {
                        pickHeight = 100.dp
                        mode = "Register"
                        notAuth.value = false
                    },
                        colors = ButtonDefaults.buttonColors(clickableColor)
                    ) {
                        Text (
                            text = "Reg",
                            color = Color.White
                        )
                    }
                    Text(
                        text = "or",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Button(onClick = {
                        mode = "Login"
                        notAuth.value = false
                    },
                        colors = ButtonDefaults.buttonColors(clickableColor)
                    ) {
                        Text (
                            text = "Log",
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                } else {
                    AuthContent(mode, notAuth)
                }
            }
        }
    }
}

fun main() = application {
    Window(title="myTasks", onCloseRequest = ::exitApplication) {
        App()
    }
}
