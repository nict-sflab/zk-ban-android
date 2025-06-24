package com.akakou.zkbanandroid

import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.akakou.proverkit.AbstractProver
import zkbancrypto.Zkbancrypto

class Prover(): AbstractProver<Pass>() {
    val baseURL = "http://localhost:8080"

    override suspend fun prove(uri: Uri, preferences: SharedPreferences, t: Pass) {
        val gpk = preferences.getString("gpk", "")!!.toByteArray()
        val gpkRaw = java.util.Base64.getDecoder().decode(gpk)
        val credential = preferences.getString("credential", "")!!.toByteArray()

        Log.d("Debug", credential.toString(Charsets.UTF_8))
        Log.d("Debug", gpkRaw.toString(Charsets.UTF_8))

        Zkbancrypto.sign("msg".toByteArray(Charsets.UTF_8) , t.a.toLong(), credential, gpkRaw)
    }

    override suspend fun register(preferences: SharedPreferences) {
        val idToken = preferences.getString("idToken", "")!!.toByteArray()

        val signer = Zkbancrypto.requestJoin(idToken.toString(Charsets.UTF_8), "${baseURL}/issue-credential")
        val gpkRaw = Zkbancrypto.fetchGroupPublicKey("${baseURL}/group-public-key")
        val gpk = java.util.Base64.getEncoder().encode(gpkRaw)

        preferences.edit().putString("credential", signer.toString(Charsets.UTF_8)).apply()
        preferences.edit().putString("gpk", gpk.toString(Charsets.UTF_8)).apply()
    }
}

