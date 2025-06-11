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

            val gpk = Zkbancrypto.fetchGroupPublicKey("http://localhost:8080/group-public-key")
            Toast.makeText(this@MainActivity, gpk.toString(Charsets.UTF_8), Toast.LENGTH_LONG).show()

            val sign = Zkbancrypto.sign("msg".toByteArray(Charsets.UTF_8) , 1, signer, gpk)
            Toast.makeText(this@MainActivity, sign.toString(Charsets.UTF_8), Toast.LENGTH_LONG).show()

            val rl = Zkbancrypto.fetchRevocationList("http://localhost:8080/revocation-list")
            Toast.makeText(this@MainActivity, rl.toString(Charsets.UTF_8), Toast.LENGTH_LONG).show()

            Thread.sleep(5000)

            val update = Zkbancrypto.requestUpdate(signer, rl, gpk, "http://localhost:8080/update-credential" )
            Toast.makeText(this@MainActivity, update.toString(Charsets.UTF_8), Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()

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
