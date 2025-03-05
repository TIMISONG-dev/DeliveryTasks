import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CourierBagViewModel {
    var bags by mutableStateOf(
        listOf(
            CourierBag(1, "Courier Bag", 5, "30x40 cm", 1.8),
            CourierBag(2, "Courier Bag", 10, "40x50 cm", 1.9),
            CourierBag(3, "Courier Bag", 15, "50x60 cm", 2.0)
        )
    )

    fun addBag(name: String, quantity: Int, size: String, weight: Double) {
        val newId = (bags.maxOfOrNull { it.id } ?: 0) + 1
        bags = bags + CourierBag(newId, name, quantity, size, weight)
    }

    fun removeBag(id: Int) {
        bags = bags.filter { it.id != id }
    }

    fun updateQuantity(id: Int, newQuantity: Int) {
        bags = bags.map {
            if (it.id == id) it.copy(quantity = maxOf(0, newQuantity)) else it
        }
    }
}
