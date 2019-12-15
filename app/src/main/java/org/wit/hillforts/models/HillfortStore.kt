package org.wit.hillforts.models

interface HillfortStore {
    fun findAll(): List<HillfortModel>
    fun findById(id:Long) : HillfortModel?
    fun findAllByUser(user: UserModel): List<HillfortModel>
    fun findVisitedHillfortsByUser(user: UserModel): List<HillfortModel>
    fun create(hillfort: HillfortModel)
    fun update(hillfort: HillfortModel)
    fun delete(hillfort: HillfortModel)
    fun deleteImage(hillfort: HillfortModel)
    fun register(user: UserModel)
    fun login(email: String, password: String): UserModel?
}