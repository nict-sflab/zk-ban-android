package com.akakou.zkbanandroid

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import com.akakou.proverkit.AbstractProver
import zkbancrypto.Zkbancrypto

class Prover(): AbstractProver<Long>() {
    val gmURLBase = "http://192.168.137.1:8080"
    val verifierURL = "http://192.168.137.1:8000/verify"

    override suspend fun register(context: Context) {
        val preferences = context.getSharedPreferences("default", Context.MODE_PRIVATE)
        val idToken = preferences.getString("idToken", "")!!.toByteArray()

        val signer = Zkbancrypto.requestJoin(idToken.toString(Charsets.UTF_8), "${gmURLBase}/issue-credential")
        val gpkRaw = Zkbancrypto.fetchGroupPublicKey("${gmURLBase}/group-public-key")
        val gpk = java.util.Base64.getEncoder().encode(gpkRaw)

        preferences.edit().putString("credential", signer.toString(Charsets.UTF_8)).apply()
        preferences.edit().putString("gpk", gpk.toString(Charsets.UTF_8)).apply()

        UpdateWorker.run(context)
    }

    override suspend fun prove(uri: String, context: Context, t: Long) {
        val preferences = context.getSharedPreferences("default", Context.MODE_PRIVATE)
        val gpk = preferences.getString("gpk", "")!!.toByteArray()
        val gpkRaw = java.util.Base64.getDecoder().decode(gpk)
        val credential = preferences.getString("credential", "")!!.toByteArray()

        val u = uri.toUri()
        val message = u.getQueryParameter("message")

        Log.d("zk-ban", credential.toString(Charsets.UTF_8))
        Log.d("zk-ban", gpkRaw.toString(Charsets.UTF_8))

        Zkbancrypto.sign(message!!.toByteArray(Charsets.UTF_8) , t, credential, gpkRaw, verifierURL)
    }

    suspend fun update(context: Context) {
        val preferences = context.getSharedPreferences("default", Context.MODE_PRIVATE)
        val gpk = preferences.getString("gpk", "")!!.toByteArray()
        val gpkRaw = java.util.Base64.getDecoder().decode(gpk)

        val credential = preferences.getString("credential", "")!!.toByteArray()
        val rl = Zkbancrypto.fetchRevocationList(credential, "${gmURLBase}/revocation-list")
        val signer = Zkbancrypto.requestUpdate(credential, rl, gpkRaw, "${gmURLBase}/update-credential")

        Log.d("zk-ban", signer.toString(Charsets.UTF_8))
        preferences.edit().putString("credential", signer.toString(Charsets.UTF_8)).apply()
    }
}

