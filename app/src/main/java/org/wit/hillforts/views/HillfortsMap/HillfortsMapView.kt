package org.wit.hillforts.views.HillfortsMap

import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView

import kotlinx.android.synthetic.main.activity_hillfort_maps.*
import kotlinx.android.synthetic.main.content_hillfort_maps.*
import org.jetbrains.anko.toast
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
        super.init(toolbar, true)

        presenter = initPresenter(HillfortsMapPresenter(this)) as HillfortsMapPresenter

        //app = application as MainApp
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync {
            map = it
            map.setOnMarkerClickListener(this)
            presenter.loadHillforts()
        }
        val bottomNavigationView = findViewById(R.id.maps_bottom_nav) as BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(
            object : BottomNavigationView.OnNavigationItemSelectedListener {
                override fun onNavigationItemSelected(item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.bottom_nav_home -> presenter.doGoToHome()
                        R.id.bottom_nav_map -> presenter.doShowHillfortsMap()
                        R.id.bottom_nav_favorites -> presenter.doGoToFavorites()
                    }
                    return false
                }
            }
        )
    }

    override fun showHillfort(hillfort: HillfortModel) {
        currentName.text = hillfort.name
        currentDescription.text = hillfort.description
        //currentImage.setImageBitmap(readImageFromPath(this, hillfort.image))
        Glide.with(this).load(hillfort.image).into(currentImage);
    }

    override fun showHillforts(hillforts: List<HillfortModel>) {
        presenter.doPopulateMap(map, hillforts)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
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
