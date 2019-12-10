package org.wit.hillforts.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillforts.models.*

class MainApp: Application(), AnkoLogger {

    //val hillforts = ArrayList<HillfortModel>()
    //val hillforts = HillfortkMemStore()
    lateinit var hillforts: HillfortStore
    lateinit var users: UserStore
    lateinit var loggedInUser: UserModel
    lateinit var visitedHillforts: List<HillfortModel>

    override fun onCreate() {
        super.onCreate()
        //hillforts = HillfortkMemStore()
        users = UsersJSONStore(applicationContext)
        hillforts = HillfortJSONStore(applicationContext)
        info("Hillforts started")
    }
}