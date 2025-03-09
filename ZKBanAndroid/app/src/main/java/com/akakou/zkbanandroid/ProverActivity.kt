package com.akakou.zkbanandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.akakou.proverkit.ProverActivityHelper


class ProverActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val helper = ProverActivityHelper(manager)
        helper.start(this@ProverActivity)
    }
}

