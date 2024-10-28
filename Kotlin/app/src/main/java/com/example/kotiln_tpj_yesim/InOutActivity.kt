package com.example.kotiln_tpj_yesim

import android.content.Context
import android.content.Intent
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kotiln_tpj_yesim.Retrofit.RetrofitClient
import com.example.kotiln_tpj_yesim.Retrofit.RetrofitFunc
import com.example.kotiln_tpj_yesim.databinding.ActivityInOutBinding
import com.example.kotiln_tpj_yesim.db.DBHelper
import com.example.kotiln_tpj_yesim.dto.ItemDto
import com.example.kotiln_tpj_yesim.dto.PurchaseDto
import com.example.kotiln_tpj_yesim.dto.UsageDto
import com.example.kotiln_tpj_yesim.dto.UserDto
import com.google.gson.Gson
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Console
import java.time.LocalDateTime

class InOutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInOutBinding
    private lateinit var currentItem: ItemDto
    private lateinit var currentPurchase: PurchaseDto

    private var role: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "입출고 처리"

        binding = ActivityInOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val itemId = intent.getStringExtra("itemId")
        val purchaseId = intent.getStringExtra("purchaseId")

        val dbHelper = DBHelper(this)
        role = dbHelper.getIntRole()

        binding.cardViewInStock2.visibility = if (role == 2) View.VISIBLE else View.GONE
        // 아이템, 구매정보 불러오기
        itemId?.let { item ->
            purchaseId?.let { purchase ->
                RetrofitFunc(this).getItemAndPurchase(item.toLong(), purchase.toLong(),
                    successCallback = { call, res ->
                        if (res.isSuccessful) {
                            val responseBody = res.body()

                            val itemData = responseBody?.get("item") as? Map<String, Any>
                            val purchaseData =
                                responseBody?.get("purchase") as? Map<String, Any>

                            currentItem =
                                Gson().fromJson(Gson().toJson(itemData), ItemDto::class.java)
                            currentPurchase = Gson().fromJson(
                                Gson().toJson(purchaseData),
                                PurchaseDto::class.java
                            )

                            // 아이템 부분
                            Picasso.get()
                                .load(currentItem.thumbNail)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .error(R.drawable.ic_move_inbox)
                                .into(binding.itemView.imageView)

                                binding.itemView.textViewItem.text = currentItem.name
                                binding.itemView.textViewItemSubTitle.text =
                                    DecimalFormat("##000").format(currentItem.id)
                                binding.itemView.textViewCount.text =
                                    currentItem.totalNum.toString()

                            binding.textViewCompany.text = currentItem.company.name
                            binding.textViewCompanyCode.text = currentItem.company.code

                            binding.textViewLocation.text =
                                "${currentItem.container.section} 창고"
                            binding.textViewLocationCode.text = currentItem.container.code

                            // 입고 부분
                            binding.textViewNew.visibility =
                                if (currentPurchase.newYn == "Y") View.VISIBLE else View.INVISIBLE
                            binding.textViewName.text = currentPurchase.user.userName
                            binding.textViewNum.text = currentPurchase.reqNum.toString()
                        }
                    },
                    failureCallback =
                    {call, t ->
                        Log.e("Error", "scanQR로 데이터 불러오는 중 오류 발생")
                    })


//                RetrofitClient.retrofit.getItemAndPurchase(
//                    dbHelper.getAccessToken(),
//                    item.toLong(),
//                    purchase.toLong()
//                )
//                    .enqueue(object : retrofit2.Callback<Map<String, Any>> {
//                        override fun onResponse(
//                            call: Call<Map<String, Any>>,
//                            response: Response<Map<String, Any>>
//                        ) {
//                            if (response.isSuccessful) {
//                                val responseBody = response.body()
//
//                                val itemData = responseBody?.get("item") as? Map<String, Any>
//                                val purchaseData =
//                                    responseBody?.get("purchase") as? Map<String, Any>
//
//                                currentItem =
//                                    Gson().fromJson(Gson().toJson(itemData), ItemDto::class.java)
//                                currentPurchase = Gson().fromJson(
//                                    Gson().toJson(purchaseData),
//                                    PurchaseDto::class.java
//                                )
//
//                                // 아이템 부분
//                                Picasso.get()
//                                    .load(currentItem.thumbNail)
//                                    .networkPolicy(NetworkPolicy.NO_CACHE)
//                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
//                                    .error(R.drawable.ic_move_inbox)
//                                    .into(binding.itemView.imageView)
//
//                                binding.itemView.textViewItem.text = currentItem.name
//                                binding.itemView.textViewItemSubTitle.text =
//                                    DecimalFormat("##000").format(currentItem.id)
//
//                                binding.textViewCompany.text = currentItem.company.name
//                                binding.textViewCompanyCode.text = currentItem.company.code
//
//                                binding.textViewLocation.text =
//                                    "${currentItem.container.section} 창고"
//                                binding.textViewLocationCode.text = currentItem.container.code
//
//                                // 입고 부분
//                                binding.textViewNew.visibility = if (currentPurchase.newYn == "Y") View.VISIBLE else View.INVISIBLE
//                                binding.textViewName.text = currentPurchase.user.userName
//                                binding.textViewNum.text = currentPurchase.reqNum.toString()
//
//                            } else {
//                                Log.e("Error", "scanQR로 통신 실패: ${response.code()}")
//                            }
//                        }
//
//                        override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
//                            Log.e("Error", "scanQR로 데이터 불러오는 중 오류 발생")
//                        }
//
//                    })
            }
        }

        // 출고 처리
        binding.buttonOutput.setOnClickListener {
            val usageNum = binding.textViewUseCount.text.toString().toInt()

            val usageDto = UsageDto(
                -1L,
                usageNum,
                "",
                currentItem,
                UserDto(
                    -1L,
                    dbHelper.getUID(),
                    "",
                    "",
                    ""
                )
            )

            RetrofitFunc(this).useItem(usageDto,
                {call, res->

                    val intent = Intent(this@InOutActivity, MainActivity::class.java)
                    startActivity(intent)
                })

//            RetrofitClient.retrofit.useItem(dbHelper.getAccessToken(), usageDto)
//                .enqueue(object : retrofit2.Callback<Boolean> {
//                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
//
////                        Toast.makeText(this@InOutActivity, "차감 성공", Toast.LENGTH_SHORT).show()
//
//                    }
//
//                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
//                        Toast.makeText(this@InOutActivity, "차감 실패", Toast.LENGTH_SHORT).show()
//                    }
//
//                })

        }

        // 입고 처리
        binding.wholeLinear.setOnClickListener {
            itemId?.let { item ->
                purchaseId?.let { purchase ->
                    RetrofitFunc(this).instockItem(item.toLong(), purchase.toLong(),
                        {call, res->
                            val intent = Intent(this@InOutActivity, MainActivity::class.java)
                            startActivity(intent)
                        })

//                    RetrofitClient.retrofit.instockItem(dbHelper.getAccessToken(), item.toLong(), purchase.toLong())
//                        .enqueue(object : retrofit2.Callback<Boolean> {
//                            override fun onResponse(
//                                call: Call<Boolean>,
//                                response: Response<Boolean>
//                            ) {
////                                Toast.makeText(this@InOutActivity, "입고 성공", Toast.LENGTH_SHORT).show()
//                                val intent = Intent(this@InOutActivity, MainActivity::class.java)
//                                startActivity(intent)
//                            }
//
//                            override fun onFailure(call: Call<Boolean>, t: Throwable) {
//                                Toast.makeText(this@InOutActivity, "입고 실패", Toast.LENGTH_SHORT).show()
//
//                            }
//
//                        })
                }
            }
        }

        binding.fabMinus.setOnClickListener {
            updateCount(false)
        }

        binding.fabPlus.setOnClickListener {
            updateCount(true)
        }

    }

    private fun updateCount(add: Boolean) {
        var currentCount: Int = binding.textViewUseCount.text.toString().toInt()

        if (add) {
            val plusCount = currentCount + 1

            if (currentItem.totalNum >= plusCount) {
                currentCount = plusCount
            }

        } else {
            val minusCount = currentCount - 1

            if (minusCount > 0) {
                currentCount = minusCount
            }

        }

        binding.textViewUseCount.text = currentCount.toString()
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