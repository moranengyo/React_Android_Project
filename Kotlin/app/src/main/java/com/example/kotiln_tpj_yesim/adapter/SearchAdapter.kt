package com.example.kotiln_tpj_yesim.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotiln_tpj_yesim.R
import com.example.kotiln_tpj_yesim.databinding.ItemSearchBinding
import com.example.kotiln_tpj_yesim.dto.ItemInOutDto
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class SearchAdapter(
    var itemList: MutableList<ItemInOutDto>,
    var role: Int

) : RecyclerView.Adapter<SearchAdapter.Holder>() {

    class Holder(val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        var imageView = binding.imageView
        var titleTextView = binding.textViewTitle
        var subTitleTextView = binding.textViewSubTitle
        var rightTextView = binding.textViewFirstCount
        var rightSubTextView = binding.textViewFirstTitle
    }

    // 새로운 ViewHolder를 생성하는 메서드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val holder = Holder(
            ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        return holder
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = itemList[position]

        Picasso.get()
            .load(item.thumbnail)
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .error(R.drawable.ic_move_inbox)
            .into(holder.binding.imageView)

        holder.titleTextView.text = item.itemName

        if (role == 1) {
            holder.subTitleTextView.text = item.companyName
            holder.rightTextView.text = "A창고"
            holder.rightSubTextView.text = item.containerSection
        } else {
            holder.subTitleTextView.text = item.companyName + " | " + item.containerSection
            holder.rightTextView.text = item.totalNum.toString()
            holder.rightSubTextView.text = "총 재고"
        }

    }


}