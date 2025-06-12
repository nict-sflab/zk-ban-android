package com.akakou.zkbanandroid

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.core.util.Function
import kotlin.run


class ProverActivity : com.akakou.proverkit.ProverActivity<Pass>(Prover()) {
    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.scheme = "http"
        super.proofQuery = "signature"
        super.onCreate(savedInstanceState)
        setContent { ProverUI{ counter ->
            super.run(Pass(counter))
        } }
    }
}

@Composable
fun ProverUI(callback: (Int) -> Any) {
    Column(modifier = Modifier.padding(8.dp)) {
        val message = "Which pseudonym do you choose?\n" +
                "NOTE: If you don't delete the cookies on your browser before,\n" +
                "web site track users using cookies"

        Text(
            text = message,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        var expanded by remember { mutableStateOf(false) }
        val items = listOf("Taro Pseudonym 1 (Default)", "Jiro Pseudonym 2", "Saburo Pseudonym 3")
        var selectedItem by remember { mutableStateOf(items[1]) }

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedItem,
                onValueChange = {},
                label = { Text("Please choose") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                readOnly = true
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            selectedItem = item
                            expanded = false
                            val counter = items.indexOf(item)
                            callback(counter)
                        },
                        text = { Text(text = item) }
                    )
                }
            }
        }
    }
}
