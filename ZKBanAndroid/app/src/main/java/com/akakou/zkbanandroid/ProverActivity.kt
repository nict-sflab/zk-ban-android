package com.akakou.zkbanandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.room.Room.databaseBuilder
import com.akakou.proverkit.ProverActivityHelper
import com.akakou.zkbanandroid.store.AppDatabase


class ProverActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "prover"
        ).build()

        manager = ProverManager(db)


        val helper = ProverActivityHelper(manager!!)
        helper.start(this@ProverActivity)
    }
}

