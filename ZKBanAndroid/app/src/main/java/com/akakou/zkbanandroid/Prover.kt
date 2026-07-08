package com.akakou.zkbanandroid

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.akakou.proverkit.AbstractProver
import zkbancrypto.Zkbancrypto

class Prover(): AbstractProver<Long>() {
    val gmURLBase   = "http://10.96.204.121:8080"
    val verifierURL = "http://10.96.204.121:8000/verify"

    override suspend fun register(context: Context) {
        Log.d("zk-ban", "${Zkbancrypto.getPeriod()}")

        val preferences = context.getSharedPreferences("default", Context.MODE_PRIVATE)
        val idToken = preferences.getString("idToken", "")!!.toByteArray()

        val gpkRaw = Zkbancrypto.fetchGroupPublicKey("${gmURLBase}/group-public-key")
        val signerRaw = Zkbancrypto.requestJoin(idToken.toString(Charsets.UTF_8), "${gmURLBase}/issue-credential")
        val gpk = java.util.Base64.getEncoder().encode(gpkRaw)
        val signer = java.util.Base64.getEncoder().encode(signerRaw)

        preferences.edit()
            .putString("credential", signer.toString(Charsets.UTF_8))
            .putString("gpk", gpk.toString(Charsets.UTF_8)).commit()

        UpdateWorker.run(context)
    }

    override suspend fun prove(uri: String, context: Context, t: Long) {
        val preferences = context.getSharedPreferences("default", Context.MODE_PRIVATE)
        val gpk = preferences.getString("gpk", "")!!.toByteArray()
        val signer = preferences.getString("credential", "")!!.toByteArray()

        val gpkRaw = java.util.Base64.getDecoder().decode(gpk)
        val signerRaw = java.util.Base64.getDecoder().decode(signer)

        val u = uri.toUri()
        val message = u.getQueryParameter("message")

        Log.d("zk-ban", signerRaw.toString(Charsets.UTF_8))
        Log.d("zk-ban", gpkRaw.toString(Charsets.UTF_8))

        Zkbancrypto.sign(message!!.toByteArray(Charsets.UTF_8) , t, signerRaw, gpkRaw, verifierURL)
    }

    fun update(context: Context) {
        Log.d("zk-ban", "${Zkbancrypto.getPeriod()}")
        val preferences = context.getSharedPreferences("default", Context.MODE_PRIVATE)
        val gpk = preferences.getString("gpk", "")!!.toByteArray()
        val signer = preferences.getString("credential", "")!!.toByteArray()
        val gpkRaw = java.util.Base64.getDecoder().decode(gpk)
        val signerRaw = java.util.Base64.getDecoder().decode(signer)
        val rl = Zkbancrypto.fetchRevocationList(signerRaw, "${gmURLBase}/revocation-list")
        val newSignerRaw = Zkbancrypto.requestUpdate(signerRaw, rl, gpkRaw, "${gmURLBase}/update-credential")
        val newSigner = java.util.Base64.getEncoder().encode(newSignerRaw)

        if (newSigner.contentEquals(signer)) return
        Log.d("zk-ban", signer.toString(Charsets.UTF_8))
        preferences.edit().putString("credential", newSigner.toString(Charsets.UTF_8)).commit()
    }
}

