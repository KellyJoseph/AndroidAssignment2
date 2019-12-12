package org.wit.hillforts.views.HillfortList

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_hillforts_list.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.wit.hillforts.R
import org.wit.hillforts.activities.*
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.models.UserModel
import org.wit.hillforts.views.BaseView
import org.wit.hillforts.views.HillfortMaps.HillfortsMapView
import org.wit.hillforts.views.Hillfort.HillfortView

class HillfortListView: BaseView(), HillfortListener {

    lateinit var app: MainApp
    lateinit var presenter: HillfortListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillforts_list)
        setSupportActionBar(toolbar)
        toolbar.title = title

        val data = intent.extras
        val user = data!!.getParcelable<UserModel>("user")
        toast("welcome ${user?.firstName}")
        app = application as MainApp
        var visitedHillforts = app.hillforts.findVisitedHillfortsByUser(app.loggedInUser)
        app.visitedHillforts = visitedHillforts

        presenter = initPresenter(HillfortListPresenter(this)) as HillfortListPresenter

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = HillfortsAdapter(
            app.hillforts.findAllByUser(app.loggedInUser),
            this
        )
    }

    //just add the + button
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //handler for the add button
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> startActivityForResult<HillfortView>(0)
            R.id.item_map -> startActivity<HillfortsMapView>()
            R.id.settings -> startActivityForResult<UserActivity>(0)
            R.id.logout -> startActivity(Intent(this, LoginActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    //handler for select activity edit
    override fun onHillfortClick(hillfort: HillfortModel) {
        startActivityForResult(intentFor<HillfortView>().putExtra("hillfort_edit", hillfort), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //loadHillfortsByUser()
        //recyclerView.adapter?.notifyDataSetChanged()
        presenter.losdHillforts()
        super.onActivityResult(requestCode, resultCode, data)
    }
}