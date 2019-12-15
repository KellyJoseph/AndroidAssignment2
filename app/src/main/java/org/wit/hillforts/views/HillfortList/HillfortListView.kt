package org.wit.hillforts.views.HillfortList

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_hillforts_list.*
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.wit.hillforts.R
import org.wit.hillforts.activities.*
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.models.UserModel
import org.wit.hillforts.views.BaseView
import org.wit.hillforts.views.Settings.SettingsView

class HillfortListView: BaseView(), HillfortListener {

    //lateinit var app: MainApp
    lateinit var presenter: HillfortListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillforts_list)
        setSupportActionBar(toolbar)
        toolbar.title = title

        val data = intent.extras
        val user = data!!.getParcelable<UserModel>("user")
        toast("welcome ${user!!.firstName}")
        //app = application as MainApp
        //var visitedHillforts = app.hillforts.findVisitedHillfortsByUser(app.loggedInUser)
        //app.visitedHillforts = visitedHillforts

        presenter = initPresenter(HillfortListPresenter(this)) as HillfortListPresenter
        presenter.app.loggedInUser = user

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        presenter.loadHillforts()
    }

    override fun showHillforts(hillforts: List<HillfortModel>) {
        recyclerView.adapter = HillfortsAdapter(hillforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    //just add the + button
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //handler for the add button
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> presenter.doAddHillfort()
            R.id.item_map -> presenter.doShowHillfortsMap()
            R.id.settings -> startActivityForResult<SettingsView>(0)
            R.id.logout -> startActivity(Intent(this, LoginActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    //handler for select activity edit
    override fun onHillfortClick(hillfort: HillfortModel) {
        presenter.doEditHillfort(hillfort)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //loadHillfortsByUser()
        //recyclerView.adapter?.notifyDataSetChanged()
        presenter.loadHillforts()
        super.onActivityResult(requestCode, resultCode, data)
    }
}