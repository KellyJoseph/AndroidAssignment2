package org.wit.hillforts.views.LoginFB


import org.wit.hillforts.views.BasePresenter
import org.wit.hillforts.views.BaseView
import org.wit.hillforts.views.VIEW

class LoginPresenterFB(view: BaseView) : BasePresenter(view) {

    fun doLogin(email: String, password: String) {
        view?.navigateTo(VIEW.HILLFORTSLIST)
    }

    fun doSignUp(email: String, password: String) {
        view?.navigateTo(VIEW.HILLFORTSLIST)
    }
}