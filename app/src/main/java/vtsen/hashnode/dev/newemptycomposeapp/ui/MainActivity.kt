package com.tonprojet.famoconfc

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.widget.TextView

// On implémente NfcAdapter.ReaderCallback pour lire en arrière-plan (idéal Famoco)
class MainActivity : Activity(), NfcAdapter.ReaderCallback {

    private var nfcAdapter: NfcAdapter? = null
    private lateinit var tvStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if (nfcAdapter == null) {
            tvStatus.text = "Erreur : NFC non disponible sur cet appareil."
        }
    }

    // Quand l'application est à l'écran, on allume le lecteur NFC
    override fun onResume() {
        super.onResume()
        val flags = NfcAdapter.FLAG_READER_NFC_A or 
                    NfcAdapter.FLAG_READER_NFC_B or 
                    NfcAdapter.FLAG_READER_NFC_F or 
                    NfcAdapter.FLAG_READER_NFC_V
        
        nfcAdapter?.enableReaderMode(this, this, flags, null)
    }

    // Quand on quitte l'application, on coupe le lecteur pour économiser la batterie
    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableReaderMode(this)
    }

    // Cette fonction se déclenche automatiquement quand une carte touche le Famoco
    override fun onTagDiscovered(tag: Tag?) {
        tag?.let {
            // Extraction de l'UUID (tableau d'octets)
            val uidBytes = it.id
            // Conversion en format Hexadécimal lisible (ex: 04A2B3...)
            val uidHex = uidBytes.joinToString("") { byte -> "%02X".format(byte) }

            // Mise à jour de l'interface graphique (doit se faire sur le thread principal)
            runOnUiThread {
                tvStatus.text = "Carte détectée !\n\nUUID : $uidHex\n\nPrêt à envoyer au script Python..."
            }
            
            // TODO: C'est ici que tu ajouteras la requête HTTP (POST) vers ton web app Python
        }
    }
}
