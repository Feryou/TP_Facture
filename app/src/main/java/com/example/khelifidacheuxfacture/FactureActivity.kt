package com.example.khelifidacheuxfacture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

class FactureActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FactureForm()
        }
    }
}

@Composable
fun FactureForm() {
    var quantite by remember { mutableStateOf("") }
    var prixUnitaire by remember { mutableStateOf("") }
    var montantHT by remember { mutableStateOf("") }
    var tauxTVA by remember { mutableStateOf("") }
    var estFidele by remember { mutableStateOf(false) }
    var remise by remember { mutableStateOf("") }

    val quantiteNum = quantite.toDoubleOrNull() ?: 0.0
    val prixUnitaireNum = prixUnitaire.toDoubleOrNull() ?: 0.0
    montantHT = (quantiteNum * prixUnitaireNum).toString()

    val isFormValid = quantite.isNotEmpty() && prixUnitaire.isNotEmpty() && montantHT.isNotEmpty() && tauxTVA.isNotEmpty() && (!estFidele || remise.isNotEmpty())

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = quantite,
            onValueChange = {
                quantite = it
                val quantNum = it.toDoubleOrNull() ?: 0.0
                montantHT = (quantNum * prixUnitaireNum).toString()
            },
            label = { Text("Quantité") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = prixUnitaire,
            onValueChange = {
                prixUnitaire = it
                val prixUnitNum = it.toDoubleOrNull() ?: 0.0
                montantHT = (quantiteNum * prixUnitNum).toString()
            },
            label = { Text("Prix Unitaire") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = montantHT,
            onValueChange = { montantHT = it },
            label = { Text("Montant HT") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            readOnly = true
        )

        OutlinedTextField(value = tauxTVA, onValueChange = { tauxTVA = it }, label = { Text("Taux TVA") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

        FideliteRadioButtons(estFidele = estFidele, onFideliteChanged = { estFidele = it })

        if (estFidele) {
            OutlinedTextField(value = remise, onValueChange = { remise = it }, label = { Text("Remise") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        }

        val context = LocalContext.current

        Button(onClick = {
            val quantiteNum = quantite.toDoubleOrNull() ?: 0.0
            val prixUnitaireNum = prixUnitaire.toDoubleOrNull() ?: 0.0
            val montantHTNum = montantHT.toDoubleOrNull() ?: 0.0
            val tauxTVANum = tauxTVA.toDoubleOrNull() ?: 0.0
            val remiseNum = if (estFidele) remise.toDoubleOrNull() ?: 0.0 else 0.0

            val montantTTC = (montantHTNum + (montantHTNum * tauxTVANum / 100)) * (1 - remiseNum / 100)

            val intent = android.content.Intent(context, ResultatActivity::class.java)
            intent.putExtra("montantTTC", montantTTC.toString())
            context.startActivity(intent)
        }, enabled = isFormValid, modifier = Modifier.padding(top = 8.dp)) {
            Text("Calculer TTC")
        }

        Button(onClick = {
            quantite = ""
            prixUnitaire = ""
            montantHT = ""
            tauxTVA = ""
            estFidele = false
            remise = ""
        }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Remise à zéro")
        }
    }
}

@Composable
fun FideliteRadioButtons(estFidele: Boolean, onFideliteChanged: (Boolean) -> Unit) {
    Row {
        RadioButton(
            selected = !estFidele,
            onClick = { onFideliteChanged(false) }
        )
        Text(
            text = "Non Fidèle",
            modifier = Modifier
                .clickable(onClick = { onFideliteChanged(false) })
                .padding(end = 8.dp)
        )
        RadioButton(
            selected = estFidele,
            onClick = { onFideliteChanged(true) }
        )
        Text(
            text = "Fidèle",
            modifier = Modifier.clickable(onClick = { onFideliteChanged(true) })
        )
    }
}
