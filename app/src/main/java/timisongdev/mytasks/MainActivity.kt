package timisongdev.mytasks

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import kotlinx.coroutines.*
import timisongdev.mytasks.ui.theme.MyTasksTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyTasksTheme {
                Scaffold {
                    Greeting()
                }
            }
        }
    }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthContent(
    pageLevel: MutableIntState,
    mode: String,
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

    val material = MaterialTheme.colorScheme

    var isClickedF by remember { mutableStateOf(false) }

    var isClickedS by remember { mutableStateOf(false) }

    val firstBox by animateColorAsState(
        targetValue = if (isClickedF) material.primary else material.primaryContainer, label = ""
    )
    val secondBox by animateColorAsState(
        targetValue = if (isClickedS) material.primary else material.primaryContainer, label = ""
    )

    var pageVis by remember { mutableStateOf(true) }

    val sheetState = rememberBottomSheetScaffoldState(bottomSheetState = rememberStandardBottomSheetState(skipHiddenState = false))

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

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val context = LocalContext.current

    val activity = (LocalContext.current as? Activity)

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
        visualTransformation = visualTransformation
    )

    Column(
        modifier = Modifier
            .heightIn(min = 100.dp, max = screenHeight - 100.dp)
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.padding(24.dp))

        AnimatedVisibility(
            visible = pageVis,
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            Column {
                if (pageLevel.intValue < 3) {
                    getTextField(
                        value = if (pageLevel.intValue == 1) email else inn,
                        onValueChange = { newText -> if (pageLevel.intValue == 1) email = newText else inn = newText },
                        label = pageLevels(pageLevel.intValue).getOrNull(0) ?: "",
                        keyboardType = if (pageLevel.intValue == 1) KeyboardType.Email else KeyboardType.Number,
                        leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = "Email") }
                    )

                    if (mode == "Register") {
                        getTextField(
                            value = if (pageLevel.intValue == 1) name else card,
                            onValueChange = { newText -> if (pageLevel.intValue == 1) name = newText else card = newText },
                            label = pageLevels(pageLevel.intValue).getOrNull(1) ?: "",
                            keyboardType = if (pageLevel.intValue == 1) KeyboardType.Text else KeyboardType.Number,
                            leadingIcon = {
                                Icon(painter = painterResource(id = R.drawable.ic_name), contentDescription = "Name")
                            }
                        )
                    }

                    getTextField(
                        value = if (pageLevel.intValue == 1) password else phone,
                        onValueChange = { newText -> if (pageLevel.intValue == 1) password = newText else phone = newText },
                        label = pageLevels(pageLevel.intValue).getOrNull(2) ?: "",
                        keyboardType = if (pageLevel.intValue == 1) KeyboardType.Password else KeyboardType.Phone,
                        leadingIcon = {
                            if (pageLevel.intValue == 1) Icon(Icons.Outlined.Lock, contentDescription = "Password")
                            else Icon(Icons.Outlined.Phone, contentDescription = "Phone")
                        },
                        trailingIcon = {
                            if (pageLevel.intValue == 1) {
                                val image = if (passwordVisibility) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                                    Icon(painter = painterResource(id = image), contentDescription = "")
                                }
                            }
                        },
                        visualTransformation = if (passwordVisibility || pageLevel.intValue > 1) VisualTransformation.None else PasswordVisualTransformation()
                    )

                    if (mode == "Register") {
                        getTextField(
                            value = if (pageLevel.intValue == 1) checkPass else adphone,
                            onValueChange = { newText -> if (pageLevel.intValue == 1) checkPass = newText else adphone = newText },
                            label = pageLevels(pageLevel.intValue).getOrNull(3) ?: "",
                            keyboardType = if (pageLevel.intValue == 1) KeyboardType.Password else KeyboardType.Phone,
                            leadingIcon = {
                                if (pageLevel.intValue == 1) {
                                    Icon(painter = painterResource(id = R.drawable.ic_enhanced_encryption), contentDescription = "Check password")
                                } else {
                                    Icon(Icons.Outlined.Warning, contentDescription = "Additional phone")
                                }
                            },
                            trailingIcon = {
                                if (pageLevel.intValue == 1) {
                                    val image = if (checkPassVisibility) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                                    IconButton(onClick = { checkPassVisibility = !checkPassVisibility }) {
                                        Icon(painter = painterResource(id = image), contentDescription = "")
                                    }
                                }
                            },
                            visualTransformation = if (checkPassVisibility || pageLevel.intValue > 1) VisualTransformation.None else PasswordVisualTransformation()
                        )
                    }

                    if (pageLevel.intValue == 2 || mode == "Login") {
                        getTextField(
                            value = key,
                            onValueChange = { newText -> key = newText },
                            label = "Key (for admins)",
                            keyboardType = KeyboardType.Password,
                            leadingIcon = { Icon(Icons.Outlined.Build, contentDescription = "Admin key") },
                            trailingIcon = {
                                val image = if (keyVisibility) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                                IconButton(onClick = { keyVisibility = !keyVisibility }) {
                                    Icon(painter = painterResource(id = image), contentDescription = "")
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
                            Pair(R.drawable.ic_footprint, "Walking"),
                            Pair(R.drawable.ic_pedal_bike, "Biker")
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
                                    painter = painterResource(id = icon),
                                    contentDescription = "",
                                    Modifier.size(82.dp).padding(8.dp),
                                    tint = if (isClicked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = label,
                                    Modifier.padding(8.dp),
                                    color = if (isClicked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        Button(onClick = {
            if (mode == "Register") {
                scope.launch {
                    if (pageLevel.intValue != 3) {
                        pageVis = !pageVis
                        delay(500)
                        pageLevel.intValue += 1
                        pageVis = true
                    } else {
                        context.startActivity(Intent(context, Workspace::class.java))
                        activity?.finish()
                    }
                }
            } else {
                // Login check
                context.startActivity(Intent(context, Workspace::class.java))
                activity?.finish()
            }
        }, Modifier.padding(8.dp)) {
            Text(text = "Enter")
        }
        if (pageLevel.intValue > 1) {
            Button(onClick = {
                pageVis = !pageVis
                scope.launch {
                    delay(500)
                    if (pageLevel.intValue != 1)
                        pageLevel.intValue -= 1
                    pageVis = true
                }
            }, Modifier.padding(8.dp)) {
                Text(text = "Back")
            }
        }

        Text(
            text = "Close Tab",
            Modifier.padding(8.dp).clickable {
                scope.launch {
                    sheetState.bottomSheetState.hide()
                }
            },
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition", "SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting() {
    val pageLevel = remember { mutableIntStateOf(1) }

    var pickHeight by remember { mutableStateOf(0.dp) }
    val sheetState = rememberBottomSheetScaffoldState(bottomSheetState = rememberStandardBottomSheetState(skipHiddenState = false))

    val scope = rememberCoroutineScope()
    var mode by remember { mutableStateOf("Login") }

    Column (
        Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Column (
            Modifier
                .padding(8.dp)
                .width(200.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.inverseOnSurface)
                .wrapContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Do you have account?",
                Modifier
                    .padding(8.dp),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Button(onClick = {
                scope.launch {
                    sheetState.bottomSheetState.expand()
                }
                pickHeight = 100.dp
                mode = "Register"
            },
            ) {
                Text (
                    text = "Reg"
                )
            }
            Text(
                text = "or",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Button(onClick = {
                scope.launch {
                    pageLevel.intValue = 1
                    sheetState.bottomSheetState.expand()
                }
                pageLevel.intValue = 1
                pickHeight = 100.dp
                mode = "Login"
            },
            ) {
                Text (
                    text = "Log"
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
        }
    }

    BottomSheetScaffold(
        sheetContent = {
            AuthContent(pageLevel, mode)
        },
        Modifier
            .heightIn(min = 100.dp, max = 500.dp)
            .padding(8.dp),
        scaffoldState = sheetState,
        sheetPeekHeight = pickHeight,
        sheetSwipeEnabled = true,
        content = {
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyTasksTheme {
        Greeting()
    }
}