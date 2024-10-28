package com.example.kotiln_tpj_yesim.Retrofit

import android.content.Context
import android.util.Log
import com.example.kotiln_tpj_yesim.db.DBHelper
import com.example.kotiln_tpj_yesim.dto.ItemDto
import com.example.kotiln_tpj_yesim.dto.JwtDto
import com.example.kotiln_tpj_yesim.dto.PurchaseDto
import com.example.kotiln_tpj_yesim.dto.UsageDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


// 권한이 필요한 페이지(URL) 관리
class RetrofitFunc(private val context : Context) {

    private var dbHelper:DBHelper = DBHelper(context)

    private fun getAccessToken() :String {
        return dbHelper.getAccessToken()
    }

    private fun getRefreshToken():String {
        return dbHelper.getRefreshToken()
    }

    //토근 재발급 용

    // access token 만료 시 refresh token 을 사용 하여 새로 가져옴
    private fun refreshJWT(successCallback:(Call<JwtDto>, Response<JwtDto>)->(Any)){
        RetrofitClient.retrofit.refreshJWT(getRefreshToken()).enqueue(object : Callback<JwtDto> {
            override fun onResponse(
                call: Call<JwtDto>,
                response: Response<JwtDto>
            ) {
                val jwt = response.body() as JwtDto

                // 새로 받은 토큰 갱신
                dbHelper.updateAccessToken(jwt.accessToken)
                successCallback(call, response)

            }

            override fun onFailure(call: Call<JwtDto>, t: Throwable) {
                // refresh token 의 만료 로그인 안된 상태
                // 로그인 화면으로

            }
        })
    }

    fun getItemList(pageNum:Int, successCallback: (Call<List<ItemDto>>, Response<List<ItemDto>>) -> Any, isRefresh:Boolean = true){
        RetrofitClient.retrofit.getItemList(getAccessToken(), pageNum).enqueue(object : Callback<List<ItemDto>>{
            override fun onResponse(call: Call<List<ItemDto>>, response: Response<List<ItemDto>>) {

                when(response.code()){
                    200 -> successCallback(call, response)
                    403 -> if(isRefresh) refreshJWT { c, r ->
                        getItemList(pageNum, successCallback, false)
                    }
                }
            }

            override fun onFailure(call: Call<List<ItemDto>>, t: Throwable) {
            }
        })
    }

