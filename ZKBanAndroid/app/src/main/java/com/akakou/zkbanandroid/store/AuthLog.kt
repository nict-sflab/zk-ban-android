package com.akakou.zkbanandroid.store

import androidx.room.*
import androidx.room.PrimaryKey

@Entity(tableName = "auth_log")
data class AuthLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "domain") val domain: String?,
    @ColumnInfo(name = "counter") val counter: Int?,
    @ColumnInfo(name = "date") val date: Int?,
)


