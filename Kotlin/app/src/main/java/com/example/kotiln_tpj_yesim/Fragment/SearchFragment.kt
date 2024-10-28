package com.example.kotiln_tpj_yesim

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotiln_tpj_yesim.Retrofit.RetrofitClient
import com.example.kotiln_tpj_yesim.Retrofit.RetrofitFunc
import com.example.kotiln_tpj_yesim.adapter.SearchAdapter
import com.example.kotiln_tpj_yesim.adapter.UsageAdapter
import com.example.kotiln_tpj_yesim.data.Item
import com.example.kotiln_tpj_yesim.data.SearchItem
import com.example.kotiln_tpj_yesim.databinding.FragmentSearchBinding
import com.example.kotiln_tpj_yesim.db.DBHelper
import com.example.kotiln_tpj_yesim.dto.ItemDto
import com.example.kotiln_tpj_yesim.dto.ItemInOutDto
import retrofit2.Call
import retrofit2.Response

class SearchFragment : Fragment() {

    private lateinit var searchInput: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var noResultsMessage: TextView
    private lateinit var searchPromptMessage: TextView
    private val itemList: MutableList<ItemDto> = mutableListOf()
    private var filteredList: MutableList<ItemDto> = mutableListOf()
    private lateinit var dbHelper:DBHelper
    private var pageNum = 0

    private var isLoading = false
    private var isSearching = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSearchBinding.inflate(layoutInflater)
        dbHelper = DBHelper(this.requireContext())

        searchInput = binding.searchInput
        recyclerView = binding.recyclerViewSearch
        noResultsMessage = binding.noResultsMessage
        searchPromptMessage = binding.searchPromptMessage

        searchAdapter = SearchAdapter(mutableListOf(), 2)
        recyclerView.adapter = searchAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        loadItemListData()

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                performSearch(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.recyclerViewSearch.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition >= totalItemCount)
                    && firstVisibleItemPosition >= 0
                ) {
                    if (isLoading) return
                    isLoading = true
                    loadItemListData()
                    Log.d("end scroll", "end scroll")
                }
            }
        })

        return binding.root
    }

    private fun loadItemListData() {
        if(isSearching) return

        RetrofitFunc(this.requireContext()).getItemList(pageNum,
            { call, res ->
                val dataList = res.body() as List<ItemDto>

                if(dataList.isEmpty()) {
                    isLoading = false
                }
                else {
                    for (data in dataList) {
                        searchAdapter.itemList.add(
                            ItemInOutDto(
                                data.id.toDouble(),
                                data.name,
                                data.company.name,
                                data.thumbNail,
                                data.container.section,
                                data.totalNum,
                                data.totalNum,
                                data.totalNum
                            )
                        )
                    }

                    searchAdapter.notifyDataSetChanged()
                    pageNum++
                    isLoading = false
                }

                isLoading
            });
        /*
        RetrofitClient.retrofit.getItemList(dbHelper.getAccessToken(), pageNum).enqueue(object : retrofit2.Callback<List<ItemDto>>{
            override fun onResponse(call: Call<List<ItemDto>>, response: Response<List<ItemDto>>) {

                val dataList = response.body() as List<ItemDto>

                if(dataList.isEmpty()) {
                    isLoading = false
                    return
                }

                for(data in dataList){
                    searchAdapter.itemList.add(
                        ItemInOutDto(
                            data.id.toDouble(),
                            data.name,
                            data.company.name,
                            data.thumbNail,
                            data.container.section,
                            data.totalNum,
                            data.totalNum,
                            data.totalNum
                        )
                    )
                }

                searchAdapter.notifyDataSetChanged()
                pageNum++
                isLoading = false
            }

            override fun onFailure(call: Call<List<ItemDto>>, t: Throwable) {
            }
        })
*/
        searchAdapter.notifyDataSetChanged()
    }

    private fun performSearch(query: String) {
        isSearching = !query.isEmpty()
        if (query.isEmpty()) {
            searchPromptMessage.visibility = View.VISIBLE
            recyclerView.visibility = View.INVISIBLE
            noResultsMessage.visibility = View.INVISIBLE
        } else {

            RetrofitFunc(this.requireContext()).getItemListByNameSearch(query,
                {call, res ->
                    val dataList = res.body() as List<ItemDto>
                    if(dataList.isEmpty()){
                        noResultsMessage.visibility = View.VISIBLE
                        recyclerView.visibility = View.INVISIBLE
                        searchPromptMessage.visibility = View.INVISIBLE
                    }
                    else{
                        noResultsMessage.visibility = View.INVISIBLE
                        searchPromptMessage.visibility = View.INVISIBLE
                        recyclerView.visibility = View.VISIBLE
                        searchAdapter.itemList.clear()
                        for(data in dataList){
                            searchAdapter.itemList.add(
                                ItemInOutDto(
                                    data.id.toDouble(),
                                    data.name,
                                    data.company.name,
                                    data.thumbNail,
                                    data.container.section,
                                    data.totalNum,
                                    data.totalNum,
                                    data.totalNum
                                )
                            )
                        }
                        searchAdapter.notifyDataSetChanged()
                    }
                })

            /*
            RetrofitClient.retrofit.getItemListByNameSearch(dbHelper.getAccessToken(), query).enqueue(object : retrofit2.Callback<List<ItemDto>>{
                override fun onResponse(
                    call: Call<List<ItemDto>>,
                    response: Response<List<ItemDto>>
                ) {
                    val dataList = response.body() as List<ItemDto>
                    if(dataList.isEmpty()){
                        noResultsMessage.visibility = View.VISIBLE
                        recyclerView.visibility = View.INVISIBLE
                        searchPromptMessage.visibility = View.INVISIBLE
                    }
                    else{
                        noResultsMessage.visibility = View.INVISIBLE
                        searchPromptMessage.visibility = View.INVISIBLE
                        recyclerView.visibility = View.VISIBLE
                        searchAdapter.itemList.clear()
                        for(data in dataList){
                            searchAdapter.itemList.add(
                                ItemInOutDto(
                                    data.id.toDouble(),
                                    data.name,
                                    data.company.name,
                                    data.thumbNail,
                                    data.container.section,
                                    data.totalNum,
                                    data.totalNum,
                                    data.totalNum
                                )
                            )
                        }
                        searchAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<List<ItemDto>>, t: Throwable) {
                }

            })
        */
        }


        searchAdapter.apply {
            itemList.clear()
//            itemList.addAll(filteredList)
            notifyDataSetChanged()
        }
    }
}