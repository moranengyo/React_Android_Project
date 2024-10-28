package com.example.kotiln_tpj_yesim.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotiln_tpj_yesim.Retrofit.RetrofitClient
import com.example.kotiln_tpj_yesim.Retrofit.RetrofitFunc
import com.example.kotiln_tpj_yesim.adapter.NotificationAdapter
import com.example.kotiln_tpj_yesim.adapter.RequestAdapter
import com.example.kotiln_tpj_yesim.data.Request
import com.example.kotiln_tpj_yesim.databinding.FragmentRequestBinding
import com.example.kotiln_tpj_yesim.db.DBHelper
import com.example.kotiln_tpj_yesim.dto.ItemDto
import com.example.kotiln_tpj_yesim.dto.PurchaseDto
import com.example.kotiln_tpj_yesim.dto.UserDto
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response

class RequestFragment : Fragment() {

    private lateinit var binding: FragmentRequestBinding
    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var requestAdapter: RequestAdapter
    private var requestData = mutableListOf<PurchaseDto>()
    private var isLoading = false
    private var hasMoreData = true
    private var isNotification: Boolean = false
    private var role: String = "구매관리자"

    private var pageNumber:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when(DBHelper(this.requireContext()).getUserInfo()!!.role){
            "ROLE_MANAGER" -> role = "구매관리자"
            else -> role = ""
        }

        arguments?.let {
            isNotification = it.getBoolean("isNotification", false)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRequestBinding.inflate(inflater, container, false)
        binding.recyclerviewRequest.layoutManager = LinearLayoutManager(requireContext())

        setupRequestAdapter()
//        showRequestData()

        loadData()

        binding.recyclerviewRequest.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && hasMoreData && (visibleItemCount + firstVisibleItemPosition >= totalItemCount)
                    && firstVisibleItemPosition >= 0
                ) {
                    if (isLoading) return
                    isLoading = true
                    loadData(false)
                }
            }
        })

        return binding.root
    }

    private fun setupRequestAdapter() {
        notificationAdapter = NotificationAdapter(requireContext(), requestData, role)
        requestAdapter = RequestAdapter(requireContext(), requestData, role)

        binding.recyclerviewRequest.adapter = if (isNotification) notificationAdapter else requestAdapter
    }

    private fun loadData(isInit:Boolean = true){
        val dbHelper:DBHelper = DBHelper(this.requireContext())
        if(isNotification) {
            if(role.equals("구매관리자")) {
                RetrofitFunc(this.requireContext()).getUnderMinItemList(pageNumber,
                    { call, res ->
                        val body = res.body() as Map<String, Any>


                        val itemListData = body["itemList"] as List<*>
                        val itemList:MutableList<ItemDto> = mutableListOf()

                        for(itemData in itemListData){
                            val data = itemData as Map<String, Any>
                            itemList.add(Gson().fromJson(Gson().toJson(data), ItemDto::class.java))
                        }

                        val data:MutableList<PurchaseDto> = mutableListOf()

                        for(item in itemList){
                            data.add(
                                PurchaseDto(
                                    -1L,
                                    item.name,
                                    item.totalNum,
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "N",
                                    item,
                                    UserDto(
                                        -1L,
                                        "",
                                        "",
                                        "",
                                        ""
                                    )
                                )
                            )
                        }
                        if (isInit)
                            showRequestData(data)
                        else
                            showMoreRequestData(data)
                        true
                    })
            }
            else{
                RetrofitFunc(this.requireContext()).getUnconfirmedPurchase(pageNumber,
                    {call, res ->
                        if (isInit)
                            showRequestData(res.body() as List<PurchaseDto>)
                        else
                            showMoreRequestData(res.body() as List<PurchaseDto>)
                    })
            }

        }else{
            RetrofitFunc(this.requireContext()).getConfirmedPurchase(pageNumber,
                {call, res
                ->
                    if(isInit)
                        showRequestData(res.body() as List<PurchaseDto>)
                    else
                        showMoreRequestData(res.body() as List<PurchaseDto>)
                })
        }
    }


    private fun showRequestData(data:List<PurchaseDto>) {
        requestData.addAll(data)
        val currentAdapter = if (isNotification) notificationAdapter else requestAdapter
        currentAdapter.notifyDataSetChanged()
        pageNumber++
    }

    private fun filter(data: List<Request>): List<Request> {
        if (role == "구매관리자") {
            if (isNotification) {
                return data.filter { it.status == "미확인" }
            } else {
                return data.filter { it.status == "요청" || it.status == "승인" || it.status == "반려" }
            }
        } else {
            if (isNotification) {
                return data.filter { it.status == "요청"}
            } else {
                return data.filter { it.status == "승인" || it.status == "반려" }
            }
        }
    }


    private fun showMoreRequestData(data:List<PurchaseDto>) {
        isLoading = false
        if(data.isEmpty()) {
            return
        }

        showRequestData(data)
    }
}