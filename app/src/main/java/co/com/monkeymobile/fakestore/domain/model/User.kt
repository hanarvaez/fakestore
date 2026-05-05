package co.com.monkeymobile.fakestore.domain.model

data class User(
    val id: Int,
    val email: String,
    val username: String,
    val password: String,
    val name: Name,
    val phone: String,
    val v: Int
)

data class Name(
    val firstname: String,
    val lastname: String
)