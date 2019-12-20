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

    fun loadHillforts() {
        //view?.showHillforts(app.hillforts.findAll())
        doAsync {
            val hillforts = app.hillforts.findAll()
            uiThread {
                view?.showHillforts(hillforts)
            }
        }
    }
}