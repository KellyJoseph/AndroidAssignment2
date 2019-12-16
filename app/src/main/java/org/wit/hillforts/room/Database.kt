package org.wit.hillforts.room

import androidx.room.Database
import androidx.room.RoomDatabase
import org.wit.hillforts.models.HillfortModel

@Database(entities = arrayOf(HillfortModel::class), version = 2,  exportSchema = false)

abstract class Database : RoomDatabase() {

    abstract fun HillfortDao(): HillfortDao
}