package com.dede.dedegame.repo.domain

import com.dede.dedegame.domain.model.DomainConfig
import retrofit2.Response
import retrofit2.http.GET

interface ConfigApiService {
    @GET("dede_app_domains.json")
    suspend fun getDomainConfig(): Response<DomainConfig>
}