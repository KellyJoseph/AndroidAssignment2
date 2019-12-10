package org.wit.hillforts.models

interface UserStore {
    fun findAll(): List<UserModel>
    fun create(hillfort: UserModel)
    fun update(hillfort: UserModel)
    fun delete(hillfort: UserModel)
    fun register(user: UserModel)
    fun login(email: String, password: String): UserModel?
}