package org.wit.hillforts.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.toolbarAdd
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.toast
import org.wit.hillforts.R
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.UserModel

class RegisterActivity: AppCompatActivity() {

    var user = UserModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)
        app = application as MainApp

        registerSubmit.setOnClickListener() {
            user.firstName = registerFirstName.text.toString()
            user.lastName = registerLastName.text.toString()
            user.email = registerEmail.text.toString()
            user.password = registerPassword.text.toString()
            if (user.firstName.isNotEmpty() && user.lastName.isNotEmpty() && user.email.isNotEmpty() &&
                user.password.isNotEmpty()) {
                app.users.register(user)
                toast("Log in to proceed")
                finish()
                //startActivity(Intent(this, HillfortListView::class.java))

            } else {
                toast("An email address and password are required")
            }
        }
    }
}