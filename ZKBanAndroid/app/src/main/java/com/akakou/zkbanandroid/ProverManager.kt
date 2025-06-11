package com.akakou.zkbanandroid

import android.content.Context
import android.content.SharedPreferences
import com.akakou.proverkit.AbstractProverManager
import zkbancrypto.Zkbancrypto

var manager: ProverManager? = null

class ProverManager(val preference : SharedPreferences): AbstractProverManager() {
    override fun createProver(uri: android.net.Uri): Prover {
        return Prover(uri, preference)
    }

    override suspend fun register(): Unit {
        val idToken = preference.getString("idToken", "")!!
        try {
            Zkbancrypto.requestJoin(idToken, "localhost:8080")
        } catch (e: Exception) {
            throw e
        }
    }
}