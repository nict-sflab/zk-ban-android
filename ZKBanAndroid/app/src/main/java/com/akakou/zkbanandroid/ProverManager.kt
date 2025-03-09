package com.akakou.zkbanandroid

import com.akakou.proverkit.AbstractProverManager

val manager = ProverManager()

class ProverManager: AbstractProverManager() {
    override fun createProver(uri: android.net.Uri): Prover {
        return Prover(uri)
    }

    override fun register(): Unit {

    }
}