package org.wit.hillforts.views.HillfortList

import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel

import org.jetbrains.anko.startActivityForResult
import org.wit.hillforts.views.BasePresenter
import org.wit.hillforts.views.BaseView
import org.wit.hillforts.views.Hillfort.HillfortView
import org.wit.hillforts.views.VIEW

class HillfortListPresenter(view: BaseView): BasePresenter(view) {

    fun losdHillforts() = app.hillforts.findAll()

    fun doAddHillfort() {
        view?.navigateTo(VIEW.HILLFORT)
    }

    fun doEditHillfort(hillfort: HillfortModel) {
        view?.navigateTo(VIEW.HILLFORTSLIST, 0, "hillfort_edit", hillfort)
    }

    fun doShowHillfortsMap() {
        view?.navigateTo(VIEW.MAPS)
    }

    fun loadPlacemarks() {
        view?.showPlacemarks(app.placemarks.findAll())
    }
}