import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MainData {
    companion object {
        var email by mutableStateOf("")
        var password by mutableStateOf("")
        var name by mutableStateOf("")
        var checkPass by mutableStateOf("")
        var inn by mutableStateOf("")
        var card by mutableStateOf("")
        var phone by mutableStateOf("")
        var adphone by mutableStateOf("")
        var key by mutableStateOf("")
        var pageLevel by mutableIntStateOf(1)
        var support by mutableStateOf(false)
    }
}