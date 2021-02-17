package com.study.mystudyapp.database.repositories

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.study.mystudyapp.Coroutine
import com.study.mystudyapp.database.models.WordsModel
import com.study.mystudyapp.database.room.AppDatabase
import com.study.mystudyapp.database.room.games.HanziGame
import com.study.mystudyapp.database.room.users.User
import com.study.mystudyapp.observeOnce

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


    fun setGame(
        lifecycleOwner: LifecycleOwner,
        month: String,
        words: List<WordsModel>
    ): LiveData<Boolean> {
        val done = MutableLiveData<Boolean>()


        db.getHanziGameDao().getRandomWordByDate(month, 10)
            .observeOnce(lifecycleOwner, {

                done.value = false
                Coroutine.main {
                    words.forEachIndexed { _, wordsModel ->

                        if (wordsModel.pinyin != null && wordsModel.hanzi != null && wordsModel.pinyin.isNotEmpty() && wordsModel.hanzi.length <= 4) {
                            db.getHanziGameDao().insert(
                                HanziGame(
                                    meaning = wordsModel.meaning,
                                    hanzi = wordsModel.hanzi,
                                    pinyin = wordsModel.pinyin,
                                    month = month,
                                    seen_count = 0,
                                    word_length = wordsModel.hanzi.length
                                )
                            )
                        }
                    }
                    done.value = true
                }

            })
        return done
    }


}