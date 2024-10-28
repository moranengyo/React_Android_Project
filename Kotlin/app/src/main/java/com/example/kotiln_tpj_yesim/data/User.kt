package com.example.kotiln_tpj_yesim.data

data class User(val userId: String, val password: String, val role: String)

object UserSession {
    var user: User? = null

    fun isLoggedIn(): Boolean {
        return user != null
    }

    fun login(userId: String, password: String) {
        // retrofit으로 토큰 정보 받아와서 sqllite 혹은 텍스트파일에 저장

        user = User("test", "1234", "basic")
    }

    fun logout() {
        user = null
    }
}