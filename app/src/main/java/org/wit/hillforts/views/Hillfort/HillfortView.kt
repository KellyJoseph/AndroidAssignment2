package org.wit.hillforts.views.Hillfort

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.content_hillfort_maps.*
import org.wit.hillforts.R
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import org.wit.hillforts.helpers.readImageFromPath
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.models.Location
import org.wit.hillforts.views.BaseView
import java.util.*


class HillfortView : BaseView(), AnkoLogger {
    lateinit var presenter: HillfortPresenter
    lateinit var map: GoogleMap
    var hillfort = HillfortModel()
    var dateVisited = String()
    lateinit var app: MainApp
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
    var edit = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        super.init(toolbarAdd)
        toolbarAdd.title = title

        presenter = initPresenter (HillfortPresenter(this)) as HillfortPresenter
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        hillfortMap.onCreate(savedInstanceState);
        hillfortMap.getMapAsync {
            presenter.doConfigureMap(it)
            it.setOnMapClickListener { presenter.doSetLocation() }
        }

        btnAdd.setOnClickListener() {presenter.doAddOrSave(hillfortName.text.toString(),
            description.text.toString(), notes.text.toString(), presenter.app.loggedInUser.id,
            checkbox.isChecked, dateVisited)}
        //hillfortLocation.setOnClickListener(){ presenter.doSetLocation()}
        chooseImage.setOnClickListener(){ presenter.doSelectImage()}
        deleteImage.setOnClickListener() { presenter.doDeleteImage()}
    }

    override fun showHillfort(hillfort: HillfortModel) {
        hillfortName.setText(hillfort.name)
        description.setText(hillfort.description)
        hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image))
        btnAdd.setText(R.string.save_hillfort)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.hillfort_menu, menu)
        if (edit && menu != null) menu.getItem(0).setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> {
                presenter.doCancel()
            }
            R.id.item_delete -> {
                toast ("Delete button was pressed ${hillfort.id}")
                presenter.doDelete()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked
            when (view.id) {
                R.id.checkbox -> {
                    if (checked) {
                        hillfort.visited = true
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun clickDataPicker(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            Toast.makeText(this, """$dayOfMonth - ${monthOfYear + 1} - $year""", Toast.LENGTH_LONG).show()
            dateVisited = "$dayOfMonth" + "/" + "$monthOfYear" + "/" + "$year"
        }, year, month, day)
        dpd.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                        hillfort.image = data.getData().toString()
                    }
                    chooseImage.setText(R.string.change_hillfort_image)
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    val location = data.extras?.getParcelable<Location>("location")!!
                    hillfort.lat = location.lat
                    hillfort.lng = location.lng
                    hillfort.zoom = location.zoom
                }
            }

        }
    }
    override fun onDestroy() {
        super.onDestroy()
        hillfortMap.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        hillfortMap.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        hillfortMap.onPause()
    }

    override fun onResume() {
        super.onResume()
        hillfortMap.onResume()
        presenter.doResartLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        hillfortMap.onSaveInstanceState(outState)
    }
}
