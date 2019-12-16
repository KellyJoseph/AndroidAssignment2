package org.wit.hillforts.views.Hillfort

import android.annotation.SuppressLint
import android.content.Intent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.uiThread
import org.wit.hillforts.helpers.checkLocationPermissions
import org.wit.hillforts.helpers.createDefaultLocationRequest
import org.wit.hillforts.helpers.isPermissionGranted
import org.wit.hillforts.helpers.showImagePicker
import org.wit.hillforts.models.Location
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.views.BasePresenter
import org.wit.hillforts.views.BaseView
import org.wit.hillforts.views.EditLocation.EditLocationView

class HillfortPresenter(view: BaseView): BasePresenter(view) {

    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
    val locationRequest = createDefaultLocationRequest()
    var map: GoogleMap? = null
    var hillfort = HillfortModel()
    var location = Location(52.245696, -7.139102, 15f)
    var edit = false
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)


    init {
        //app = view.application as MainApp
        if(view.intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = view.intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
            view.showHillfort(hillfort)
        } else {
            //hillfort.lat = location.lat
            //hillfort.lng = location.lng
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
        }
    }
    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            locationUpdate(location.lat, location.lng)
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
        doAsync {
            if (edit) {
                app.hillforts.update(hillfort)
            } else {
                app.hillforts.create(hillfort)
            }
            uiThread {
                view?.finish()
            }
        }
    }

    fun doCancel() {
        view?.finish()
    }
    fun doDelete() {
        app.hillforts.delete(hillfort)
        view?.finish()
    }
    fun doDeleteImage(){
        app.hillforts.deleteImage(hillfort)
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
        view?.startActivityForResult(view?.intentFor<EditLocationView>()?.putExtra("location", location), LOCATION_REQUEST)
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(l.latitude, l.longitude)
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when(requestCode) {
            IMAGE_REQUEST -> {
                hillfort.image = data.getData().toString()
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