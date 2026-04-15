package com.akakou.zkbanandroid

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.akakou.proverkit.LaunchActivity
import com.akakou.proverkit.identification.phone_auth.PhoneNumberAuthActivity

class RegisterActivity : LaunchActivity<Long>(Prover(), PhoneNumberAuthActivity::class.java) {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Toast.makeText(this, "hi", Toast.LENGTH_LONG).show()
    }
}
