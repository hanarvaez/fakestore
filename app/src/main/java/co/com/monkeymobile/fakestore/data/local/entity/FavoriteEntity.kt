package co.com.monkeymobile.fakestore.data.local.entity

import androidx.room.Entity

@Entity(tableName = "favorites", primaryKeys = ["productId", "userId"])
data class FavoriteEntity(
    val productId: Int,
    val userId: Int
)