    fun getItemInoutList(pageNum:Int, date:String,
                         successCallback: (Call<Map<String, Any>>, Response<Map<String, Any>>) -> Any, isRefresh:Boolean = true){
        RetrofitClient.retrofit.getItemInoutList(getAccessToken(), pageNum, date).enqueue(object : Callback<Map<String, Any>>{
            override fun onResponse(
                call: Call<Map<String, Any>>,
                response: Response<Map<String, Any>>
            ) {
                when(response.code()) {
                    200 -> successCallback(call, response)
                    403 -> if(isRefresh) refreshJWT { c, r ->
                        getItemInoutList(pageNum, date, successCallback, false)
                    }
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
            }

        })
    }

    fun getPurchaseListInStore(pageNum:Int, date:String,
                               successCallback:(Call<Map<String, Any>>, Response<Map<String, Any>>) -> Any, isRefresh: Boolean = true){
        RetrofitClient.retrofit.getPurchaseListInStore(getAccessToken(), pageNum, date).enqueue(object :Callback<Map<String, Any>>{
            override fun onResponse(
                call: Call<Map<String, Any>>,
                response: Response<Map<String, Any>>
            ) {
                when(response.code()){
                    200 -> successCallback(call, response)
                    403 -> if(isRefresh) refreshJWT{ c, r ->
                        getPurchaseListInStore(pageNum, date, successCallback, false)
                    }
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
            }

        })
    }

    fun getUsageListGroupedItem(pageNum:Int, date:String,
                                successCallback: (Call<Map<String, Any>>, Response<Map<String, Any>>) -> Any, isRefresh: Boolean = true){
        RetrofitClient.retrofit.getUsageListGroupedItem(getAccessToken(), pageNum, date).enqueue(object : Callback<Map<String, Any>>{
            override fun onResponse(
                call: Call<Map<String, Any>>,
                response: Response<Map<String, Any>>
            ) {
                when(response.code()){
                    200 -> successCallback(call, response)
                    403 -> if(isRefresh) refreshJWT{ c, r ->
                        getUsageListGroupedItem(pageNum, date, successCallback, false)
                    }
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
            }

        })
    }

    fun getUsageListByUser(pageNum:Int, date:String,
                           successCallback: (Call<Map<String, Any>>, Response<Map<String, Any>>) -> Any, isRefresh: Boolean = true){
        RetrofitClient.retrofit.getUsageListByUser(getAccessToken(), pageNum, date).enqueue(object : Callback<Map<String, Any>>{
            override fun onResponse(
                call: Call<Map<String, Any>>,
                response: Response<Map<String, Any>>
            ) {
                when(response.code()){
                    200 -> successCallback(call, response)
                    403 -> if(isRefresh) refreshJWT{ c, r ->
                        getUsageListByUser(pageNum, date, successCallback, false)
                    }
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
            }

        })
    }

    fun getUnconfirmedPurchase(pageNum:Int,
                               successCallback: (Call<List<PurchaseDto>>, Response<List<PurchaseDto>>) -> Any, isRefresh: Boolean = true){
        RetrofitClient.retrofit.getUnconfirmedPurchase(getAccessToken(), pageNum).enqueue(object : Callback<List<PurchaseDto>>{
            override fun onResponse(
                call: Call<List<PurchaseDto>>,
                response: Response<List<PurchaseDto>>
            ) {
                when(response.code()){
                    200 -> successCallback(call, response)
                    403 -> if(isRefresh) refreshJWT { c, r ->
                        getUnconfirmedPurchase(pageNum, successCallback, false)
                    }
                }
            }

            override fun onFailure(call: Call<List<PurchaseDto>>, t: Throwable) {
            }

        })
    }

    fun getConfirmedPurchase(pageNum:Int,
                             successCallback: (Call<List<PurchaseDto>>, Response<List<PurchaseDto>>) -> Any, isRefresh: Boolean = true){
        RetrofitClient.retrofit.getConfirmedPurchase(getAccessToken(), pageNum).enqueue(object : Callback<List<PurchaseDto>>{
            override fun onResponse(
                call: Call<List<PurchaseDto>>,
                response: Response<List<PurchaseDto>>
            ) {
                when(response.code()){
                    200 -> successCallback(call, response)
                    403 -> if(isRefresh) refreshJWT { c, r ->
                        getConfirmedPurchase(pageNum, successCallback, false)
                    }
                }
            }

            override fun onFailure(call: Call<List<PurchaseDto>>, t: Throwable) {
            }

        })
    }

    fun getItemListByNameSearch(searchVal:String,
                                successCallback: (Call<List<ItemDto>>, Response<List<ItemDto>>) -> Any, isRefresh: Boolean = true){
        RetrofitClient.retrofit.getItemListByNameSearch(getAccessToken(), searchVal).enqueue(object : Callback<List<ItemDto>>{
            override fun onResponse(call: Call<List<ItemDto>>, response: Response<List<ItemDto>>) {

                when(response.code()){
                    200 -> successCallback(call, response)
                    403 -> if(isRefresh) refreshJWT { c, r -> getItemListByNameSearch(searchVal, successCallback, false) }
                }
            }

            override fun onFailure(call: Call<List<ItemDto>>, t: Throwable) {
            }

        })
    }

    fun getItemAndPurchase(itemId:Long, purchaseId:Long,
                           successCallback: (Call<Map<String, Any>>, Response<Map<String, Any>>) -> Any,
                           failureCallback: (Call<Map<String, Any>>, Throwable) -> Any,
                           isRefresh: Boolean = true){
        RetrofitClient.retrofit.getItemAndPurchase(getAccessToken(), itemId, purchaseId).enqueue(object : Callback<Map<String, Any>>{
            override fun onResponse(
                call: Call<Map<String, Any>>,
                response: Response<Map<String, Any>>
            ) {
                when(response.code()){
                    200 -> successCallback(call, response)
                    403 -> if(isRefresh) refreshJWT { c, r -> getItemAndPurchase(itemId, purchaseId, successCallback, failureCallback, false)}
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                failureCallback(call, t)
            }

        })
    }

    fun useItem(usageDto:UsageDto,
                successCallback: (Call<Boolean>, Response<Boolean>) -> Any, isRefresh: Boolean = true){
        RetrofitClient.retrofit.useItem(getAccessToken(), usageDto).enqueue(object : Callback<Boolean>{
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                when(response.code()){
                    200 -> successCallback(call, response)
                    403 -> if(isRefresh) refreshJWT { c, r -> useItem(usageDto, successCallback, false)}
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
            }

        })
    }

    fun instockItem(itemId:Long, purchaseId:Long,
                    successCallback: (Call<Boolean>, Response<Boolean>) -> Any, isRefresh: Boolean = true){
        RetrofitClient.retrofit.instockItem(getAccessToken(), itemId, purchaseId).enqueue(object : Callback<Boolean>{
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                when(response.code()){
                    200 -> successCallback(call, response)
                    403 -> if(isRefresh) refreshJWT { c, r -> instockItem(itemId, purchaseId, successCallback, false)}
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
            }

        })
    }

    fun getUnderMinItemList(pageNum:Int,
                            successCallback: (Call<Map<String, Any>>, Response<Map<String, Any>>) -> Any, isRefresh: Boolean = true){
        RetrofitClient.retrofit.getUnderMinItemList(getAccessToken(), pageNum).enqueue(object : Callback<Map<String, Any>>{
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                when(response.code()){
                    200 -> successCallback(call, response)
                    403 -> if(isRefresh) refreshJWT { c, r -> getUnderMinItemList(pageNum, successCallback, false)}
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                Log.d("-------", t.message.toString())
            }

        })
    }

    fun getTotalUsageListByDateAndUserGroupedItem(pageNum: Int, date:String, userId:String,
                                                  successCallback: (Call<Map<String, Any>>, Response<Map<String, Any>>) -> Any, isRefresh: Boolean = true){
        RetrofitClient.retrofit.getTotalUsageListByDateAndUserGroupedItem(getAccessToken(), pageNum, date, userId).enqueue(object :Callback<Map<String, Any>>{
            override fun onResponse(
                call: Call<Map<String, Any>>,
                response: Response<Map<String, Any>>
            ) {
                when(response.code()){
                    200 -> successCallback(call, response)
                    403 -> if(isRefresh) refreshJWT { c, r -> getTotalUsageListByDateAndUserGroupedItem(pageNum, date, userId, successCallback, false)}
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
            }

        })
    }

}