package org.wit.hillforts.views.Favorites

import org.wit.hillforts.views.Favorites.FavoritesPresenter

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
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

class FavoritesView: BaseView(), HillfortListener, NavigationView.OnNavigationItemSelectedListener {

    lateinit var presenter: FavoritesPresenter
    lateinit var loader : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        setSupportActionBar(toolbar)

        val bottomNavigationView = findViewById(R.id.favorites_bottom_nav) as BottomNavigationView
        bottomNavigationView?.setOnNavigationItemSelectedListener(
            object : BottomNavigationView.OnNavigationItemSelectedListener {
                override fun onNavigationItemSelected(item: MenuItem): Boolean {
                    when (item?.itemId) {
                        R.id.bottom_nav_home -> finish()
                        R.id.bottom_nav_map -> presenter.doGoToMap()
                        R.id.bottom_nav_favorites -> presenter.doGoToFavorites()
                    }
                    return false
                }
            }
        )

        navViewHillfortList.setNavigationItemSelectedListener(this)
        navViewHillfortList.getHeaderView(0).textView1.text = FirebaseAuth.getInstance().currentUser!!.email

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        presenter = initPresenter(FavoritesPresenter(this)) as FavoritesPresenter
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        presenter.loadHillforts()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_add -> presenter.doAddHillfort()
            R.id.nav_map -> presenter.doShowHillfortsMap()
            R.id.nav_home -> navigateTo(VIEW.HILLFORTSLIST)
            R.id.nav_settings -> navigateTo(VIEW.SETTINGS)
            R.id.nav_favorites -> toast("you are already on favorites")

            else -> toast("You Selected Something Else")
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun showHillforts(hillforts: List<HillfortModel>) {
        recyclerView.adapter = HillfortsAdapter(hillforts, this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNullOrEmpty()) {
                    presenter.doFilterFavorites()
                }
                else
                    toast("text has changed $newText")
                presenter.doFilterHillforts(newText)

                return false
            }
            override fun onQueryTextSubmit(newText: String): Boolean {

                if (newText.isNullOrEmpty()) {
                    presenter.loadHillforts()
                }
                else
                    toast("text has changed $newText")
                presenter.doFilterHillforts(newText)
                return false
            }
        }
        )
        return true
    }

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