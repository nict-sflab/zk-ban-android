package com.akakou.zkbanandroid

import com.akakou.proverkit.AbstractProverManager
import com.akakou.zkbanandroid.store.AppDatabase
import com.akakou.zkbanandroid.store.AuthLogDao

var manager: ProverManager? = null

class ProverManager(val db : AppDatabase): AbstractProverManager() {
    override fun createProver(uri: android.net.Uri): Prover {
        return Prover(db, uri)
    }

    override suspend fun register(): Unit {

    }
}