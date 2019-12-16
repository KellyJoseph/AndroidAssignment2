package org.wit.hillforts.views.HillfortsMap

import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

import kotlinx.android.synthetic.main.activity_hillfort_maps.*
import kotlinx.android.synthetic.main.content_hillfort_maps.*
import org.wit.hillforts.R
import org.wit.hillforts.helpers.readImageFromPath
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.views.BaseView

class HillfortsMapView: BaseView(), GoogleMap.OnMarkerClickListener {

    lateinit var map: GoogleMap
    lateinit var app: MainApp
    lateinit var presenter: HillfortsMapPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_maps)
        //setSupportActionBar(toolbar)
        super.init(toolbar, true)

        //presenter = HillfortsMapPresenter(this)
        presenter = initPresenter(HillfortsMapPresenter(this)) as HillfortsMapPresenter

        //app = application as MainApp
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync {
            map = it
            map.setOnMarkerClickListener(this)
            presenter.loadHillforts()
        }
    }

    fun configureMap() {
        map.uiSettings.setZoomControlsEnabled(true)
        app.hillforts.findAll().forEach() {
            var loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.name).position(loc)
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
            map.setOnMarkerClickListener(this)
        }
    }

    override fun showHillfort(hillfort: HillfortModel) {
        currentName.text = hillfort.name
        currentDescription.text = hillfort.description
        currentImage.setImageBitmap(readImageFromPath(this, hillfort.image))
    }

    override fun showHillforts(hillforts: List<HillfortModel>) {
        presenter.doPopulateMap(map, hillforts)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        //currentName.text = marker.title
        //val tag = marker.tag as Long
        //val placemark = app.hillforts.findById(tag)
        //currentDescription.text = placemark!!.description
        //currentImage.setImageBitmap(readImageFromPath(this, placemark.images.last()))
        presenter.doMarkerSelected(marker)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
