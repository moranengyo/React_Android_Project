package com.example.kotiln_tpj_yesim.Retrofit

import com.example.kotiln_tpj_yesim.dto.ItemDto
import com.example.kotiln_tpj_yesim.dto.JwtDto
import com.example.kotiln_tpj_yesim.dto.LoginCmpltDto
import com.example.kotiln_tpj_yesim.dto.PurchaseDto
import com.example.kotiln_tpj_yesim.dto.SignUpDto
import com.example.kotiln_tpj_yesim.dto.UsageDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitInterface {

    // 토근 만료 시 새로운 토큰으로 갱신
    @POST("/auth/refresh")
    fun refreshJWT(@Header("RefreshToken") refreshToken:String) : Call<JwtDto>

    @POST("/auth/signup")
    fun signUp(@Body user:SignUpDto) : Call<String>

    @GET("/auth/login")
    fun login(@Query("userId") userId:String, @Query("userPw") userPw:String) : Call<LoginCmpltDto> // loginDto: LoginDto) :Call<JwtDto>


    @GET("/user/item/detail/{itemId}")
    fun getItemDetail(@Header("Authorization") authorization:String, @Path("itemId") itemId:Long) : Call<ItemDto>

    @GET("/user/item/{pageNum}")
    fun getItemList(@Header("Authorization") authorization:String, @Path("pageNum") pageNum:Int) :Call<List<ItemDto>>


    @GET("/manager/inout/{pageNum}")
    fun getItemInoutList(@Header("Authorization") authorization: String, @Path("pageNum") pageNum:Int, @Query("date") date:String) :Call<Map<String, Any>>

    @GET("/manager/purchase/in-store/{pageNum}")
    fun getPurchaseListInStore(@Header("Authorization") authorization: String, @Path("pageNum") pageNum: Int, @Query("date") date:String) : Call<Map<String, Any>>

    @GET("/manager/usage/{pageNum}")
    fun getUsageList(@Header("Authorization") authorization: String, @Path("pageNum") pageNum:Int, @Query("date") date:String)  : Call<Map<String, Any>>


    @GET("/user/usage/item/{pageNum}")
    fun getUsageListGroupedItem(@Header("Authorization") authorization: String, @Path("pageNum") pageNum:Int, @Query("date") date:String)  : Call<Map<String, Any>>

    @GET("/user/usage/user/{pageNum}")
    fun getUsageListByUser(@Header("Authorization") authorization: String, @Path("pageNum") pageNum:Int, @Query("date") date:String) : Call<Map<String, Any>>

    @GET("/user/purchase/detail/{purchaseId}")
    fun getPurchaseDetail(@Header("Authorization") authorization:String, @Path("purchaseId") purchaseId:Long) : Call<PurchaseDto>


    @GET("/manager/purchase/unconfirmed/{pageNum}")
    fun getUnconfirmedPurchase(@Header("Authorization") authorization: String, @Path("pageNum") pageNum: Int) :Call<List<PurchaseDto>>


    @GET("/manager/purchase/confirmed/{pageNum}")
    fun getConfirmedPurchase(@Header("Authorization") authorization: String, @Path("pageNum") pageNum: Int) :Call<List<PurchaseDto>>

    @GET("/user/item/search")
    fun getItemListByNameSearch(@Header("Authorization") authorization: String, @Query("searchVal") searchVal:String) : Call<List<ItemDto>>

    @GET("/user/scanQRCode")
    fun getItemAndPurchase(@Header("Authorization") authorization: String, @Query("itemId") itemId: Long, @Query("purchaseId") purchaseId: Long) : Call<Map<String, Any>>

    @POST("/user/useItem")
    fun useItem(@Header("Authorization") authorization: String, @Body usageDto: UsageDto) : Call<Boolean>

    @GET("/manager/inStock")
    fun instockItem(@Header("Authorization") authorization: String, @Query("itemId") itemId: Long, @Query("purchaseId") purchaseId: Long) : Call<Boolean>

    @GET("/manager/item/under-min/{pageNum}")
    fun getUnderMinItemList(@Header("Authorization") authorization: String, @Path("pageNum") pageNum:Int) :Call<Map<String, Any>>


    @GET("/auth/check/userId/duplicate")
    fun checkUserIdDuplicate(@Query("userId") userId:String) : Call<Boolean>

    @GET("/user/usage/user/item/{pageNum}")
    fun getTotalUsageListByDateAndUserGroupedItem(@Header("Authorization") authorization: String, @Path("pageNum") pageNum:Int, @Query("date") date:String, @Query("userId") userId:String) : Call<Map<String, Any>>
}