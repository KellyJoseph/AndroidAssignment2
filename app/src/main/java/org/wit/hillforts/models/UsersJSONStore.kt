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

val USERS_FILE = "users.json"
val gsonBuilderUsers = GsonBuilder().setPrettyPrinting().create()
val listTypeUsers = object  : TypeToken<ArrayList<UserModel>>() {}.type

fun generateRandomIdUsers(): Long {
    return Random().nextLong()
}

class UsersJSONStore: UserStore, AnkoLogger {
    val context: Context
    var users = mutableListOf<UserModel>()


    constructor (context: Context) {
        this.context = context
        if (exists(context, USERS_FILE)) {
            deserialize(USERS_FILE)
        }
    }

    override fun findAll(): MutableList<UserModel> {
        return users
    }


    override fun create(user: UserModel) {
        user.id = generateRandomIdUsers()
        users.add(user)
        serialize(USERS_FILE)
    }
    override fun delete(user: UserModel) {
        var foundUser: UserModel? = users.find { p -> p.id == user.id }
        users.remove(foundUser)
        serialize(USERS_FILE)
    }

    override fun login(email: String, password: String) : UserModel? {
        var foundUser: UserModel? = users.find { p -> p.email == email}
        if (foundUser?.password == password) {
            return foundUser
        }
        return null
    }
    override fun register(user: UserModel) {
        var foundUser: UserModel? = users.find { p -> p.email == user.email}
        if (foundUser == null) {
            user.id = generateRandomId()
            users.add(user)
            serialize(USERS_FILE)
        }
    }

    override fun update(user: UserModel) {
        var foundUser: UserModel? = users.find { p -> p.id == user.id }
        if (foundUser != null) {
            foundUser.firstName = user.firstName
            foundUser.lastName = user.lastName
            foundUser.email = user.email
            foundUser.password = user.password
        }
        serialize(USERS_FILE)
    }

    private fun serialize(fileType: String) {
        val jsonString = gsonBuilderUsers.toJson(users, listTypeUsers)
        write(context, fileType, jsonString)
    }

    private fun deserialize(fileType: String) {
        val jsonString = read(context, fileType)
        users = Gson().fromJson(jsonString, listTypeUsers)
    }
}