package org.wit.hillforts.views.Settings

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_user.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.hillforts.R
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.models.UserModel
import org.wit.hillforts.views.BaseView


class SettingsView: BaseView(), AnkoLogger {

    lateinit var presenter: SettingsPresenter
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        settingsMenu.title = "Settings"
        setSupportActionBar(settingsMenu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        info("User Activity started..")

        presenter = initPresenter(SettingsPresenter(this)) as SettingsPresenter

        //var user = presenter.app.loggedInUser
        editFirstName.setText(user.firstName)
        editLastName.setText(user.lastName)
        editPassword.setText(user.password)
        editEmail.setText(user.email)
        presenter.doGetAllHillforts()
        presenter.doGetAllHillfortsByUser()
        presenter.doGetAllVisitedHillfortsByUser()


        editUserSubmit.setOnClickListener() {
            user.firstName= editFirstName.text.toString()
            user.lastName = editLastName.text.toString()
            user.password = editPassword.text.toString()
            //user.id = user.id
            user.id = 0
            user.email = user.email
            if (user.firstName.isEmpty() || user.lastName.isEmpty() || user.password.isEmpty())  {
                toast(R.string.enter_hillfort_name)
            } else {
                presenter.app.users.update(user.copy())
            }
            info("user info updated")
            setResult(AppCompatActivity.RESULT_OK)
            finish()
        }
    }
    override fun showTotal(number: Int) {
        totalNumber.text = "Total hillforts: $number"
    }

    override fun showUserHillforts(number: Int) {
        userRecorded.text = "User recorded: $number"
    }

    override fun showVisited(number: Int) {
        userVisited.text = "Hillforts visited: $number"
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}