package org.wit.hillforts.views.HillfortList

import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import org.wit.hillforts.models.HillfortModel
import org.jetbrains.anko.uiThread
import org.wit.hillforts.views.BasePresenter
import org.wit.hillforts.views.BaseView
import org.wit.hillforts.views.VIEW

class HillfortListPresenter(view: BaseView): BasePresenter(view) {

    fun doAddHillfort() {
        view?.navigateTo(VIEW.HILLFORT)
    }

    fun doEditHillfort(hillfort: HillfortModel) {
        view?.navigateTo(VIEW.HILLFORT, 0, "hillfort_edit", hillfort)
    }

    fun doShowHillfortsMap() {
        view?.navigateTo(VIEW.MAP)
    }

    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        view?.navigateTo(VIEW.LOGIN)
    }

    fun doFilterHillforts(newText: String) {
        doAsync {
            val hillforts = app.hillforts.findAll().filter { it.name.toLowerCase().contains(newText.toLowerCase()) }
            //val filteredHillforts = hillforts.toMutableList()
            //filteredHillforts.filter { it.name.startsWith(newText) }
            uiThread {
                view?.showHillforts(hillforts)
            }
        }
    }

    fun loadHillforts() {
        doAsync {
            val hillforts = app.hillforts.findAll()
            uiThread {
                view?.showHillforts(hillforts)
            }
        }
    }
}