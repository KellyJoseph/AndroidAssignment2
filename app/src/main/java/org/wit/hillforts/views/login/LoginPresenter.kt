package org.wit.hillforts.views.login

import org.wit.hillforts.views.BasePresenter
import org.wit.hillforts.views.BaseView
import org.wit.hillforts.views.VIEW

class LoginPresenter(view: BaseView) : BasePresenter(view) {

    fun doLogin(email: String, password: String) {
        view?.navigateTo(VIEW.HILLFORTSLIST)
    }

    fun doSignUp(email: String, password: String) {
        view?.navigateTo(VIEW.HILLFORTSLIST)
    }
}