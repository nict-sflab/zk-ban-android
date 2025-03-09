package com.akakou.zkbanandroid

import android.net.Uri
import com.akakou.proverkit.AbstractProver

class Prover(uri: Uri): AbstractProver(uri) {
    override fun needUserCheck(): Boolean {
        return false
    }

    override fun prove(): String {
        return "hi! this is zk-ban"
    }
}