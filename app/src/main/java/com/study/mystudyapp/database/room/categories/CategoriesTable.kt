package com.study.mystudyapp.database.room.categories

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoriesTable(
    var cat_name: String,
    var cat_level: Int
) {
    /**
     * 1 - HSk1
     * 2 - HSk2
     * 3 - HSk3
     * .......
     * */
    @PrimaryKey(autoGenerate = true)
    var cat_id: Int = 1
}