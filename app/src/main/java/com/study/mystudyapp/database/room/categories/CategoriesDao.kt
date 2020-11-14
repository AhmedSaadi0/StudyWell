package com.study.mystudyapp.database.room.categories

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface CategoriesDao {

    @Insert
    suspend fun insert(cat: CategoriesTable): Long

    @Query("SELECT * FROM categoriestable WHERE cat_name=:category")
    fun getCategoryByName(category: String): LiveData<CategoriesTable>

    @Query("SELECT * FROM categoriestable")
    fun getAllCategory(): LiveData<List<CategoriesTable>>


}