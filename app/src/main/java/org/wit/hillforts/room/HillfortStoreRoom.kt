package org.wit.hillforts.room

import android.content.Context
import androidx.room.Room
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.models.HillfortStore
import org.wit.hillforts.models.UserModel

class HillfortStoreRoom(val context: Context) : HillfortStore {

    var dao: HillfortDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.HillfortDao()
    }

    override fun findAll(): List<HillfortModel> {
        return dao.findAll()
    }

    override fun clear() {
    }

    override fun findAllByUser(userId: String): List<HillfortModel> {
        var authorId = userId
        return dao.findAllByUser(authorId)
    }

    override fun findVisitedHillfortsByUser(userId: String): List<HillfortModel> {
        var authorId = userId
        return dao.findVisitedHillfortsByUser(authorId)
    }

    override fun deleteImage(hillfort: HillfortModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findById(id: Long): HillfortModel? {
        return dao.findById(id)
    }

    override fun create(hillfort: HillfortModel) {
        dao.create(hillfort)
    }

    override fun update(hillfort: HillfortModel) {
        dao.update(hillfort)
    }

    override fun delete(hillfort: HillfortModel) {
        dao.deleteHillfort(hillfort)
    }

    override fun register(user: UserModel) {
        TODO("not implemented") //To cha
    }

    override fun login(email: String, password: String): UserModel? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}