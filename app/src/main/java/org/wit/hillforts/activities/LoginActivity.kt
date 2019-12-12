package org.wit.hillforts.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.toolbarAdd
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.wit.hillforts.R
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.UserModel
import org.wit.hillforts.views.HillfortList.HillfortListActivity

class LoginActivity : AppCompatActivity(), AnkoLogger {

    var user = UserModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)
        app = application as MainApp
        //set a toolbar to act as an action bar in this activity


        loginSubmit.setOnClickListener() {
            var email : String = loginEmail.text.toString()
            var password : String = loginPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                var res = app.users.login(email, password)
                if (res != null) {
                    app.loggedInUser = res
                    toast("logged in")
                   // val intent = Intent(this)
                    startActivity(Intent(this, HillfortListActivity::class.java).
                        putExtra("user", res))
                }
                else if (res == null) {
                    toast("incorrect email and/or password")
                }
            } else {
                toast("An email address and password are required")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.login_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.register -> startActivityForResult<RegisterActivity>(0)
        }
        return super.onOptionsItemSelected(item)
    }

}
