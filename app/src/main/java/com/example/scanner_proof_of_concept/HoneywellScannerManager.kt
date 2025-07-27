package com.example.scanner_proof_of_concept

import android.content.Context
import com.honeywell.aidc.AidcManager
import com.honeywell.aidc.BarcodeReadEvent

class HoneywellScannerManager(context: Context, private val onScanned: (String) -> Unit) {
    private var aidcManager: AidcManager? = null
    private var barcodeReader: com.honeywell.aidc.BarcodeReader? = null

    init {
        AidcManager.create(context) { manager ->
            aidcManager = manager
            barcodeReader = manager.createBarcodeReader()
            barcodeReader?.apply {
                addBarcodeListener { event: BarcodeReadEvent ->
                    onScanned(event.barcodeData)
                }
                claim()
            }
        }
    }

    fun release() {
        barcodeReader?.release()
        aidcManager?.close()
    }
}