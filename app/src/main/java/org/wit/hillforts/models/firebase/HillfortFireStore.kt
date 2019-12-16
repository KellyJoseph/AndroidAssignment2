/*
package org.wit.hillforts.models.firebase

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.AnkoLogger
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.models.HillfortStore

class PlacemarkFireStore(val context: Context) : HillfortStore, AnkoLogger {

    val placemarks = ArrayList<HillfortModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference

    override fun findAll(): List<HillfortModel> {
        return placemarks
    }

    override fun findById(id: Long): HillfortModel? {
        val foundPlacemark: HillfortModel? = placemarks.find { p -> p.id == id }
        return foundPlacemark
    }

    override fun create(placemark: HillfortModel) {
        val key = db.child("users").child(userId).child("placemarks").push().key
        key?.let {
            placemark.fbId = key
            placemarks.add(placemark)
            db.child("users").child(userId).child("placemarks").child(key).setValue(placemark)
        }
    }

    override fun update(hillfort: HillfortModel) {
        var foundHillfort: HillfortModel? = placemarks.find { p -> p.fbId == hillfort.fbId }
        if (foundHillfort != null) {
            foundHillfort.name = hillfort.name
            foundHillfort.description = hillfort.description
            foundHillfort.image = hillfort.image
            foundHillfort.location = hillfort.location
        }

        db.child("users").child(userId).child("placemarks").child(placemark.fbId).setValue(placemark)

    }

    override fun delete(placemark: HillfortModel) {
        db.child("users").child(userId).child("placemarks").child(placemark.fbId).removeValue()
        placemarks.remove(placemark)
    }

    override fun clear() {
        placemarks.clear()
    }

    fun fetchHillforts(placemarksReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(placemarks) { it.getValue<HillfortModel>(HillfortModel::class.java) }
                placemarksReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        placemarks.clear()
        db.child("users").child(userId).child("placemarks").addListenerForSingleValueEvent(valueEventListener)
    }
}

 */