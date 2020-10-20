package com.study.mystudyapp.database.room.users

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_USER_ID = 0

@Entity
data class User(
    var user_id: String? = null,
    var user_name: String? = null,
    var user_email: String? = null,
    var user_password: String? = null,
    var user_phone: String? = null,
    var user_local: String? = null
) {
    @PrimaryKey(autoGenerate = false)
    var uid: Int = CURRENT_USER_ID
}
