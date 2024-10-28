package com.example.kotiln_tpj_yesim.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.kotiln_tpj_yesim.R
import com.example.kotiln_tpj_yesim.databinding.ItemRequestBinding
import com.example.kotiln_tpj_yesim.dto.PurchaseDto
import com.example.kotiln_tpj_yesim.dto.Util

class RequestAdapter(
    val context: Context,
    var requestList: List<PurchaseDto>,
    var role: String = "구매관리자"
) : RecyclerView.Adapter<RequestAdapter.Holder>() {

    class Holder(val binding: ItemRequestBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val request = requestList[position]

        with(holder.binding) {
            textViewDate.text = Util.formatDateTime(request.reqTime)
            textViewItem.text = request.title
            textViewStatus.text = Util.translateEnum(request.approvedStatus)
            textViewName.text = "addmin"
            textViewCount.text = "+ ${request.reqNum}"

            if (role == "구매관리자") {
                // 구매관리자
                textViewName.visibility = View.GONE
            } else {
                // 구매부장
                textViewName.visibility = android.view.View.VISIBLE
            }

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