package com.dede.dedegame.repo

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

object UrlUtils {
    /**
     * Thêm "www." vào hostname của URL nếu chưa có.
     * Nếu URL không hợp lệ hoặc null, trả về chuỗi rỗng.
     */
    fun addWwwToDomain(url: String?): String {
        if (url.isNullOrBlank()) return ""

        val httpUrl = url.toHttpUrlOrNull() ?: return ""
        val host = httpUrl.host

        val newHost = if (host.startsWith("www.", ignoreCase = true)) {
            host
        } else {
            "www.$host"
        }

        val newUrl = httpUrl.newBuilder()
            .host(newHost)
            .build()

        return newUrl.toString()
    }
}
