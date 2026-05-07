package co.com.monkeymobile.fakestore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.com.monkeymobile.fakestore.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUsersCount(): Int

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun validateUser(username: String, password: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}