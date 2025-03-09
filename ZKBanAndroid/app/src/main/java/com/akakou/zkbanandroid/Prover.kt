package com.akakou.zkbanandroid

import android.net.Uri
import com.akakou.proverkit.AbstractProver
import com.akakou.zkbanandroid.store.AppDatabase
import com.akakou.zkbanandroid.store.AuthLog
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class Prover(db: AppDatabase, uri: Uri): AbstractProver(uri) {
    var todayInt : Int
    var allCountPettern = listOf(1,2,3)
    var availabeCounter : List<Int?> = listOf()
    val authLogDao = db.authLogDao()


    init {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        todayInt = today.format(formatter).toInt()
    }

    override suspend fun prepare() {
        val authLogs = authLogDao.getAllByDomainAndDate(uri.host.toString(), todayInt)

        val counters = authLogs.map { it -> it.counter }
        availabeCounter = allCountPettern.filter { it -> it !in counters }
    }

    override suspend fun needUserCheck(): Boolean {
        return availabeCounter.size != allCountPettern.size
    }

    override suspend fun prove(): String {
        val counter = availabeCounter.random()
        val log = AuthLog(
            domain = uri.host,
            date = todayInt,
            counter = counter
        )

        authLogDao.insertAll(log)
        return "hi! this is zk-ban"
    }
}

