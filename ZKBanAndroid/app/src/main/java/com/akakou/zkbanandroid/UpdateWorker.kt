package com.akakou.zkbanandroid

import android.content.Context
import android.util.Log
import androidx.core.util.toRange
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
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

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workManager = WorkManager.getInstance(context)
            val workRequest = OneTimeWorkRequestBuilder<UpdateWorker>()
                .setInitialDelay(duration = hour.toLong(), timeUnit = UNIT)
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    30,
                    TimeUnit.SECONDS
                )
                .build()

            workManager.enqueueUniqueWork(
                "update-worker",
                ExistingWorkPolicy.APPEND_OR_REPLACE,
                workRequest
            )
        }
    }

    override suspend fun doWork(): Result {
        Log.d("debug", "${UpdateWorker.debug}")
        val prover = Prover()
        var result = false
        try {
            debug ++
            prover.update(appContext)
            result = true
        } catch (e: Exception){
            debug --
            Log.d("zk-ban", e.message.toString())
            result = false
        } finally {
            run(appContext)
        }

        if (result){
            return Result.success()
        } else {
            return Result.retry()
        }
    }
}
