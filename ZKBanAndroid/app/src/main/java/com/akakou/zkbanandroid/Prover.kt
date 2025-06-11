package com.akakou.zkbanandroid

import zkbancrypto.Zkbancrypto
import android.content.SharedPreferences
import android.net.Uri
import com.akakou.proverkit.AbstractProver
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Prover(uri: Uri, val preference: SharedPreferences): AbstractProver(uri) {
    var dateString : String = ""
    var tag : String = ""
    var counter = 0

    fun needUserCheck() : Boolean{
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        dateString = today.format(formatter)
        tag = dateString + uri.toString()
        val result = preference.getBoolean(tag, false)

        return result
    }

    override suspend fun prove(): String {
        preference.edit().putBoolean(tag, true).apply()

        return "hi! this is zk-ban! psuedonym ${counter}!"
    }
}

