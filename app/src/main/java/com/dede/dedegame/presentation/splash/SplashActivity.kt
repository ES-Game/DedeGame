package com.dede.dedegame.presentation.splash

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.dede.dedegame.BuildConfig
import com.dede.dedegame.DedeSharedPref
import com.dede.dedegame.DomainManager
import com.dede.dedegame.DomainManager.CONFIG_URL
import com.dede.dedegame.R
import com.dede.dedegame.presentation.common.LogUtil
import com.dede.dedegame.presentation.home.HomeActivity
import com.dede.dedegame.presentation.login.LoginActivity
import com.dede.dedegame.repo.UrlUtils
import com.dede.dedegame.repo.domain.ConfigApiService
import com.dede.dedegame.repo.network.ApiService
import com.quangph.base.mvp.ICommand
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Layout(R.layout.activity_splash)
class SplashActivity : JetActivity<SplashView>() {

    companion object {
        const val TIME_DELAY_SPLASH: Long = 1000
    }

    override fun onStart() {
        super.onStart()
        supportActionBar?.hide()
    }

    override fun onPresenterReady() {
        super.onPresenterReady()

        DomainManager.initFromCache()

        lifecycleScope.launch {
            val currentDomain = DomainManager.getCurrentDomain()

            if (!currentDomain.isNullOrEmpty()) {
                // Có domain đã lưu, test domain này trước
                LogUtil.getInstance().d("Testing existing domain: $currentDomain")
                val isDomainValid = testDomain(currentDomain)

                if (isDomainValid) {
                    // Domain hiện tại vẫn hoạt động, tiếp tục splash
                    LogUtil.getInstance().d("Existing domain is valid, proceeding to splash")
                    delaySplash()
                } else {
                    // Domain hiện tại không hoạt động, tìm domain mới
                    LogUtil.getInstance().d("Existing domain is invalid, checking for new domain")
                    val newDomainValid = checkAndSetValidDomain()

                    if (!newDomainValid) {
                        LogUtil.getInstance().e("No valid domain found")
                        showNoDomainAlert()
                    } else {
                        delaySplash()
                    }
                }
            } else {
                // Không có domain đã lưu, tìm domain mới
                LogUtil.getInstance().d("No existing domain, checking for valid domain")
                val isDomainValid = checkAndSetValidDomain()

                if (!isDomainValid) {
                    LogUtil.getInstance().e("No valid domain found")
                    showNoDomainAlert()
                } else {
                    delaySplash()
                }
            }
        }
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {

        }
    }

    private fun delaySplash() {
        if (DedeSharedPref.getUserInfo() == null) {
            Handler(Looper.getMainLooper()).postDelayed({
                navigateToLogin()
            }, TIME_DELAY_SPLASH)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                navigateToHome()
            }, TIME_DELAY_SPLASH)
        }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun showNoDomainAlert() {
        AlertDialog.Builder(this)
            .setTitle("Lỗi kết nối")
            .setMessage("Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng và thử lại.")
            .setPositiveButton("Thử lại") { _, _ ->
                // Thử lại việc kiểm tra domain
                lifecycleScope.launch {
                    val currentDomain = DomainManager.getCurrentDomain()

                    if (!currentDomain.isNullOrEmpty()) {
                        val isDomainValid = testDomain(currentDomain)

                        if (isDomainValid) {
                            delaySplash()
                        } else {
                            val newDomainValid = checkAndSetValidDomain()
                            if (!newDomainValid) {
                                showNoDomainAlert()
                            } else {
                                delaySplash()
                            }
                        }
                    } else {
                        val isDomainValid = checkAndSetValidDomain()
                        if (!isDomainValid) {
                            showNoDomainAlert()
                        } else {
                            delaySplash()
                        }
                    }
                }
            }
            .setNegativeButton("Thoát") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private suspend fun checkAndSetValidDomain(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val logging = HttpLoggingInterceptor()
                if (BuildConfig.DEBUG) {
                    logging.level = HttpLoggingInterceptor.Level.BODY
                } else {
                    logging.level = HttpLoggingInterceptor.Level.NONE
                }

                val builder = OkHttpClient.Builder().addInterceptor(logging)
                builder.connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)

                // Lấy config từ server
                val configRetrofit = Retrofit.Builder()
                    .baseUrl(CONFIG_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build()

                val configService = configRetrofit.create(ConfigApiService::class.java)
                val configResponse = configService.getDomainConfig()

                if (configResponse.isSuccessful) {
                    val domains = configResponse.body()?.domains ?: return@withContext false
                    val validDomain = findFirstValidDomainSequential(domains)

                    if (validDomain.isNotEmpty()) {
                        LogUtil.getInstance()
                            .e("DomainCheck" + "DOMAIN su dung la =========>: $validDomain")
                        DomainManager.setCurrentDomain(validDomain)
                        return@withContext true
                    }
                }
                false
            } catch (e: Exception) {
                LogUtil.getInstance().e("DomainCheck" + "=====Error checking domains: ${e.message}")
                // Nếu có lỗi, kiểm tra xem có domain đã lưu trước đó không
                val currentDomain = DomainManager.getCurrentDomain()
                !currentDomain.isNullOrEmpty()
            }
        }
    }

    // Thực hiện tuần tự thay vì song song
    private suspend fun findFirstValidDomainSequential(domains: List<String>): String {
        for (domain in domains) {
            LogUtil.getInstance().d("🔍 Checking domain: $domain")
            val isValid = testDomain(UrlUtils.addWwwToDomain(domain))
            if (isValid) {
                LogUtil.getInstance().d("✅ Found valid domain: $domain")
                return domain // trả về ngay domain hợp lệ đầu tiên
            }
        }
        LogUtil.getInstance().d("❌ No valid domain found")
        return ""
    }

    private suspend fun testDomain(domain: String): Boolean = withContext(Dispatchers.IO) {
        val fullDomain = if (domain.endsWith("/")) domain else "$domain/"
        val baseUrl = fullDomain + "api/v1/" // đảm bảo có dấu "/" đúng

        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        return@withContext try {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            val api = retrofit.create(ApiService::class.java)
            val response = api.checkDomain() // suspend function gọi API

            if (response.isSuccessful && response.code() == 200) {
                val contentType = response.headers()["Content-Type"] ?: ""
                val isJson = contentType.contains("application/json", ignoreCase = true)
                val hasBody = response.body() != null

                LogUtil.getInstance()
                    .d("✅ Domain $baseUrl - Status: ${response.code()}, Content-Type: $contentType, Has Body: $hasBody")

                if (isJson && hasBody) {
                    true
                } else {
                    LogUtil.getInstance()
                        .d("❌ Domain $baseUrl - Invalid response format (not JSON or no body)")
                    false
                }
            } else {
                LogUtil.getInstance()
                    .d("❌ Domain $baseUrl response error: ${response.code()} - ${response.message()}")
                false
            }
        } catch (e: Exception) {
            LogUtil.getInstance().d("❌ Exception checking domain $baseUrl: ${e.message}")
            false
        }
    }
}