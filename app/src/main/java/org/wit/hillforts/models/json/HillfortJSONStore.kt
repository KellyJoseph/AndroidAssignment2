package org.wit.hillforts.models.json

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.wit.hillforts.helpers.exists
import org.wit.hillforts.helpers.read
import org.wit.hillforts.helpers.write
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.models.HillfortStore
import org.wit.hillforts.models.UserModel
import java.util.*
import kotlin.collections.ArrayList

val HILLFORTS_FILE = "hillforts.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listTypeHillfort = object : TypeToken<ArrayList<HillfortModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class HillfortJSONStore: HillfortStore, AnkoLogger {
    val context: Context
    var users = mutableListOf<UserModel>()
    var hillforts = mutableListOf<HillfortModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, HILLFORTS_FILE)) {
            deserialize(HILLFORTS_FILE)
        }
    }

    override fun findAll(): MutableList<HillfortModel> {
        return hillforts
    }

    override fun findAllByUser(userId: Long): MutableList<HillfortModel> {
        //var filteredHillforts = hillforts.filter{ it.authorId == user.id }
        //var userHillforts = hillforts.retainAll { (it.authorId == user.id) }
        val userHillfortList = mutableListOf<HillfortModel>()
        hillforts.forEach {
            if(it.authorId == userId) {
                userHillfortList.add(it)
            }
        }
        return userHillfortList
    }


    override fun findVisitedHillfortsByUser(userId: Long): MutableList<HillfortModel> {
        val visitedUserHillfortList = mutableListOf<HillfortModel>()
        hillforts.forEach {
            if(it.authorId == userId && it.visited == true) {
                visitedUserHillfortList.add(it)
            }
        }
        return visitedUserHillfortList
    }

    override fun create(hillfort: HillfortModel) {
        hillfort.id = generateRandomId()
        hillforts.add(hillfort)
        serialize(HILLFORTS_FILE)
    }
    override fun delete(hillfort: HillfortModel) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            hillforts.remove(hillfort)
            serialize(HILLFORTS_FILE)
        }
    }

    override fun findById(id:Long) : HillfortModel? {
        val foundHillfort: HillfortModel? = hillforts.find { it.id == id }
        return foundHillfort
    }

    override fun clear() {
        hillforts.clear()
    }
    override fun deleteImage(hillfort: HillfortModel) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            hillfort.image = ""
        }
        serialize(HILLFORTS_FILE)
    }

    override fun login(email: String, password: String) : UserModel {
        return UserModel()
    }

    override fun register(user: UserModel) {
        user.id = generateRandomId()
        users.add(user)
        serialize(HILLFORTS_FILE)
    }

    override fun update(hillfort: HillfortModel) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            foundHillfort.name = hillfort.name
            foundHillfort.description = hillfort.description
            foundHillfort.notes = hillfort.notes
            foundHillfort.visited = hillfort.visited
            foundHillfort.visitedDate = hillfort.visitedDate
            foundHillfort.image = hillfort.image
            foundHillfort.location = hillfort.location
        }
        serialize(HILLFORTS_FILE)
    }

    private fun serialize(fileType: String) {
        val jsonString = gsonBuilder.toJson(hillforts,
            listTypeHillfort
        )
        write(context, fileType, jsonString)
    }

    private fun deserialize(fileType: String) {
        val jsonString = read(context, fileType)
        hillforts = Gson().fromJson(jsonString, listTypeHillfort)
    }
}