package com.study.mystudyapp.database.room.users

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.study.mystudyapp.database.room.users.CURRENT_USER_ID
import com.study.mystudyapp.database.room.users.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM user Where uid = $CURRENT_USER_ID")
    fun getUser(): LiveData<User>

    @Query("SELECT * FROM user Where uid = $CURRENT_USER_ID")
    fun getUserInBack(): User

    @Query("SELECT user_local FROM user WHERE uid = $CURRENT_USER_ID")
    fun getUserLocal(): LiveData<String>


}