package co.com.monkeymobile.fakestore.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: NameDto,
    @SerializedName("phone") val phone: String,
    @SerializedName("__v") val v: Int
)

data class NameDto(
    @SerializedName("firstname") val firstname: String,
    @SerializedName("lastname") val lastname: String
)