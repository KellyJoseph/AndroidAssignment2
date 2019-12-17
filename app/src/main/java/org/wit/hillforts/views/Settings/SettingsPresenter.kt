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


    fun doGetAllHillfortsByUser(userId: String) {
        doAsync {
            val hillforts = app.hillforts.findAllByUser(userId).size
            uiThread {
                view?.showUserHillforts(hillforts)
            }
        }
    }

    fun doGetAllVisitedHillfortsByUser(userId: String) {
        doAsync {
            val hillforts = app.hillforts.findAllByUser(userId).size
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