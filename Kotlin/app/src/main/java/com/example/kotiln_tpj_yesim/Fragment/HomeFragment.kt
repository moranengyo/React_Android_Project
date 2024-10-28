package com.example.kotiln_tpj_yesim.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.kotiln_tpj_yesim.R
import com.example.kotiln_tpj_yesim.Retrofit.RetrofitClient
import com.example.kotiln_tpj_yesim.Retrofit.RetrofitFunc
import com.example.kotiln_tpj_yesim.adapter.BannerAdapter
import com.example.kotiln_tpj_yesim.adapter.OnBannerClickListener
import com.example.kotiln_tpj_yesim.adapter.UsageAdapter
import com.example.kotiln_tpj_yesim.databinding.FragmentHomeBinding
import com.example.kotiln_tpj_yesim.db.DBHelper
import com.example.kotiln_tpj_yesim.dto.ItemInOutDto
import retrofit2.Call
import retrofit2.Response

// role: 1 : 직원
// role: 2, 3 : 구매관리자, 구매부장
class HomeFragment(
    val role: Int,
    val isBackStack: Boolean

) : Fragment(), OnBannerClickListener {

    private lateinit var viewPagerBanner: ViewPager2
    private lateinit var usageList: MutableList<ItemInOutDto>
    private lateinit var recyclerViewUsage: RecyclerView
    private lateinit var usageAdapter: UsageAdapter
    private lateinit var bannerAdapter: BannerAdapter
    private var isLoading = false // 로딩 중인지 확인하는 플래그
    lateinit var binding: FragmentHomeBinding

    private var bannerKind: Int = 0
    private var bannerPosition: Int = 0
    private var pageNum: Int = 0

    private var isUserList:Boolean = false
    private var usage: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        usage = arguments?.let { if (role > 1) it.getBoolean("usage") else true } ?: true
        val actionbarTitle: String
        val backColorId: Int

        if (role > 1 && !isBackStack) {
            bannerKind = 0
            actionbarTitle = "입출고 현황 조회"
            backColorId = ContextCompat.getColor(requireContext(), R.color.bannerPrimary)
        } else if (role > 1 && isBackStack && !usage) {
            bannerKind = 1
            actionbarTitle = "입고 현황 조회"
            backColorId = ContextCompat.getColor(requireContext(), R.color.blue)
        } else {
            bannerKind = 2
            actionbarTitle = "사용 현황 조회"
            backColorId = ContextCompat.getColor(requireContext(), R.color.pink)
        }

        val actionBar = (activity as? AppCompatActivity)?.supportActionBar

        actionBar?.setDisplayHomeAsUpEnabled(isBackStack)
        actionBar?.title = actionbarTitle

        // banner
        bannerAdapter = BannerAdapter(
            role,
            isBackStack,
            usage,
            "30",
            "30",
            this,
            actionbarTitle,
            backColorId
        )

        initRecyclerView()

        viewPagerBanner = binding.viewPagerBanner
        viewPagerBanner.adapter = bannerAdapter


        // 배너 변경
        viewPagerBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                try {
                    // 스크롤 정지 및 최상위 이동
                    recyclerViewUsage.scrollToPosition(0)
                    recyclerViewUsage.stopScroll()

                    // 페이지 초기화
                    pageNum = 0
                    bannerPosition = position
                    loadData()
                } catch (e: Exception) {
                    Log.d("Page Changing Err", e.message.toString())
                }
            }
        })

        val dotsIndicator = binding.dotsIndicator
        dotsIndicator.setViewPager2(viewPagerBanner)
        dotsIndicator.selectedDotColor = backColorId

        if (role > 1 && isBackStack && usage) {
            binding.toggleGroup.visibility = View.VISIBLE
        }


        binding.toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                var date = "D"
                // 스크롤 정지 및 최상위 이동
                recyclerViewUsage.scrollToPosition(0)
                recyclerViewUsage.stopScroll()

                pageNum = 0
                when (bannerPosition) {
                    0 -> {
                        // 일
                        date = "D"
                    }

                    1 -> {
                        // 월
                        date = "M"
                    }

                    2 -> {
                        // 연
                        date = "Y"
                    }
                }
                when (checkedId) {

                    binding.buttonProduct.id -> {
//                        makeUsageAdapter(usage, true)
                        isUserList = false
                        loadUsageData(usage, date,  true)
                    }

                    binding.buttonEmployee.id -> {
//                        makeUsageAdapter(usage, false)
                        isUserList = true
                        loadUsageData(usage, date, true)
                    }
                }
            }
        }

        return binding.root
    }

    override fun onBannerClick(isUsage: Boolean) {

        val anotherFragment = HomeFragment(2, true)

        val bundle = Bundle()
        bundle.putBoolean("usage", isUsage)
        anotherFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(R.id.container, anotherFragment)
            .addToBackStack(null)
            .commit()
    }

    fun initRecyclerView() {
        var blueTitle: String? = 2.toString()
        var blueSubTitle: String? = null
        var pinkTitle: String? = 3.toString()

        if (role < 2) {
            blueTitle = null
        } else {
            if (isBackStack) {
                if (!usage) {
                    pinkTitle = null
                } else {
                    blueTitle = 4.toString()
                    blueSubTitle = "총 재고"
                }
            }
        }

//        blueTitle = if (!isItemSelected) null else blueTitle

        usageList = mutableListOf()
        usageAdapter =
            UsageAdapter(usageList, blueTitle, pinkTitle, blueSubTitle, true)


        recyclerViewUsage = binding.recyclerView
        recyclerViewUsage.adapter = usageAdapter
        recyclerViewUsage.layoutManager = LinearLayoutManager(this.context)


        // 무한 스크롤을 감지하는 스크롤 리스너 추가
        recyclerViewUsage.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                // 리스트 끝에 도달했을 때 추가 데이터 로드
                if (!isLoading && lastVisibleItemPosition >= totalItemCount - 2) {
                    if (isLoading) {
                        return
                    }

                    loadData(false)
                    isLoading = true
                }
            }
        })
    }

    fun loadData(isInit: Boolean = true) {

        var date = ""
        when (bannerPosition) {
            0 -> {
                // 일
                date = "D"
            }

            1 -> {
                // 월
                date = "M"
            }

            2 -> {
                // 연
                date = "Y"
            }
        }

        when (bannerKind) {
            0 -> {
                loadInOutData(usage, date, isInit)
            }

            1 -> {
                loadInStoreData(usage, date, isInit)
            }

            2 -> {
                loadUsageData(
                    usage,
                    date,
                    isInit
                )
            }
        }
    }

    // 관리자 이상(manager) 입출고(inout)
    fun loadInOutData(usage: Boolean = true, date: String = "D", isInit: Boolean = true) {
        val dbHelper = this.context?.let { DBHelper(it) }
        val accessToken = dbHelper?.getAccessToken().toString()

        RetrofitFunc(this.requireContext()).getItemInoutList(pageNum, date,
            {call, res ->
                val data = res.body() as Map<String, Any>

                val totalReqSum = data["totalReqSum"].toString().toFloat().toInt()
                val totalUsageSum = data["totalUsageSum"].toString().toFloat().toInt()
                val dataList = data["inoutList"] as List<*>

                var inoutList: MutableList<ItemInOutDto> = mutableListOf()

                for (data in dataList) {
                    val inout = data as Map<String, *>
                    inoutList.add(
                        ItemInOutDto(
                            inout["itemId"].toString().toDouble(),
                            inout["itemName"].toString(),
                            inout["companyName"].toString(),
                            inout["thumbnail"].toString(),
                            inout["containerSection"].toString(),
                            inout["totalNum"].toString().toDouble().toInt(),
                            inout["totalReqNum"].toString().toDouble().toInt(),
                            inout["totalUsageNum"].toString().toDouble().toInt(),
                        )
                    )
                }

                val ba = viewPagerBanner.adapter as BannerAdapter
                ba.setFirstCount(totalReqSum)
                ba.setSecondCount(totalUsageSum)
                ba.notifyDataSetChanged()
                makeUsageAdapter(inoutList, usage, isInit)
            })
/*
        RetrofitClient.retrofit.getItemInoutList(accessToken, pageNum, date)
            .enqueue(object : retrofit2.Callback<Map<String, Any>> {
                override fun onResponse(
                    call: Call<Map<String, Any>>,
                    response: Response<Map<String, Any>>
                ) {

                    Log.d("inoutList", "inoutList");
                    val data = response.body() as Map<String, Any>
                    try {
                        val totalReqSum = data["totalReqSum"].toString().toFloat().toInt()
                        val totalUsageSum = data["totalUsageSum"].toString().toFloat().toInt()
                        val dataList = data["inoutList"] as List<*>

                        var inoutList: MutableList<ItemInOutDto> = mutableListOf()

                        for (data in dataList) {
                            val inout = data as Map<String, *>
                            inoutList.add(
                                ItemInOutDto(
                                    inout["itemId"].toString().toDouble(),
                                    inout["itemName"].toString(),
                                    inout["companyName"].toString(),
                                    inout["thumbnail"].toString(),
                                    inout["containerSection"].toString(),
                                    inout["totalNum"].toString().toDouble().toInt(),
                                    inout["totalReqNum"].toString().toDouble().toInt(),
                                    inout["totalUsageNum"].toString().toDouble().toInt(),
                                )
                            )
                        }

                        val ba = viewPagerBanner.adapter as BannerAdapter
                        ba.setFirstCount(totalReqSum)
                        ba.setSecondCount(totalUsageSum)
                        ba.notifyDataSetChanged()
                        makeUsageAdapter(inoutList, usage, isInit)
                    } catch (e: Exception) {
                        Log.d("inoutList Err", e.toString())
                    }
                    Log.d("inoutList", "inoutList")
                    Log.d("inoutList", "-------------")
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    isLoading = false
                }

            })

 */
    }

    fun loadInStoreData(usage: Boolean = true, date: String = "D", isInit: Boolean = true) {
        RetrofitFunc(this.requireContext()).getPurchaseListInStore(pageNum, date,
            { call, res ->
                val data = res.body() as Map<String, Any>
                val totalReqSum = data["totalReqSum"].toString().toFloat().toInt()
                val dataList = data["inStoreList"] as List<*>

                var inStoreList: MutableList<ItemInOutDto> = mutableListOf()

                for (data in dataList) {
                    val inStore = data as Map<String, *>
                    val item = inStore["item"] as Map<String, *>
                    val container = item["container"] as Map<String, *>
                    val company = item["company"] as Map<String, *>

                    inStoreList.add(
                        ItemInOutDto(
                            inStore["id"].toString().toDouble(),
                            item["name"].toString(),
                            company["name"].toString(),
                            item["thumbNail"].toString(),
                            container["section"].toString(),
                            item["totalNum"].toString().toDouble().toInt(),
                            inStore["reqNum"].toString().toDouble().toInt(),
                            0
                        )
                    )
                }

                val ba = viewPagerBanner.adapter as BannerAdapter
                ba.setFirstCount(totalReqSum)
                ba.notifyDataSetChanged()
                makeUsageAdapter(inStoreList, usage, isInit)
            })
    }


    fun loadUsageData(
        usage: Boolean = true,
        date: String = "D",
        isInit: Boolean = true
    ) {
        val dbHelper = this.context?.let { DBHelper(it) }
        val accessToken = dbHelper?.getAccessToken().toString()
        if (isUserList) {
            // 직원별??


                RetrofitFunc(this.requireContext()).getUsageListByUser(pageNum, date,
                    { call, res ->
                        val data = res.body() as Map<String, Any>
                        makeUsageAdapter(setUsageData(data), usage, isInit)
                    })

        } else {
            if(role == 1){
                val userId = dbHelper!!.getUID()
                RetrofitFunc(this.requireContext()).getTotalUsageListByDateAndUserGroupedItem(pageNum, date, userId, {
                        call, res ->
                    val data = res.body() as Map<String, Any>
                    makeUsageAdapter(setUsageData(data), usage, isInit)
                })
            }else {
                RetrofitFunc(this.requireContext()).getUsageListGroupedItem(pageNum, date,
                    { call, res ->
                        val data = res.body() as Map<String, Any>

                        makeUsageAdapter(setUsageData(data), usage, isInit)
                    })
            }
        }
    }

    fun setUsageData(data: Map<String, Any>): MutableList<ItemInOutDto> {
        try {
            val totalUsageSum = data["totalUsageSum"].toString().toFloat().toInt()
            val dataList = data["usageList"] as List<*>

            var usageList: MutableList<ItemInOutDto> = mutableListOf()

            if(isUserList){
                for (data in dataList) {
                    val usage = data as Map<String, *>
                    val item = usage["item"] as Map<String, *>
                    val user = usage["user"] as Map<String, *>

                    usageList.add(
                        ItemInOutDto(
                            usage["id"].toString().toDouble(),
                            user["userName"].toString(),
                            "",
                            item["thumbNail"].toString(),
                            "",
                            item["totalNum"].toString().toDouble().toInt(),
                            item["totalNum"].toString().toDouble().toInt(),
                            usage["usageNum"].toString().toDouble().toInt(),
                        )
                    )
                }
            }else {
                for (data in dataList) {
                    val usage = data as Map<String, *>
                    val item = usage["item"] as Map<String, *>
                    val container = item["container"] as Map<String, *>
                    val company = item["company"] as Map<String, *>

                    usageList.add(
                        ItemInOutDto(
                            usage["id"].toString().toDouble(),
                            item["name"].toString(),
                            company["name"].toString(),
                            item["thumbNail"].toString(),
                            container["section"].toString(),
                            item["totalNum"].toString().toDouble().toInt(),
                            item["totalNum"].toString().toDouble().toInt(),
                            usage["usageNum"].toString().toDouble().toInt(),
                        )
                    )
                }
            }
            val ba = viewPagerBanner.adapter as BannerAdapter
            ba.setSecondCount(totalUsageSum)
            ba.notifyDataSetChanged()
            return usageList

        } catch (e: Exception) {
            Log.d("inoutList Err", e.toString())
        }

        return mutableListOf()
    }

    fun makeUsageAdapter(inoutData: List<ItemInOutDto>, usage: Boolean, isInit: Boolean = false) {
        if (!isInit) {
            usageList.addAll(inoutData.toMutableList())
        } else {
            usageList = inoutData.toMutableList()
        }

        if (usageList.size > 0) {
            recyclerViewUsage.visibility = View.VISIBLE
            usageAdapter.itemList = usageList;
            pageNum++
        } else {
            recyclerViewUsage.visibility = View.INVISIBLE
        }
        usageAdapter.showImage = !isUserList
        usageAdapter.notifyDataSetChanged()
        isLoading = false
    }
}