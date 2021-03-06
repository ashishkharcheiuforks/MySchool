package com.nandra.myschool.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val first_name: String = "",
    val last_name: String = "",
    val id: String = "",
    val role: String = "",
    val profile_picture_storage_path: String = ""
)