package com.akakou.zkbanandroid

import android.content.SharedPreferences
import android.net.Uri
import com.akakou.proverkit.AbstractProver

class Prover(): AbstractProver<Pass>() {
    override suspend fun prove(uri: Uri, preference: SharedPreferences, t: Pass): String {
        return "hi! this is zk-ban! psuedonym with ${t.a}"
    }

    override suspend fun register(preferences: SharedPreferences) {
        preferences.edit().putString("credential", "this is credential").apply()
    }
}

