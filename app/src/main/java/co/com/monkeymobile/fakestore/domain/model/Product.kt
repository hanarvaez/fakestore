package co.com.monkeymobile.fakestore.domain.model

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating,
    val isFavorite: Boolean = false
)

data class Rating(
    val rate: Double,
    val count: Int
)