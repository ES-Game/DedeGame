package com.dede.dedegame

import com.dede.dedegame.repo.UrlUtils

object DomainManager {
    private var currentDomain: String? = null
    const val CONFIG_URL = "https://storage.googleapis.com/sdk-api-vn/"

    fun getCurrentDomain(): String {
        return if (currentDomain != null) {
            UrlUtils.addWwwToDomain(currentDomain)
        } else {
            ""
        }
    }

    fun setCurrentDomain(domain: String) {
        currentDomain = domain
        // Lưu vào SharedPreferences để persist
        DedeSharedPref.saveDomain(domain)
    }

    fun initFromCache() {
        currentDomain = DedeSharedPref.getSavedDomain()
    }
}