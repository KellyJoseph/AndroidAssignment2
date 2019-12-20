package org.wit.hillforts.views.HillfortList

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_hillforts_list.*
import kotlinx.android.synthetic.main.activity_hillforts_list.drawerLayout
import kotlinx.android.synthetic.main.nav_header.view.*
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.wit.hillforts.R
import org.wit.hillforts.activities.*
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.views.BaseView
import org.wit.hillforts.views.Settings.SettingsView
import org.wit.hillforts.views.VIEW

class HillfortListView: BaseView(), HillfortListener, NavigationView.OnNavigationItemSelectedListener {

    lateinit var presenter: HillfortListPresenter
    lateinit var loader : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillforts_list)
        setSupportActionBar(toolbar)


        navViewHillfortList.setNavigationItemSelectedListener(this)
        navViewHillfortList.getHeaderView(0).textView1.text = FirebaseAuth.getInstance().currentUser!!.email

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        presenter = initPresenter(HillfortListPresenter(this)) as HillfortListPresenter
        //presenter.app.loggedInUser = user

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        presenter.loadHillforts()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_add -> toast("you are already on the home page")
            R.id.nav_map -> presenter.doShowHillfortsMap()
            R.id.nav_home -> navigateTo(VIEW.HILLFORTSLIST)
            R.id.nav_settings -> navigateTo(VIEW.SETTINGS)

            else -> toast("You Selected Something Else")
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun showHillforts(hillforts: List<HillfortModel>) {
        recyclerView.adapter = HillfortsAdapter(hillforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }



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
            R.id.item_logout ->presenter.doLogout()
        }
        return super.onOptionsItemSelected(item)
    }

    //handler for select activity edit
    override fun onHillfortClick(hillfort: HillfortModel) {
        presenter.doEditHillfort(hillfort)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.loadHillforts()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

}