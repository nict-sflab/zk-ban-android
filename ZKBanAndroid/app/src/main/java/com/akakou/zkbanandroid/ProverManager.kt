package com.akakou.zkbanandroid

import android.content.SharedPreferences
import com.akakou.proverkit.AbstractProverManager

var manager: ProverManager? = null

class ProverManager(val preference : SharedPreferences): AbstractProverManager() {
    override fun createProver(uri: android.net.Uri): Prover {
        return Prover(uri, preference)
    }

    override suspend fun register(): Unit {
    }
}