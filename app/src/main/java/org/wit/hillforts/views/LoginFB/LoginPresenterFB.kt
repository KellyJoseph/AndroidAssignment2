package org.wit.hillforts.views.LoginFB


import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.toast
import org.wit.hillforts.views.BasePresenter
import org.wit.hillforts.views.BaseView
import org.wit.hillforts.views.VIEW

class LoginPresenterFB(view: BaseView) : BasePresenter(view) {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun doLogin(email: String, password: String) {
        //view?.navigateTo(VIEW.HILLFORTSLIST)
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                view?.navigateTo(VIEW.HILLFORTSLIST)
            } else {
                view?.toast("Sign Up Failed: ${task.exception?.message}")
            }
            view?.hideProgress()
        }
    }

    fun doSignUp(email: String, password: String) {
        //view?.navigateTo(VIEW.HILLFORTSLIST)
        view?.showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                view?.navigateTo(VIEW.HILLFORTSLIST)
            } else {
                view?.toast("Sign Up Failed: ${task.exception?.message}")
            }
            view?.hideProgress()
        }
    }
}