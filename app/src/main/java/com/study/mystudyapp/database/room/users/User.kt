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
    var user_permission: String? = null,
    var user_created_at: String? = null,
    var user_updated_at: String? = null,
    var user_currency: String? = null,
    var user_qualification: String? = null,
    var user_main_salary: String? = null,
    var user_allowance: String? = null,
    var user_company_group: String? = null,
    var user_company: String? = null,
    var user_branch_group: String? = null,
    var user_branch: String? = null,
    var user_appointment_date: String? = null,
    var user_job_title: String? = null,
    var user_local: String? = null
) {
    @PrimaryKey(autoGenerate = false)
    var uid: Int = CURRENT_USER_ID
}
