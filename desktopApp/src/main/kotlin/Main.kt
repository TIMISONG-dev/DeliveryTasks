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
@Preview
fun App() {

    MaterialTheme {
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

        var pickHeight by remember { mutableStateOf(0.dp) }

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

        val clickableColor = Color(45, 98, 139)
        val nonClickableColor = Color(110, 160, 204)

        val firstBox by animateColorAsState(
            targetValue = if (isClickedF) clickableColor else nonClickableColor, label = ""
        )
        val secondBox by animateColorAsState(
            targetValue = if (isClickedS) clickableColor else nonClickableColor, label = ""
        )

        var mode by remember { mutableStateOf("Login") }

        var passwordVisibility: Boolean by remember { mutableStateOf(false) }
        var keyVisibility: Boolean by remember { mutableStateOf(false) }
        var checkPassVisibility: Boolean by remember { mutableStateOf(false) }

        var notAuth = remember { mutableStateOf(true) }

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
                    Text(
                        buildAnnotatedString {
                            append("Welcome to ")
                            withStyle(
                                style = SpanStyle(
                                    brush = Brush.linearGradient(colors = listOf(color1, color2))
                                )
                            ) {
                                append(mode)
                            }
                            append(" page!")
                        },
                        Modifier
                            .padding(8.dp),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    if (pageLevel < 3) {
                        TextField(
                            modifier = Modifier
                                .width(300.dp),
                            value = if (pageLevel == 1) email else inn,
                            onValueChange =
                            {
                                    newText -> if (pageLevel == 1) {
                                email = newText
                            } else {
                                inn = newText
                            }
                            },
                            label =
                            {
                                Text(
                                    text = pageLevels(pageLevel).getOrNull(0) ?: ""
                                )
                            },
                            singleLine = true,
                            keyboardOptions =
                            if (pageLevel == 1) {
                                KeyboardOptions(keyboardType = KeyboardType.Email)
                            } else {
                                KeyboardOptions(keyboardType = KeyboardType.Number)
                            },
                            leadingIcon =
                            if (pageLevel == 1) {
                                {
                                    Icon (
                                        Icons.Outlined.Email,
                                        contentDescription = "Email"
                                    )
                                }
                            } else {
                                {
                                    Icon (
                                        Icons.Outlined.Info,
                                        contentDescription = "INN"
                                    )
                                }
                            },
                            colors = TextFieldDefaults.textFieldColors(cursorColor = clickableColor, focusedIndicatorColor = clickableColor, focusedLabelColor = clickableColor)
                        )
                        if (mode == "Register") {
                            TextField(
                                modifier = Modifier
                                    .width(300.dp),
                                value = if (pageLevel == 1) name else card,
                                onValueChange =
                                {
                                        newText -> if (pageLevel == 1) {
                                    name = newText
                                } else {
                                    card = newText
                                }
                                },
                                label =
                                {
                                    Text(
                                        text = pageLevels(pageLevel).getOrNull(1) ?: ""
                                    )
                                },
                                singleLine = true,
                                keyboardOptions =
                                if (pageLevel == 1) {
                                    KeyboardOptions(keyboardType = KeyboardType.Text)
                                } else {
                                    KeyboardOptions(keyboardType = KeyboardType.Number)
                                },
                                leadingIcon = if (pageLevel == 1) {
                                    {
                                        Icon(
                                            Icons.Outlined.Person,
                                            contentDescription = "Name"
                                        )
                                    }
                                } else {
                                    {
                                        Icon(
                                            Icons.Outlined.ShoppingCart,
                                            contentDescription = "Card"
                                        )
                                    }
                                },
                                colors = TextFieldDefaults.textFieldColors(cursorColor = clickableColor, focusedIndicatorColor = clickableColor, focusedLabelColor = clickableColor)
                            )
                        }
                        TextField(
                            modifier = Modifier
                                .width(300.dp),
                            value = if (pageLevel == 1) password else phone,
                            onValueChange =
                            {
                                    newText -> if (pageLevel == 1) {
                                password = newText
                            } else {
                                phone = newText
                            }
                            },
                            label =
                            {
                                Text(
                                    text = pageLevels(pageLevel).getOrNull(2) ?: ""
                                )
                            },
                            singleLine = true,
                            visualTransformation = if (passwordVisibility || pageLevel > 1) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions =
                            if (pageLevel == 1) {
                                KeyboardOptions(keyboardType = KeyboardType.Password)
                            } else {
                                KeyboardOptions(keyboardType = KeyboardType.Phone)
                            },
                            leadingIcon = if (pageLevel == 1) {
                                {
                                    Icon(Icons.Outlined.Lock, contentDescription = "Password")
                                }
                            } else {
                                {
                                    Icon(Icons.Outlined.Phone, contentDescription = "Phone")
                                }
                            },
                            trailingIcon = if (pageLevel == 1) {
                                {
                                    val image = if (passwordVisibility) {
                                        loadDrawable("ic_visibility.png")
                                    } else {
                                        loadDrawable("ic_visibility_off.png")
                                    }
                                    IconButton(onClick = {
                                        passwordVisibility = !passwordVisibility
                                    }) {
                                        Icon(
                                            image,
                                            contentDescription = ""
                                        )
                                    }
                                }
                            } else null,
                            colors = TextFieldDefaults.textFieldColors(cursorColor = clickableColor, focusedIndicatorColor = clickableColor, focusedLabelColor = clickableColor)
                        )
                        if (mode == "Register") {
                            TextField(
                                modifier = Modifier
                                    .width(300.dp),
                                value = if (pageLevel == 1) checkPass else adphone,
                                onValueChange =
                                {
                                        newText -> if (pageLevel == 1) {
                                    checkPass = newText
                                } else {
                                    adphone = newText
                                }
                                },
                                label =
                                {
                                    Text(
                                        text = pageLevels(pageLevel).getOrNull(3) ?: ""
                                    )
                                },
                                singleLine = true,
                                visualTransformation = if (checkPassVisibility || pageLevel > 1) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions =
                                if (pageLevel == 1) {
                                    KeyboardOptions(keyboardType = KeyboardType.Password)
                                } else {
                                    KeyboardOptions(keyboardType = KeyboardType.Phone)
                                },
                                leadingIcon = if (pageLevel == 1) {
                                    {
                                        Icon(
                                            Icons.Outlined.CheckCircle,
                                            contentDescription = "Check password"
                                        )
                                    }
                                } else {
                                    {
                                        Icon(
                                            Icons.Outlined.Warning,
                                            contentDescription = "Additional phone"
                                        )
                                    }
                                },
                                trailingIcon = if (pageLevel == 1) {
                                    {
                                        val image = if (checkPassVisibility) {
                                            loadDrawable("ic_visibility.png")
                                        } else {
                                            loadDrawable("ic_visibility_off.png")
                                        }
                                        IconButton(onClick = {
                                            checkPassVisibility = !checkPassVisibility
                                        }) {
                                            Icon(
                                                image,
                                                contentDescription = ""
                                            )
                                        }
                                    }
                                } else null,
                                colors = TextFieldDefaults.textFieldColors(cursorColor = clickableColor, focusedIndicatorColor = clickableColor, focusedLabelColor = clickableColor)
                            )
                        }
                        if (pageLevel == 2 || mode == "Login") {
                            TextField(
                                modifier = Modifier
                                    .width(300.dp),
                                value = key,
                                onValueChange =
                                {
                                        newText -> key = newText
                                },
                                label =
                                {
                                    Text(
                                        text = "Key (for admins)"
                                    )
                                },
                                singleLine = true,
                                visualTransformation = if (keyVisibility || pageLevel > 1) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Build,
                                        contentDescription = "Admin key"
                                    )
                                },
                                trailingIcon = {
                                    val image = if (keyVisibility) {
                                        loadDrawable("ic_visibility.png")
                                    } else {
                                        loadDrawable("ic_visibility_off.png")
                                    }
                                    IconButton(onClick = {
                                        keyVisibility = !keyVisibility
                                    }) {
                                        Icon(
                                            image,
                                            contentDescription = ""
                                        )
                                    }
                                },
                                colors = TextFieldDefaults.textFieldColors(cursorColor = clickableColor, focusedIndicatorColor = clickableColor, focusedLabelColor = clickableColor)
                            )
                        }
                    } else {
                        Row (
                            Modifier
                                .wrapContentSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Column (
                                Modifier
                                    .padding(8.dp)
                                    .width(200.dp)
                                    .height(200.dp)
                                    .weight(1F)
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(firstBox)
                                    .clickable {
                                        isClickedF = true
                                        isClickedS = false
                                    }
                                    .wrapContentSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon (
                                    loadDrawable("ic_footprint.png"),
                                    contentDescription = "",
                                    Modifier
                                        .size(82.dp)
                                        .padding(8.dp),
                                    tint = if (isClickedF) MaterialTheme.colors.surface else MaterialTheme.colors.onSurface
                                )
                                Text (
                                    text = "Walking",
                                    Modifier
                                        .padding(8.dp),
                                    color = if (isClickedF) MaterialTheme.colors.surface else MaterialTheme.colors.onSurface,
                                    fontSize = 18.sp
                                )
                            }
                            Column (
                                Modifier
                                    .padding(8.dp)
                                    .width(200.dp)
                                    .height(200.dp)
                                    .weight(1F)
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(secondBox)
                                    .clickable {
                                        isClickedS = true
                                        isClickedF = false
                                    }
                                    .wrapContentSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon (
                                    loadDrawable("ic_pedal_bike.png"),
                                    contentDescription = "",
                                    Modifier
                                        .size(82.dp)
                                        .padding(8.dp),
                                    tint = if (isClickedS) MaterialTheme.colors.surface else MaterialTheme.colors.onSurface
                                )
                                Text (
                                    text = "Biker",
                                    Modifier
                                        .padding(8.dp),
                                    color = if (isClickedS) MaterialTheme.colors.surface else MaterialTheme.colors.onSurface,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
                if (!notAuth.value) {
                    Row (
                        Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ){
                        if (pageLevel > 1) {
                            Button(
                                onClick = {
                                    if (pageLevel != 1)
                                        pageLevel -= 1
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
                                    if (pageLevel != 3) {
                                        pageLevel += 1
                                    } else {
                                        /* context.startActivity(Intent(context, Workspace::class.java))
                                        activity?.finish() */
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
            }
        }
    }
}

fun main() = application {
    Window(title="myTasks", onCloseRequest = ::exitApplication) {
        App()
    }
}
