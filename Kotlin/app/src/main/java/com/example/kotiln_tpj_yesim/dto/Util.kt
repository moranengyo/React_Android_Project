package com.example.kotiln_tpj_yesim.dto

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Util {
    fun translateEnum(status: String): String {
        return when (status) {
            "APPROVE" -> "승인"
            "CANCEL" -> "반려"
            "WAIT" -> "요청"
            "UNCONFIRMED" -> "미확인"
            "IN_STOCK" -> "입고"
            else -> "알 수 없음"
        }
    }

    fun formatDateTime(dateTimeString: String): String {
        if(dateTimeString.isEmpty()) return ""
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val targetFormat = SimpleDateFormat("MM.dd (E) a h:mm", Locale.getDefault())

        val date: Date? = originalFormat.parse(dateTimeString)
        return date?.let { targetFormat.format(it) } ?: ""
    }

}

// status
// <관리자>
// 미확인(적정수량미만)

// 요청(결재 상신, 부장 미확인)
// 반려
// 승인
// -------------------------------------------
// <부장>
// 요청

// 반려
// 승인