package com.akakou.zkbanandroid

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import android.util.Log
import androidx.core.net.toUri
import com.akakou.proverkit.AbstractProver
import zkbancrypto.Zkbancrypto

class Prover(): AbstractProver<Long>() {
    val gmURLBase   = "http://10.130.166.96:18080"
    val verifierURL = "http://10.130.166.96:18000/verify"

    suspend fun setupPath(context: Context) {
        val basePath = Environment.getExternalStorageDirectory()
        val path = basePath.path + "/zk-ban/"
        Zkbancrypto.setPath(path)
    }

    override suspend fun register(context: Context) {
        setupPath(context)

        Zkbancrypto.importKeys()

        val preferences = context.getSharedPreferences("default", Context.MODE_PRIVATE)
        val idToken = preferences.getString("idToken", "")!!.toByteArray()

        val gpkRaw = Zkbancrypto.fetchGroupPublicKey("${gmURLBase}/group-public-key")
        val signerRaw = Zkbancrypto.requestJoin(idToken.toString(Charsets.UTF_8), "${gmURLBase}/issue-credential")
        val gpk = java.util.Base64.getEncoder().encode(gpkRaw)
        val signer = java.util.Base64.getEncoder().encode(signerRaw)

        preferences.edit().putString("credential", signer.toString(Charsets.UTF_8)).apply()
        preferences.edit().putString("gpk", gpk.toString(Charsets.UTF_8)).apply()

        UpdateWorker.run(context)
    }

    override suspend fun prove(uri: String, context: Context, t: Long) {
        setupPath(context)

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

    suspend fun update(context: Context) {
        setupPath(context)

        val preferences = context.getSharedPreferences("default", Context.MODE_PRIVATE)
        val gpk = preferences.getString("gpk", "")!!.toByteArray()
        val signer = preferences.getString("credential", "")!!.toByteArray()
        val gpkRaw = java.util.Base64.getDecoder().decode(gpk)
        val signerRaw = java.util.Base64.getDecoder().decode(signer)
        val rl = Zkbancrypto.fetchRevocationList(signerRaw, "${gmURLBase}/revocation-list")
        val newSignerRaw = Zkbancrypto.requestUpdate(signerRaw, rl, gpkRaw, "${gmURLBase}/update-credential")
        val newSigner = java.util.Base64.getEncoder().encode(newSignerRaw)

        Log.d("zk-ban", signer.toString(Charsets.UTF_8))
        preferences.edit().putString("credential", newSigner.toString(Charsets.UTF_8)).apply()
    }
}

