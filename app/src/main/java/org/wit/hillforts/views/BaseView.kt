package org.wit.hillforts.views

import android.content.Intent

import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.models.Location
import org.wit.hillforts.views.EditLocation.EditLocationView
import org.wit.hillforts.views.Favorites.FavoritesView
import org.wit.hillforts.views.Hillfort.HillfortView
import org.wit.hillforts.views.HillfortList.HillfortListView
import org.wit.hillforts.views.HillfortsMap.HillfortsMapView
import org.wit.hillforts.views.LoginFB.LoginViewFB
import org.wit.hillforts.views.Settings.SettingsView

val IMAGE_REQUEST = 1
val LOCATION_REQUEST = 2

enum class VIEW {
    HILLFORT, HILLFORTSLIST, EDITLOCATION, LOGIN, MAP, SETTINGS, FAVORITES
}

open abstract class BaseView() : AppCompatActivity(), AnkoLogger {

    var basePresenter: BasePresenter? = null

    fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
        var intent = Intent(this, HillfortListView::class.java)
        when (view) {
            VIEW.HILLFORT -> intent = Intent(this, HillfortView::class.java)
            VIEW.EDITLOCATION -> intent = Intent(this, EditLocationView::class.java)
            VIEW.HILLFORTSLIST -> intent = Intent(this, HillfortListView::class.java)
            VIEW.MAP -> intent = Intent(this, HillfortsMapView::class.java)
            VIEW.SETTINGS -> intent = Intent(this, SettingsView::class.java)
            VIEW.FAVORITES -> intent = Intent(this, FavoritesView::class.java)
            VIEW.LOGIN -> intent = Intent(this, LoginViewFB::class.java)
        }
        if (key != "") {
            intent.putExtra(key, value)
        }
        startActivityForResult(intent, code)
    }

    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
    }

    open fun showLocation(latitude : Double, longitude : Double) {}

    fun init(toolbar: Toolbar, upEnabled: Boolean) {
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(upEnabled)
        val user = FirebaseAuth.getInstance().currentUser
        //if (user != null) {
        //    //toolbar.title = "${title}: ${user.email}"
       // }
    }

    override fun onDestroy() {
        basePresenter?.onDestroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            basePresenter?.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    open fun showHillfort(hillfort: HillfortModel) {}
    open fun showHillforts(hillforts: List<HillfortModel>) {}
    //open fun showLocation(latitude : Double, longitude : Double) {}
    open fun showLocation(location: Location) {}
    open fun showTotal(number: Int) {}
    open fun showVisited(number: Int) {}
    open fun showUserHillforts(number: Int) {}
    open fun showProgress() {}
    open fun hideProgress() {}
    }
