package com.study.mystudyapp.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.study.mystudyapp.database.room.categories.CategoriesDao
import com.study.mystudyapp.database.room.categories.CategoriesTable
import com.study.mystudyapp.database.room.users.User
import com.study.mystudyapp.database.room.users.UserDao
import com.study.mystudyapp.database.room.words.WordsDao
import com.study.mystudyapp.database.room.words.WordsTable
import net.sqlcipher.database.SupportFactory


@Database(
    entities = [
        User::class,
        WordsTable::class,
        CategoriesTable::class
    ],

    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getWordsTableDao(): WordsDao
    abstract fun getCatTableDao(): CategoriesDao


    companion object {

        // for encryption
        private val passphrase =
            net.sqlcipher.database.SQLiteDatabase.getBytes("1gGj0aeIjbHZaZBTFN1vOf3afd6RtPqHVOoX-hSETW51DSJWKOWU-PM".toCharArray())

        private val factory = SupportFactory(passphrase)


        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "MyDatabase.db"
            ).openHelperFactory(factory)
                .fallbackToDestructiveMigration().build()

    }
}