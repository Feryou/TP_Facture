package com.example.khelifidacheuxfacture

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.khelifidacheuxfacture.ui.theme.KhelifiDacheuxFactureTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KhelifiDacheuxFactureTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(onLoginSuccess = {
                        startActivity(Intent(this, FactureActivity::class.java))
                    })
                }
            }
        }
    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Veuillez vous connecter", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Login") },
            modifier = Modifier.padding(top = 16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe") },
            modifier = Modifier.padding(top = 16.dp),
            visualTransformation = PasswordVisualTransformation(),
        )

        Button(onClick = {
            if (login == "etudiant" && password == "Azerty") {
                onLoginSuccess()
            } else {
                Toast.makeText(context, "Identifiants incorrects", Toast.LENGTH_SHORT).show()
            }
        }, modifier = Modifier.padding(top = 16.dp)) {
            Text("Connexion")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KhelifiDacheuxFactureTheme {
        LoginScreen(onLoginSuccess = { /* Message de debug si besoin */ })
    }
}
