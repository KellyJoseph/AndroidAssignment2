package org.wit.hillforts.models

import android.os.Parcelable
import androidx.room.Embedded
import kotlinx.android.parcel.Parcelize
import androidx.room.Entity
import androidx.room.PrimaryKey


@Parcelize
@Entity
data class HillfortModel(@PrimaryKey(autoGenerate = true)var id: Long = 0,
                         var name: String = "",
                         var description: String = "",
                         var notes: String = "",
                         var image: String = "",
                         var lat: Double = 0.0,
                         var lng: Double = 0.0,
                         var zoom: Float = 0f,
                         var visitedDate: String = "",
                         var authorId: Long = 0,
                         var visited: Boolean = false ) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable

@Parcelize
@Entity
data class UserModel(var id: Long = 0,
                var firstName: String = "",
                var lastName: String = "",
                var email: String = "",
                var password: String = "") : Parcelable