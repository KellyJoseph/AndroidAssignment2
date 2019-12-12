package org.wit.hillforts.views.Hillfort

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
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
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.hillforts.helpers.readImageFromPath
import org.wit.hillforts.helpers.showImagePicker
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.models.Location
import org.wit.hillforts.views.Map.MapView
import java.util.*


class HillfortView : AppCompatActivity(), AnkoLogger {
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
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        presenter = HillfortPresenter(this)
        edit = true
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        info("Hillfort Activity started..")
        var edit = false
        app = application as MainApp

        if (intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
            hillfortName.setText(hillfort.name)
            description.setText(hillfort.description)
            notes.setText(hillfort.notes)
            var date = hillfort.visitedDate
            var dateText = "$date last visited"
            visitedDateDisplay.setText(dateText)
            if (hillfort.visited == true) {
                checkbox.setChecked(true);
            }
            //hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image))
            //if (hillfort.image != null) {
            //    chooseImage.setText(R.string.change_hillfort_image)
            //}

            // I'm not proud of this, but it gets the job done for now...
            if (hillfort.images.size > 0) {
                hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.images.last()))
            }
            btnAdd.setText(R.string.save_hillfort)
        }

        hillfortLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            //startActivity (intentFor<MapView>().putExtra("location", location))
            if (hillfort.zoom != 0f) {
                location.lat =  hillfort.lat
                location.lng = hillfort.lng
                location.zoom = hillfort.zoom
            }
            startActivityForResult(intentFor<MapView>().putExtra("location", location),
                LOCATION_REQUEST)
        }
        deleteImage.setOnClickListener{
            app.hillforts.deleteImage(hillfort, 0)
            finish()
        }

        btnAdd.setOnClickListener() {
            var name= hillfortName.text.toString()
            var description = description.text.toString()
            var notes = notes.text.toString()
            var authorId = app.loggedInUser!!.id
            var visited = checkbox.isChecked
            var visitedDate = dateVisited
            if (hillfort.name.isEmpty()) {
                toast(R.string.enter_hillfort_name)
            } else {
                presenter.doAddOrSave(name, description, notes, authorId, visited, visitedDate)
            }
            info("add Button Pressed: $hillfortName")
            setResult(AppCompatActivity.RESULT_OK)
            finish()
        }


        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }
    }

    fun showHillfort(hillfort: HillfortModel) {
        hillfortName.setText(hillfort.name)
        description.setText(hillfort.description)
        hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.images.last()))
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
                finish()
            }
            R.id.item_delete -> {
                toast ("Delete button was pressed ${hillfort.id}")
                app.hillforts.delete(hillfort)
                finish()
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
                        //app.hillforts.update(hillfort.copy())
                    }
                }
            }
        }
    }
    // adapted from https://tutorial.eyehunts.com/android/android-date-picker-dialog-example-kotlin/
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
                        //app.hillforts.update(hillfort.copy())
                        //finish()
                        //hillfortImage1.setImageBitmap(readImage(this, resultCode, data))
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
