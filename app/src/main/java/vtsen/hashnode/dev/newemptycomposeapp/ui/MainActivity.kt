package com.exemple.nfc // Vérifie que ce nom correspond à ton projet

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.*

class MainActivity : ComponentActivity(), NfcAdapter.ReaderCallback {

    private var nfcAdapter: NfcAdapter? = null
    // Une "variable d'état" que Compose va surveiller pour mettre à jour l'écran
    private var scanResult by mutableStateOf("Approchez une carte...")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        setContent {
            // C'est ici qu'on définit l'interface sans XML
            Text(text = scanResult)
        }
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableReaderMode(this, this, 
            NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B, null)
    }

    override fun onTagDiscovered(tag: Tag?) {
        val id = tag?.id?.joinToString("") { "%02X".format(it) } ?: "Erreur"
        // On met à jour le texte à l'écran
        scanResult = "UUID détecté : $id"
    }
}
