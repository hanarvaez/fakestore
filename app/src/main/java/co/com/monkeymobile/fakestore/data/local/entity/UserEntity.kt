package co.com.monkeymobile.fakestore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val email: String,
    val username: String,
    val password: String,
    val firstname: String,
    val lastname: String,
    val phone: String,
    val v: Int
)