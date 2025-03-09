package com.akakou.zkbanandroid.store

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AuthLogDao {
    @Query("SELECT * FROM auth_log")
    fun getAll(): List<AuthLog>

    @Query("SELECT * FROM auth_log WHERE domain = :domain AND date = :date")
    fun getAllByDomainAndDate(domain: String, date: Int): List<AuthLog>

    @Insert
    fun insertAll(vararg authLog: AuthLog)

    @Delete
    fun delete(authLog: AuthLog)
}