package org.wit.hillforts.views.LoginFB

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login_fb.*
import org.jetbrains.anko.toast
import org.wit.hillforts.R
import org.wit.hillforts.views.BaseView

class LoginViewFB : BaseView() {

    lateinit var presenter: LoginPresenterFB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_fb)
        init(toolbar, false)

        presenter = initPresenter(LoginPresenterFB(this)) as LoginPresenterFB

        signUp.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            if (email == "" || password == "") {
                toast("Please provide email + password")
            }
            else {
                presenter.doSignUp(email,password)
            }
        }

        logIn.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            if (email == "" || password == "") {
                toast("Please provide email + password")
            }
            else {
                presenter.doLogin(email,password)
            }
        }
    }
}