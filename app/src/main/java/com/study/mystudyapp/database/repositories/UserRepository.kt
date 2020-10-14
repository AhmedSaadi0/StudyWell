package com.study.mystudyapp.database.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.study.mystudyapp.Coroutine
import com.study.mystudyapp.database.room.AppDatabase
import com.study.mystudyapp.database.room.users.User

class UserRepository(private val db: AppDatabase) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    fun getUser() = db.getUserDao().getUser()

    // login logic here
    fun userLogin(email: String, password: String): LiveData<String> {
        val loginResponse = MutableLiveData<String>()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    Coroutine.main {
                        db.getUserDao().insert(
                            User(
                                user_id = FirebaseAuth.getInstance().uid,
                                user_email = email,
                                user_password = password
                            )
                        )
                    }

                    loginResponse.value = "authenticated"
                } else {
                    if (it.exception?.message!!.contains("A network error"))
                        loginResponse.value = "network error"
                    else {
                        createUser(email, password)
                    }
                }
            }
        return loginResponse
    }

    // login logic here
    private fun createUser(email: String, password: String): LiveData<String> {
        val loginResponse = MutableLiveData<String>()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    userLogin(email, password)
                } else {
                    loginResponse.value = "not authenticated"
                }
            }
        return loginResponse
    }

}