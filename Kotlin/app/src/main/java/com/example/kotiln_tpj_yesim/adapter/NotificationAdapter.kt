package com.example.kotiln_tpj_yesim.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.kotiln_tpj_yesim.R
import com.example.kotiln_tpj_yesim.databinding.ItemNotificationBinding
import com.example.kotiln_tpj_yesim.dto.PurchaseDto
import com.example.kotiln_tpj_yesim.dto.Util
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class NotificationAdapter(
    val context: Context,
    var requestList: List<PurchaseDto>,
    var role: String = "구매관리자"
) : RecyclerView.Adapter<NotificationAdapter.Holder>() {

    class Holder(val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        if(role.equals("구매관리자")){

        }

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val request = requestList[position]

        with(holder.binding) {
            textViewDate.text = Util.formatDateTime(request.reqTime)
            textViewItem.text = request.title

            if(role.equals("구매관리자")) {
                textViewStatus.text = "구매 요망"
                textViewName.text = ""
                textViewCount.text = "${request.reqNum}"
                additional.text = "재고 수량"
            }
            else {
                textViewStatus.text = Util.translateEnum(request.approvedStatus)
                textViewName.text = "admin"
                textViewCount.text = "+ ${request.reqNum}"
            }
            Picasso.get()
                .load(request.item.thumbNail)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .error(R.drawable.ic_move_inbox)
                .into(holder.binding.imageView)

            if (request.newYn.equals("Y")) {
                textViewNew.visibility = android.view.View.VISIBLE
            } else {
                textViewNew.visibility = android.view.View.GONE
            }


            val statusColor = when (request.approvedStatus) {
                "UNCONFIRMED" -> ContextCompat.getColor(context, R.color.blue)
                "WAIT" -> ContextCompat.getColor(context, R.color.yellow)
                "CANCEL" -> ContextCompat.getColor(context, R.color.pink)
                "APPROVE" -> ContextCompat.getColor(context, R.color.green)
                else -> ContextCompat.getColor(context, R.color.gray)
            }

            // 배경 색상 변경 (backgroundTint 적용)
            textViewStatus.backgroundTintList = ColorStateList.valueOf(statusColor)
        }
    }

    // 아이템 총 개수 반환
    override fun getItemCount(): Int {
        return requestList.size
    }

}