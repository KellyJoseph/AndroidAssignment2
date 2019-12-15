package org.wit.hillforts.views.Settings

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.hillforts.views.BasePresenter
import org.wit.hillforts.views.BaseView

class SettingsPresenter (view: BaseView): BasePresenter(view) {

    fun doGetAllHillforts() {
        doAsync {
            val hillforts = app.hillforts.findAll().size
            uiThread {
                view?.showTotal(hillforts)
            }
        }
    }


    fun doGetAllHillfortsByUser() {
        doAsync {
            val hillforts = app.hillforts.findAllByUser(app.loggedInUser).size
            uiThread {
                view?.showUserHillforts(hillforts)
            }
        }
    }

    fun doGetAllVisitedHillfortsByUser() {
        doAsync {
            val hillforts = app.hillforts.findAllByUser(app.loggedInUser).size
            uiThread {
                view?.showVisited(hillforts)
            }
        }
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