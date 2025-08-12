package com.akakou.zkbanandroid

import android.content.Context
import android.util.Log
import androidx.core.util.toRange
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import zkbancrypto.Zkbancrypto
import java.util.concurrent.TimeUnit

val UNIT = TimeUnit.SECONDS

class UpdateWorker(val appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext,
    params
) {
    companion object {
        var debug: Long = 1
        fun run(context: Context) {
            val hour = (0..30).random()
//            val hour = (0..24).random()

            val workManager = WorkManager.getInstance(context)
            val workRequest = OneTimeWorkRequestBuilder<UpdateWorker>()
                .setInitialDelay(duration = hour.toLong(), timeUnit = UNIT)
                .build()

            workManager
                .beginWith(workRequest)
                .enqueue()
        }
    }

    override suspend fun doWork(): Result {
        Log.d("debug", "${UpdateWorker.debug}")
        val prover = Prover()
        try {
            debug ++
            prover.update(appContext)
        } catch (e: Exception){
            debug --
            Log.d("zk-ban", e.message.toString())
        }

        run(appContext)

        return Result.success()
    }
}
