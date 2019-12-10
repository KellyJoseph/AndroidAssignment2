package org.wit.hillforts.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.wit.hillforts.helpers.exists
import org.wit.hillforts.helpers.read
import org.wit.hillforts.helpers.write
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

    override fun findAllByUser(user: UserModel): MutableList<HillfortModel> {
        //var filteredHillforts = hillforts.filter{ it.authorId == user.id }
        //var userHillforts = hillforts.retainAll { (it.authorId == user.id) }
        val userHillfortList = mutableListOf<HillfortModel>()
        hillforts.forEach {
            if(it.authorId == user.id) {
                userHillfortList.add(it)
            }
        }
        return userHillfortList
    }

    override fun findVisitedHillfortsByUser(user: UserModel): MutableList<HillfortModel> {
        val visitedUserHillfortList = mutableListOf<HillfortModel>()
        hillforts.forEach {
            if(it.authorId == user.id && it.visited == true) {
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
        //var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        hillforts.remove(hillfort)
        serialize(HILLFORTS_FILE)
    }
    override fun deleteImage(hillfort: HillfortModel, number: Int) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            if (foundHillfort.images.size >= number && foundHillfort.images.isNotEmpty()) {
                foundHillfort.images.removeAt(number)
            }
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
            //foundHillfort.image = hillfort.image
            foundHillfort.images = hillfort.images
            foundHillfort.lat = hillfort.lat
            foundHillfort.lng = hillfort.lng
            foundHillfort.zoom = hillfort.zoom
        }
        serialize(HILLFORTS_FILE)
    }

    private fun serialize(fileType: String) {
        val jsonString = gsonBuilder.toJson(hillforts, listTypeHillfort)
        write(context, fileType, jsonString)
    }

    private fun deserialize(fileType: String) {
        val jsonString = read(context, fileType)
        hillforts = Gson().fromJson(jsonString, listTypeHillfort)
    }
}