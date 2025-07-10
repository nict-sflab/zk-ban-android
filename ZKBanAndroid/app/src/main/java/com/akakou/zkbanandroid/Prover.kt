package com.akakou.zkbanandroid

import android.content.Context
import android.util.Log
import com.akakou.proverkit.AbstractProver
import zkbancrypto.Zkbancrypto

class Prover(): AbstractProver<Long>() {
    val baseURL = "http://192.168.137.1:8080"

    override suspend fun register(context: Context) {
        Zkbancrypto.setConstantPeriodForDebug(1.toLong())
        val preferences = context.getSharedPreferences("default", Context.MODE_PRIVATE)
        val idToken = preferences.getString("idToken", "")!!.toByteArray()

        val signer = Zkbancrypto.requestJoin(idToken.toString(Charsets.UTF_8), "${baseURL}/issue-credential")
        val gpkRaw = Zkbancrypto.fetchGroupPublicKey("${baseURL}/group-public-key")
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

        Log.d("zk-ban", credential.toString(Charsets.UTF_8))
        Log.d("zk-ban", gpkRaw.toString(Charsets.UTF_8))

        Zkbancrypto.sign("msg".toByteArray(Charsets.UTF_8) , t, credential, gpkRaw, uri)
    }

    suspend fun update(context: Context) {
        val preferences = context.getSharedPreferences("default", Context.MODE_PRIVATE)
        val gpk = preferences.getString("gpk", "")!!.toByteArray()
        val gpkRaw = java.util.Base64.getDecoder().decode(gpk)

        val credential = preferences.getString("credential", "")!!.toByteArray()
        val rl = Zkbancrypto.fetchRevocationList(credential, "${baseURL}/revocation-list")
        val signer = Zkbancrypto.requestUpdate(credential, rl, gpkRaw, "${baseURL}/update-credential")

        Log.d("zk-ban", signer.toString(Charsets.UTF_8))
        preferences.edit().putString("credential", signer.toString(Charsets.UTF_8)).apply()
    }
}

