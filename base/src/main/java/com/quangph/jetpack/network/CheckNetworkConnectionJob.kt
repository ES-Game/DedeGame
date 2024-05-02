package com.quangph.jetpack.network

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.net.HttpURLConnection
import java.net.URL
import java.text.DecimalFormat

/**
 * Check network velocity by pinging to google.com
 */
class CheckNetworkConnectionJob(private val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    companion object {
        private val LOW_SPEED_CONNECTION = 150 //KB/s

        val NETWORK_STATE_ACTION = "NETWORK_STATE_ACTION"
        val NETWORK_STATE_KEY = "NETWORK_STATE_KEY"
    }

    private var isInternetAvailable = false

    override fun doWork(): Result {
        Log.d("NETWORK_SERVICE", "doWork")
//        NetworkService.enqueueWork(MBHApplication.getContext(), Intent(MBHApplication.getContext(), NetworkService::class.java))

        isInternetAvailable = false

        val startTime = System.currentTimeMillis();
        Log.e("NETWORK_SERVICE", "doInBackground: StartTime $startTime");

        Log.e("NETWORK_SERVICE", "doInBackground: ${"https://www.google.com"}")

        val url = URL("https://www.google.com")

        val connection = url.openConnection() as HttpURLConnection

        val inputsStream = connection.inputStream
        val dataSize = inputsStream.readBytes().size / 1024

        val endTime = System.currentTimeMillis()

        Log.e("NETWORK_SERVICE", "doInBackground: EndTime $endTime");

        val takenTime = endTime - startTime
        val s = takenTime.toDouble() / 1000
        val speed = dataSize / s
        Log.e(
            "NETWORK_SERVICE",
            "result: " + "" + DecimalFormat("##.##").format(speed) + "kb/second" + " ${takenTime / 1000.0}"
        )
        isInternetAvailable = speed >= LOW_SPEED_CONNECTION

        Log.e(
            "NETWORK_SERVICE",
            "result job: $isInternetAvailable"
        )

        context.sendBroadcast(Intent(NETWORK_STATE_ACTION).apply {
            this.putExtra(NETWORK_STATE_KEY, isInternetAvailable)
        })

        return Result.success()
    }
}