package com.example.kotiln_tpj_yesim


import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kotiln_tpj_yesim.databinding.ActivityMainBinding
import com.example.kotiln_tpj_yesim.fragments.HomeFragment
import com.example.kotiln_tpj_yesim.fragments.RequestFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import android.content.Intent
import android.view.Menu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.kotiln_tpj_yesim.db.DBHelper

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.background = null

        val menu = binding.bottomNavigation.menu

        var role = 0;

        val dbHelper = DBHelper(this);
        val userRole = dbHelper.getUserInfo()!!.role

        when (userRole){
            "ROLE_MANAGER" -> role = 2
            "ROLE_SENIOR_MANAGER" -> role = 3
            else -> role = 1
        }

        if (role == 1) {
            val searchId = R.id.navigation_search
            val searchItem = menu.findItem(searchId)
            menu.removeItem(searchId)
            menu.add(Menu.NONE, searchId, 3, searchItem.title).setIcon(searchItem.icon)

            menu.removeItem(R.id.navigation_notification)
            menu.removeItem(R.id.navigation_history)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, HomeFragment(role, false))
                .commit()
        }

        val bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_statistics -> {
                    loadFragment(HomeFragment(role, false))
                    true
                }

                R.id.navigation_search -> {
                    supportActionBar?.title = "비품 조회"
                    loadFragment(SearchFragment())
                    true
                }

                R.id.navigation_notification -> {
                    supportActionBar?.title = if (role == 2) "적정수량 미만 비품" else "미결재 조회"
                    val fragment = RequestFragment()
                    val bundle = Bundle()
                    bundle.putBoolean("isNotification", true)
                    fragment.arguments = bundle
                    loadFragment(fragment)
                    true
                }

                R.id.navigation_history -> {
                    supportActionBar?.title = "결재 내역 목록"
                    val fragment = RequestFragment()
                    loadFragment(fragment)
                    true
                }

                else -> false
            }
        }

        // QR 코드 버튼 클릭 이벤트
        val fabQR = findViewById<FloatingActionButton>(R.id.fab_qr)
        fabQR.setOnClickListener {
//            startQRScanner()
            val intent = Intent(this, QRScannerActivity::class.java)
            startActivity(intent)

//            val detailIntent = Intent(this, InOutActivity::class.java)
//            startActivity(detailIntent)
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
//            } else {
//                startQRScanner()
//            }

        }

        val fabLogin = binding.fabLogout
        fabLogin.setOnClickListener {
            dbHelper.deleteAllAuth()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()  // 뒤로 가기 버튼 동작 처리
        return true
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 100) {
//            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                // 권한이 승인된 경우 QR 스캐너 시작
//                Toast.makeText(this, "카메라 권한이 확인", Toast.LENGTH_SHORT).show()
//                startQRScanner()
//            } else {
//                Toast.makeText(this, "카메라 권한이 필요합니다", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    // ZXing 스캐너의 결과를 처리하는 콜백 함수
//    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
//        if (result.contents != null) {
//            // 스캔한 QR 코드의 내용이 result.contents에 담김
////            Toast.makeText(this, "Scanned: ${result.contents}", Toast.LENGTH_LONG).show()
//            val result: List<String> = result.contents.split("/")
//            val detailIntent = Intent(this, InOutActivity::class.java)
//            detailIntent.putExtra("itemId", result[0])
//            detailIntent.putExtra("purchaseId", result[1])
//            startActivity(detailIntent)
//
//        } else {
//            // 스캔이 취소되었을 때
//            Toast.makeText(this, "Scan canceled", Toast.LENGTH_SHORT).show()
//        }
//    }

//    private fun startQRScanner() {
//        val options = ScanOptions()
//        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
//        options.setPrompt("QR 코드를 스캔하세요")
////        options.setCameraId(0)  // 후면 카메라
//        options.setBeepEnabled(true)
//        options.setOrientationLocked(false)
//        options.setCaptureActivity(CaptureActivity::class.java)
//        barcodeLauncher.launch(options)
//    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

}