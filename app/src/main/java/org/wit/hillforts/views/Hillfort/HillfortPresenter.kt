package org.wit.hillforts.views.Hillfort

import android.content.Intent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.intentFor
import org.wit.hillforts.helpers.showImagePicker
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.Location
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.views.BasePresenter
import org.wit.hillforts.views.BaseView
import org.wit.hillforts.views.Map.MapView

class HillfortPresenter(view: BaseView): BasePresenter(view) {

    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
    var map: GoogleMap? = null
    var hillfort = HillfortModel()
    var location = Location(52.245696, -7.139102, 15f)
    var edit = false

    init {
        //app = view.application as MainApp
        if(view.intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = view.intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
            view.showHillfort(hillfort)
        } else {
            hillfort.lat = location.lat
            hillfort.lng = location.lng
        }
    }
    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(hillfort.lat, hillfort.lng)
    }

    fun locationUpdate(lat: Double, lng: Double) {
        hillfort.lat = lat
        hillfort.lng = lng
        hillfort.zoom = 15f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(hillfort.name).position(LatLng(hillfort.lat, hillfort.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(hillfort.lat, hillfort.lng), hillfort.zoom))
        view?.showHillfort(hillfort)
    }

    fun doAddOrSave(name: String, description: String, notes: String, authorId: Long, visited: Boolean, visitedDate: String) {
        hillfort.name = name
        hillfort.description = description
        hillfort.notes = notes
        hillfort.authorId = authorId
        hillfort.visited = visited
        hillfort.visitedDate = visitedDate
        if(edit) {
            app.hillforts.update(hillfort)
        } else {
            app.hillforts.create(hillfort)
        }
        view?.finish()
    }

    fun doCancel() {
        view?.finish()
    }
    fun doDelete() {
        app.hillforts.delete(hillfort)
        view?.finish()
    }
    fun doDeleteImage(){
        app.hillforts.deleteImage(hillfort, 0)
        view?.finish()
    }
    fun doSelectImage() {
        view?.let {
            showImagePicker(view!!, IMAGE_REQUEST)
        }
    }

    fun doSetLocation() {
        if (hillfort.zoom != 0f) {
            location.lat = hillfort.lat
            location.lng = hillfort.lng
            location.zoom = hillfort.zoom
        }
        view?.startActivityForResult(view?.intentFor<MapView>()?.putExtra("location", location), LOCATION_REQUEST)
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when(requestCode) {
            IMAGE_REQUEST -> {
                hillfort.images.add(data.getData().toString())
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    val location = data.extras?.getParcelable<Location>("location")!!
                    hillfort.lat = location.lat
                    hillfort.lng = location.lng
                    hillfort.zoom = location.zoom
                    locationUpdate(hillfort.lat, hillfort.lng)
                }
            }
        }
    }
}