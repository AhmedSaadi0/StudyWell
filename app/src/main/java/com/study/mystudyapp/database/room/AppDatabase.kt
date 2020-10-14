package com.study.mystudyapp.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.study.mystudyapp.database.room.users.User
import com.study.mystudyapp.database.room.users.UserDao
import net.sqlcipher.database.SupportFactory


@Database(
    entities = [
        User::class
    ],

    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    companion object {

        // for encryption
        private val passphrase =
            net.sqlcipher.database.SQLiteDatabase.getBytes("1gGj0aeIjbHZaZBTFN1vOf3afd6RtPqHVOoX-UwvvonjZSrezFX2-PM".toCharArray())

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