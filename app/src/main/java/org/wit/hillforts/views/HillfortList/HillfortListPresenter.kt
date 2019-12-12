package org.wit.hillforts.views.HillfortList

import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel

import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.wit.hillforts.views.HillfortMaps.HillfortsMapView
import org.wit.hillforts.views.Hillfort.HillfortView


class HillfortListPresenter(val view: HillfortListActivity) {

    var app: MainApp

    init {
        app = view.application as MainApp
    }

    fun getPlacemarks() = app.hillforts.findAll()

    fun doAddPlacemark() {
        view.startActivityForResult<HillfortView>(0)
    }

    fun doEditPlacemark(placemark: HillfortModel) {
        view.startActivityForResult(view.intentFor<HillfortView>().putExtra("placemark_edit", placemark), 0)
    }

    fun doShowPlacemarksMap() {
        view.startActivity<HillfortsMapView>()
    }
}