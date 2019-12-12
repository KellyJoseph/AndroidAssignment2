package org.wit.hillforts.views.HillfortMaps


import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.views.BasePresenter
import org.wit.hillforts.views.BaseView

class HillfortsMapPresenter(view: BaseView): BasePresenter(view) {

    //var app: MainApp

    //init {
    //    app = view.application as MainApp
   // }

    fun doPopulateMap(map: GoogleMap, hillforts: List<HillfortModel>) {
        map.uiSettings.setZoomControlsEnabled(true)
        //map.setOnMarkerClickListener(view)
        //app.hillforts.findAll().forEach {
        hillforts.forEach() {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.name).position(loc)
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
    }

    fun doMarkerSelected(marker: Marker) {
        val tag = marker.tag as Long
        val hillfort = app.hillforts.findById(tag)
        if (hillfort != null) view?.showHillfort(hillfort)
    }
    fun loadHillforts() {
        view?.showHillforts(app.hillforts.findAll())
    }
}