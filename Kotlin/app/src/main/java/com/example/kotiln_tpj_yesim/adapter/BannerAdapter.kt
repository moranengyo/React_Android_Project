package com.example.kotiln_tpj_yesim.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.kotiln_tpj_yesim.R
import com.example.kotiln_tpj_yesim.databinding.ItemBannerBinding
import com.example.kotiln_tpj_yesim.fragments.HomeFragment
import kotlin.math.sign

interface OnBannerClickListener {
    fun onBannerClick(isUsage: Boolean)
}

class BannerAdapter(
    val kind: Int,
    val isBackStack: Boolean,
    var usage: Boolean,
    var firstCount: String,
    var secondCount: String,
    val listener: OnBannerClickListener,
    val bannerTitle: String,
    val colorId: Int
) : RecyclerView.Adapter<BannerAdapter.Holder>() {
    class Holder(val binding: ItemBannerBinding) : RecyclerView.ViewHolder(binding.root)


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val holder =
            Holder(ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        holder.binding.apply {
            textViewTitle.text = bannerTitle

            if (isBackStack || !(kind > 1)) {
                viewDivider.visibility = View.GONE
                LinearLayoutFirst.setOnTouchListener { view, motionEvent -> true }
                LinearLayoutSecond.setOnTouchListener { view, motionEvent -> true }

                if (usage) {
                    LinearLayoutFirst.visibility = View.GONE
                    container.setBackgroundColor(colorId)
                } else {
                    LinearLayoutSecond.visibility = View.GONE
                    container.setBackgroundColor(colorId)
                }
            }

        }

        holder.binding.LinearLayoutFirst.setOnClickListener {
            listener.onBannerClick(false)
        }
        holder.binding.LinearLayoutSecond.setOnClickListener {
            listener.onBannerClick(true)
        }

        return holder
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val dates = arrayOf("오늘", "이번달", "올해")


        holder.binding.apply {
            textViewFirstCount.text = firstCount
            textViewFirstTitle.text = "총 입고량"
            textViewSecondCount.text = secondCount
            textViewSecondTitle.text = "총 사용량"
        }

        holder.binding.textViewDate.text = dates[position]
    }


    fun setFirstCount(firstCnt:Int){
        firstCount = firstCnt.toString()
    }

    fun setSecondCount(secondCnt:Int){
        secondCount = secondCnt.toString()
    }
}
