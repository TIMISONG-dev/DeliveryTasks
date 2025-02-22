import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.*
import kotlinx.coroutines.*

@Composable
fun AuthContent(
    mode: String,
    notAuth: MutableState<Boolean>,
    onNavigateTo: ()-> Unit
) {

    var isClickedF by remember { mutableStateOf(false) }

    var isClickedS by remember { mutableStateOf(false) }

    val firstBox by animateColorAsState(
        targetValue = if (isClickedF) Component.clickableColor else Component.nonClickableColor, label = ""
    )
    val secondBox by animateColorAsState(
        targetValue = if (isClickedS) Component.clickableColor else Component.nonClickableColor, label = ""
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
        colors = TextFieldDefaults.textFieldColors(cursorColor = Component.clickableColor, focusedIndicatorColor = Component.clickableColor, focusedLabelColor = Component.clickableColor)
    )

    Text(
        buildAnnotatedString {
            // append("Welcome to ")
            append("Добро пожаловать в ")
            withStyle(style = SpanStyle(brush = Brush.linearGradient(colors = listOf(color1, color2)))) {
                // append(mode)
                append(
                    if (mode == "Login") {
                        "Логин"
                    } else {
                        "Регистрацию"
                    }
                )
            }
            // append(" page!")
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
            if (MainData.pageLevel < 3) {
                getTextField(
                    value = if (MainData.pageLevel == 1) MainData.email else MainData.inn,
                    onValueChange = { newText -> if (MainData.pageLevel == 1) MainData.email = newText else MainData.inn = newText },
                    label = pageLevels(MainData.pageLevel).getOrNull(0) ?: "",
                    keyboardType = if (MainData.pageLevel == 1) KeyboardType.Email else KeyboardType.Number,
                    leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = "Email") }
                )

                if (mode == "Register") {
                    getTextField(
                        value = if (MainData.pageLevel == 1) MainData.name else MainData.card,
                        onValueChange = { newText -> if (MainData.pageLevel == 1) MainData.name = newText else MainData.card = newText },
                        label = pageLevels(MainData.pageLevel).getOrNull(1) ?: "",
                        keyboardType = if (MainData.pageLevel == 1) KeyboardType.Text else KeyboardType.Number,
                        leadingIcon = {
                            Icon(Icons.Outlined.ShoppingCart, contentDescription = "Name")
                        }
                    )
                }

                getTextField(
                    value = if (MainData.pageLevel == 1) MainData.password else MainData.phone,
                    onValueChange = { newText -> if (MainData.pageLevel == 1) MainData.password = newText else MainData.phone = newText },
                    label = pageLevels(MainData.pageLevel).getOrNull(2) ?: "",
                    keyboardType = if (MainData.pageLevel == 1) KeyboardType.Password else KeyboardType.Phone,
                    leadingIcon = {
                        if (MainData.pageLevel == 1) Icon(Icons.Outlined.Lock, contentDescription = "Password")
                        else Icon(Icons.Outlined.Phone, contentDescription = "Phone")
                    },
                    trailingIcon = {
                        if (MainData.pageLevel == 1) {
                            val image = if (passwordVisibility) Component.loadDrawable("ic_visibility.png") else Component.loadDrawable("ic_visibility_off.png")
                            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                                Icon(image, contentDescription = "")
                            }
                        }
                    },
                    visualTransformation = if (passwordVisibility || MainData.pageLevel > 1) VisualTransformation.None else PasswordVisualTransformation()
                )

                if (mode == "Register") {
                    getTextField(
                        value = if (MainData.pageLevel == 1) MainData.checkPass else MainData.adphone,
                        onValueChange = { newText -> if (MainData.pageLevel == 1) MainData.checkPass = newText else MainData.adphone = newText },
                        label = pageLevels(MainData.pageLevel).getOrNull(3) ?: "",
                        keyboardType = if (MainData.pageLevel == 1) KeyboardType.Password else KeyboardType.Phone,
                        leadingIcon = {
                            if (MainData.pageLevel == 1) {
                                Icon(Icons.Outlined.CheckCircle, contentDescription = "Check password")
                            } else {
                                Icon(Icons.Outlined.Warning, contentDescription = "Additional phone")
                            }
                        },
                        trailingIcon = {
                            if (MainData.pageLevel == 1) {
                                val image = if (checkPassVisibility) Component.loadDrawable("ic_visibility.png") else Component.loadDrawable("ic_visibility_off.png")
                                IconButton(onClick = { checkPassVisibility = !checkPassVisibility }) {
                                    Icon(image, contentDescription = "")
                                }
                            }
                        },
                        visualTransformation = if (checkPassVisibility || MainData.pageLevel > 1) VisualTransformation.None else PasswordVisualTransformation()
                    )
                }

                if (mode == "Login") {
                    getTextField(
                        value = MainData.key,
                        onValueChange = { newText -> MainData.key = newText },
                        // label = "Key (for admins)",
                        label = "Ключ (для поддержки)",
                        keyboardType = KeyboardType.Password,
                        leadingIcon = { Icon(Icons.Outlined.Build, contentDescription = "Admin key") },
                        trailingIcon = {
                            val image = if (keyVisibility) Component.loadDrawable("ic_visibility.png") else Component.loadDrawable("ic_visibility_off.png")
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
                        Pair(Component.loadDrawable("ic_footprint.png"), "Пеший курьер"),
                        Pair(Component.loadDrawable("ic_pedal_bike.png"), "Велокурьер")
//                        Pair(Component.loadDrawable("ic_footprint.png"), "Walking"),
//                        Pair(Component.loadDrawable("ic_pedal_bike.png"), "Biker")
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

            if (MainData.pageLevel == 1 && mode == "Register") {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ){
                    Checkbox(
                        MainData.support,
                        onCheckedChange = {
                            MainData.support = it
                        }
                    )
                    Text (
                        "Я регистрируюсь в поддержку"
                    )
                }
            }
        }
    }

    Row (
        Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ){
        if (MainData.pageLevel > 1) {
            Button(
                onClick = {
                    pageVis = !pageVis
                    scope.launch {
                        delay(500)
                        if (MainData.pageLevel != 1)
                            MainData.pageLevel -= 1
                        pageVis = true
                    }
                },
                Modifier
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(Component.clickableColor)
            )
            {
                Text (
                    // text = "Back",
                    text = "Назад",
                    color = MaterialTheme.colors.surface
                )
            }
        }
        Button(
            onClick = {
                if (mode == "Register") {
                    scope.launch {
                        if (MainData.pageLevel == 2 && MainData.support) {
                            onNavigateTo()
                        } else {
                            if (MainData.pageLevel != 3) {
                                pageVis = !pageVis
                                delay(500)
                                MainData.pageLevel += 1
                                pageVis = true
                            } else {
                                onNavigateTo()
                            }
                        }
                    }
                } else {
                    // Login check
                    onNavigateTo()
                }
            },
            Modifier
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(Component.clickableColor)
        )
        {
            Text (
                // text = "Enter",
                text = "Дальше",
                color = MaterialTheme.colors.surface
            )
        }
    }
    Text (
        // "Exit from Auth",
        "Выйти из Аунтефикации",
        Modifier
            .clickable{
                MainData.pageLevel = 1
                notAuth.value = true
            },
        color = Color.Gray,
        fontSize = 20.sp
    )
}