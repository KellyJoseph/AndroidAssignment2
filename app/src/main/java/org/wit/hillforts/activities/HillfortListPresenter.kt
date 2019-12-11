package org.wit.hillforts.activities

import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel

import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult


class HillfortListPresenter(val view: HillfortListActivity) {

    var app: MainApp

    init {
        app = view.application as MainApp
    }

    fun getPlacemarks() = app.hillforts.findAll()

    fun doAddPlacemark() {
        view.startActivityForResult<HillfortActivity>(0)
    }

    fun doEditPlacemark(placemark: HillfortModel) {
        view.startActivityForResult(view.intentFor<HillfortActivity>().putExtra("placemark_edit", placemark), 0)
    }

    fun doShowPlacemarksMap() {
        view.startActivity<HillfortMapsActivity>()
    }
}