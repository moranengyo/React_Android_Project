package com.example.kotiln_tpj_yesim

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class QRScannerActivity : AppCompatActivity() {

    private lateinit var capture: CaptureManager
    private lateinit var barcodeScannerView: DecoratedBarcodeView

    // ZXing 스캐너의 결과를 처리하는 콜백 함수
    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            // 스캔한 QR 코드의 내용이 result.contents에 담김
            val result: List<String> = result.contents.split("/")
            val detailIntent = Intent(this, InOutActivity::class.java)
            detailIntent.putExtra("itemId", result[0])
            detailIntent.putExtra("purchaseId", result[1])
            startActivity(detailIntent)

        } else {
            // 스캔이 취소되었을 때
            Toast.makeText(this, "Scan canceled", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrscanner)

        supportActionBar?.hide()

        barcodeScannerView = findViewById(R.id.barcode_scanner)

        capture = CaptureManager(this, barcodeScannerView)
        capture.initializeFromIntent(intent, savedInstanceState)
        capture.decode()

        barcodeScannerView.decodeSingle(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                if (result != null && result.text != null) {
                    val resultData = result.text.split("/")
                    val detailIntent = Intent(this@QRScannerActivity, InOutActivity::class.java)
                    detailIntent.putExtra("itemId", resultData[0])
                    detailIntent.putExtra("purchaseId", resultData[1])
                    startActivity(detailIntent)
                }
            }
        })

        val cancelButton: Button = findViewById(R.id.button_cancel)
        cancelButton.setOnClickListener {
            capture.onDestroy()  // 스캔 종료
            finish()  // 액티비티 종료
        }

    }

    override fun onResume() {
        super.onResume()
        capture.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}