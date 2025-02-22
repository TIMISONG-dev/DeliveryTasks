import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*

fun pageLevels(level: Int): List<String> {
    return when (level) {
        1 -> listOf (
            "Введите емаил",
            "Введите имя",
            "Введите пароль",
            "Введите пароль снова"
//            "Type your Email",
//            "Type your Name",
//            "Type your Password",
//            "Type Password again"
        )
        2 -> listOf (
            "Введите ИНН",
            "Введите номер банковской карты",
            "Введите телефон",
            "Введите доп. телефон"
//            "Type your INN",
//            "Type number card",
//            "Type your phone",
//            "Additional phone"
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
fun Main(onNavigateTo: ()-> Unit) {

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
                    .width(600.dp)
                    .height(600.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(224, 226, 232))
                    .wrapContentSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                if (notAuth.value) {
                    Text(
                        // text = "Do you have account?",
                        text = "У вас есть аккаунт?",
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
                            // text = "Reg",
                            text = "Регистрация",
                            color = Color.White
                        )
                    }
                    Text(
                        // text = "or",
                        text = "или",
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
                            // text = "Log",
                            text = "Логин",
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                } else {
                    AuthContent(mode, notAuth, onNavigateTo)
                }
            }
        }
    }
}

fun main() = application {
    Window(title="DeliveryTasks", onCloseRequest = ::exitApplication) {
        val cs = remember { mutableStateOf("main") }
        when (cs.value) {
            "main" -> Main(onNavigateTo = { cs.value = "workspace" })
            "workspace" -> Workspace(onNavigateTo = { cs.value = "main" })
        }
    }
}