package org.wit.hillforts.models

import android.os.Parcelable
import androidx.room.Embedded
import kotlinx.android.parcel.Parcelize
import androidx.room.Entity
import androidx.room.PrimaryKey


@Parcelize
@Entity
data class HillfortModel(@PrimaryKey(autoGenerate = true)var id: Long = 0,
                         var fbId: String = "",
                         var name: String = "",
                         var description: String = "",
                         var notes: String = "",
                         var image: String = "",
                         var visitedDate: String = "",
                         var authorId: String = "",
                         var visited: Boolean = false,
                         @Embedded var location: Location = Location()) : Parcelable

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