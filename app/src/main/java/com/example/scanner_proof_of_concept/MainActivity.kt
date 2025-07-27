package com.example.scanner_proof_of_concept

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ProductScannerApp() }
    }
}

@Composable
fun ProductScannerApp() {
    var upc by remember { mutableStateOf("") }
    var product by remember { mutableStateOf<ProductInfo?>(null) }
    var loading by remember { mutableStateOf(false) }
    var showCameraScanner by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Honeywell scanner setup
    val scannerManager = remember {
        HoneywellScannerManager(context) { scannedCode ->
            upc = scannedCode
        }
    }

    DisposableEffect(Unit) {
        onDispose { scannerManager.release() }
    }

    if (showCameraScanner) {
        BarcodeScannerScreen(onBarcodeScanned = { scannedCode ->
            upc = scannedCode
            showCameraScanner = false
        })
    } else {
        Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
            OutlinedTextField(
                value = upc,
                onValueChange = { upc = it },
                label = { Text("Enter or Scan UPC") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(onClick = { showCameraScanner = true }) {
                Text("Scan with Camera")
            }

            Spacer(Modifier.height(16.dp))

            Button(onClick = {
                loading = true
                scope.launch(Dispatchers.IO) {
                    val result = fetchProductInfo(upc)
                    product = result
                    loading = false
                }
            }) {
                Text("Get Product Info")
            }

            Spacer(Modifier.height(16.dp))

            if (loading) {
                CircularProgressIndicator()
            } else if (product != null) {
                Text("UPC: ${product!!.upc}")
                Text("Name: ${product!!.name}")
                Text("Description: ${product!!.description}")
                Text("Price: $${product!!.price}")
            }
        }
    }
}