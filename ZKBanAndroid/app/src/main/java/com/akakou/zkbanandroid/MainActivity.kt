package com.akakou.zkbanandroid

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import zkbancrypto.Zkbancrypto
import java.io.File

val KEY_NAME = "update_prover-sample.bin"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        val basePath = Environment.getExternalStorageDirectory()
        val path = basePath.path + "/zk-ban/"
        Zkbancrypto.setPath(path)
        Zkbancrypto.setPeriodUnit(120000000000)


        if (!Environment.isExternalStorageManager()) {
            val intent = Intent(
                ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
            return
        }

        val dir = File(path)
        val file = File(dir, KEY_NAME)

        if (!file.exists())
        {
            Toast.makeText(this, "Loading Key...It takes minutes...", Toast.LENGTH_LONG).show()
            Zkbancrypto.importKeys()
            Toast.makeText(this, "Loading Completed!", Toast.LENGTH_SHORT).show()
        }

        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}