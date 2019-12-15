package org.wit.hillforts.room

import androidx.room.*
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.models.UserModel

@Dao
interface HillfortDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(hillfort: HillfortModel)

    @Query("SELECT * FROM HillfortModel")
    fun findAll(): List<HillfortModel>

    @Query("select * from HillfortModel where id = :id")
    fun findById(id: Long): HillfortModel

    @Query("select * from HillfortModel where authorId = :authorId")
    fun findAllByUser(authorId: Long): List<HillfortModel>

    @Query("select * from HillfortModel where authorId = :authorId and visited = true")
    fun findVisitedHillfortsByUser(authorId: Long): List<HillfortModel>

    @Update
    fun update(hillfort: HillfortModel)

    @Delete
    fun deleteHillfort(hillfort: HillfortModel)
}