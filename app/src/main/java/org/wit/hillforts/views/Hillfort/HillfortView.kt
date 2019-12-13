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
import kotlinx.android.synthetic.main.activity_hillfort.*
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
    var hillfort = HillfortModel()
    var dateVisited = String()
    lateinit var app: MainApp
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        init(toolbarAdd)
        toolbarAdd.title = title

        presenter = initPresenter (HillfortPresenter(this)) as HillfortPresenter
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
            hillfortName.setText(hillfort.name)
            description.setText(hillfort.description)
            notes.setText(hillfort.notes)
            var date = hillfort.visitedDate
            var dateText = "$date last visited"
            visitedDateDisplay.setText(dateText)
            checkbox.setChecked(hillfort.visited)
            if (hillfort.images.size > 0) {
                hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.images.last()))
            }
            btnAdd.setText("Save")
            deleteImage.setOnClickListener() { presenter.doDeleteImage()}
        }
        btnAdd.setOnClickListener() {presenter.doAddOrSave(hillfortName.text.toString(),
            description.text.toString(), notes.text.toString(), presenter.app.loggedInUser.id,
            checkbox.isChecked, dateVisited)}
        hillfortLocation.setOnClickListener(){ presenter.doSetLocation()}
        chooseImage.setOnClickListener(){ presenter.doSelectImage()}
    }

    override fun showHillfort(hillfort: HillfortModel) {
        hillfortName.setText(hillfort.name)
        description.setText(hillfort.description)
        if (hillfort.images.size > 0) {
            hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.images.last()))
        }
        if (!hillfort.images.isEmpty()) {
            chooseImage.setText(R.string.change_hillfort_image)
        }
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
                    if (hillfort.images.size > 4) {
                        toast("You cannot upload more than 4 images")
                    }
                    else {
                        hillfort.images.add(data.getData().toString())
                    }
                    chooseImage.setText(R.string.change_hillfort_image)
                }
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
}
