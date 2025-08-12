package com.akakou.zkbanandroid

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akakou.proverkit.identification.phone_auth.PhoneNumberAuthActivity
import com.akakou.proverkit.LaunchActivity
import zkbancrypto.Zkbancrypto


class MainActivity : LaunchActivity<Long>(Prover(), PhoneNumberAuthActivity::class.java) {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }
}

//val signer = Zkbancrypto.requestJoin("idToken", "http://localhost:8080/issue-credential")
//Toast.makeText(this@MainActivity, signer.toString(Charsets.UTF_8), Toast.LENGTH_SHORT).show()
//
//val gpk = Zkbancrypto.fetchGroupPublicKey("http://localhost:8080/group-public-key")
//Toast.makeText(this@MainActivity, gpk.toString(Charsets.UTF_8), Toast.LENGTH_SHORT).show()
//
//val signature = Zkbancrypto.sign("msg".toByteArray(Charsets.UTF_8) , 1, signer, gpk)
//Toast.makeText(this@MainActivity, signature.toString(Charsets.UTF_8), Toast.LENGTH_SHORT).show()
//
//val rl = Zkbancrypto.fetchRevocationList("http://localhost:8080/revocation-list")
//Toast.makeText(this@MainActivity, rl.toString(Charsets.UTF_8), Toast.LENGTH_SHORT).show()
//
//
//val url = "http://localhost:8000/verify"
//    .toUri()
//    .buildUpon()
//    .appendQueryParameter("signature", signature.toString(Charsets.UTF_8))
//    .build()
//
//val intent = Intent(Intent.ACTION_VIEW, url)
//startActivity(intent)
//
//
//Zkbancrypto.setConstantPeriodForDebug(10)
//Thread.sleep(5000)
//
//val update = Zkbancrypto.requestUpdate(signer, rl, gpk, "http://localhost:8080/update-credential" )
//Toast.makeText(this@MainActivity, update.toString(Charsets.UTF_8), Toast.LENGTH_LONG).show()
