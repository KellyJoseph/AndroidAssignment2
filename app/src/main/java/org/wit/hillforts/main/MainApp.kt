package org.wit.hillforts.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.models.HillfortStore
import org.wit.hillforts.models.UserStore
import org.wit.hillforts.models.firebase.HillfortsFireStore


class MainApp: Application(), AnkoLogger {

    //val hillforts = ArrayList<HillfortModel>()
    //val hillforts = HillfortkMemStore()
    lateinit var hillforts: HillfortStore
    lateinit var users: UserStore
    //var loggedInUserId: Long = 0
    lateinit var filteredHillforts: MutableList<HillfortModel>

    override fun onCreate() {
        super.onCreate()
        //hillforts = HillfortMemStore()
        //users = UsersJSONStore(applicationContext)
        //hillforts = HillfortJSONStore(applicationContext)
        //hillforts = HillfortStoreRoom(applicationContext)
        //hillforts = HillfortFireStore(applicationContext)
        hillforts = HillfortsFireStore(applicationContext)
        info("Hillforts started")
    }
}