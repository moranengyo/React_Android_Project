package com.example.kotiln_tpj_yesim.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.example.kotiln_tpj_yesim.R
import com.example.kotiln_tpj_yesim.databinding.ItemRightRecycleViewBinding
import com.example.kotiln_tpj_yesim.dto.ItemInOutDto
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class UsageAdapter(
    var itemList: MutableList<ItemInOutDto>,

    var blueTitle: String? = null,
    var pinkTitle: String? = null,
    var blueSubTitle: String? = null,
    var showImage: Boolean = true,

    ) : RecyclerView.Adapter<UsageAdapter.Holder>() {

        private lateinit var holder:Holder

    inner class Holder(val binding: ItemRightRecycleViewBinding) : RecyclerView.ViewHolder(binding.root) {
        var imageView =     binding.imageView
        var titleText =     binding.textViewTitle
        var subTitleText = binding.textViewSubTitle
        var firstCount =    binding.textViewFirstCount
        var firstSubTitle = binding.textViewFirstTitle
        var dividerView =   binding.viewDivider
        var secondCount =   binding.textViewSecondCount
        var viewDivider = binding.viewDivider
        var secondSubTitle = binding.textViewSecondTitle

    }

    // 새로운 ViewHolder를 생성하는 메서드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        holder = Holder(
            ItemRightRecycleViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


        if (!showImage) {
            holder.imageView.visibility = View.GONE
        }
        holder.imageView.visibility = if (!showImage) View.GONE else View.VISIBLE
        holder.firstSubTitle.text = blueSubTitle ?: "입고"


        if (blueTitle == null) {
            holder.binding.LinearLayoutFirst.visibility = View.GONE
            holder.dividerView.visibility = View.GONE
        } else if (pinkTitle == null) {
            holder.binding.LinearLayoutSecond.visibility = View.GONE
            holder.dividerView.visibility = View.GONE
        }

        return holder
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = itemList[position]

        Log.d("thumbnail", "${item.thumbnail}")

        Picasso.get()
            .load(item.thumbnail)
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .error(R.drawable.ic_move_inbox)
            .into(holder.binding.imageView)

        holder.titleText.text = item.itemName
        holder.subTitleText.text = item.companyName
        holder.firstCount.text = itemList[position].totalReqNum.toString()
        holder.secondCount.text = itemList[position].totalUsageNum.toString()

        if(!showImage) {
            holder.imageView.visibility = View.GONE
            holder.firstSubTitle.visibility = View.GONE
            holder.firstCount.visibility = View.GONE
            holder.viewDivider.visibility = View.GONE
        }else{
            holder.imageView.visibility = View.VISIBLE
            holder.firstSubTitle.visibility = View.VISIBLE
            holder.firstCount.visibility = View.VISIBLE
            holder.viewDivider.visibility = View.VISIBLE
        }
    }




}