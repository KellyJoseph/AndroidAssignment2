package org.wit.hillforts.models

interface HillfortStore {
    fun findAll(): List<HillfortModel>
    fun findById(id:Long) : HillfortModel?
    fun findAllByUser(userId: String): List<HillfortModel>
    fun findVisitedHillfortsByUser(userId: String): List<HillfortModel>
    fun create(hillfort: HillfortModel)
    fun update(hillfort: HillfortModel)
    fun delete(hillfort: HillfortModel)
    fun deleteImage(hillfort: HillfortModel)
    fun register(user: UserModel)
    fun login(email: String, password: String): UserModel?
    fun clear()
}