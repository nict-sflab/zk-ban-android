package com.akakou.zkbanandroid

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.akakou.proverkit.CredentialViewActivity
import com.akakou.proverkit.IdentificationUtils
import com.akakou.proverkit.identification.phone_auth.PhoneNumberAuthActivity
import com.akakou.zkbanandroid.ui.theme.ZKBanAndroidTheme
import zkbancrypto.Zkbancrypto

class MainActivity : ComponentActivity() {
    override fun onResume() {
        super.onResume()

        try {
            val signer = Zkbancrypto.requestJoin("idToken", "http://localhost:8080/issue-credential")
            Toast.makeText(this@MainActivity, signer.toString(Charsets.UTF_8), Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            throw e
        }


        val utils = IdentificationUtils(this@MainActivity)
        if (utils.hasIdentified()) {
            val intent = Intent(this@MainActivity, CredentialViewActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            utils.proveIdentity(PhoneNumberAuthActivity::class.java)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intent = Intent(this@MainActivity, CredentialViewActivity::class.java)
        startActivity(intent)
    }
}
