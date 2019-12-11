package org.wit.hillforts.activities

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_hillforts_list.*
import kotlinx.android.synthetic.main.hillfort_card.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.wit.hillforts.R
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.models.UserModel

class HillfortListActivity: AppCompatActivity(), HillfortListener {

    lateinit var app: MainApp
    lateinit var presenter: HillfortListPresenter

    //create the activity. bundle is what activity data is saved in if closed, used to re-create a
    //closed activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillforts_list)
        toolbar.title = title
        setSupportActionBar(toolbar)

        val data = intent.extras
        val user = data!!.getParcelable<UserModel>("user")
        toast("welcome ${user?.firstName}")
        app = application as MainApp
        var visitedHillforts = app.hillforts.findVisitedHillfortsByUser(app.loggedInUser)
        app.visitedHillforts = visitedHillforts

        presenter = HillfortListPresenter(this)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        //loadHillforts()
        recyclerView.adapter = HillfortsAdapter(app.hillforts.findAllByUser(app.loggedInUser), this)
    }

    //just add the + button
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }


    //handler for the add button
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> startActivityForResult<HillfortActivity>(0)
            R.id.item_map -> startActivity<HillfortMapsActivity>()
            R.id.settings -> startActivityForResult<UserActivity>(0)
            R.id.logout -> startActivity(Intent(this, LoginActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }



    //handler for select activity edit
    override fun onHillfortClick(hillfort: HillfortModel) {
        startActivityForResult(intentFor<HillfortActivity>().putExtra("hillfort_edit", hillfort), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //loadHillfortsByUser()
        recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }
    /*
    private fun loadHillforts() {
        showHillforts(app.hillforts.findAll())
    }
    private fun loadHillfortsByUser() {
        showHillforts(app.hillforts.findAllByUser(app.loggedInUser))
    }
    fun showHillforts (hillforts: List<HillfortModel>) {
        recyclerView.adapter = HillfortsAdapter(hillforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }

     */
}