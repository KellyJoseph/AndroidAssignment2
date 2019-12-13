package org.wit.hillforts.views.Hillfort

import android.content.Intent
import org.jetbrains.anko.intentFor
import org.wit.hillforts.helpers.showImagePicker
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.Location
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.views.BasePresenter
import org.wit.hillforts.views.BaseView
import org.wit.hillforts.views.Map.MapView

class HillfortPresenter(view: BaseView): BasePresenter(view) {

    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2

    var hillfort = HillfortModel()
    var location = Location(52.245696, -7.139102, 15f)

    var edit = false

    init {
        app = view.application as MainApp
        if(view.intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = view.intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
            view.showHillfort(hillfort)
        }
    }

    fun doAddOrSave(name: String, description: String, notes: String, authorId: Long, visited: Boolean, visitedDate: String) {
        hillfort.name = name
        hillfort.description = description
        hillfort.notes = notes
        hillfort.authorId = authorId
        hillfort.visited = visited
        hillfort.visitedDate = visitedDate
        if(edit) {
            app.hillforts.update(hillfort)
        } else {
            app.hillforts.create(hillfort)
        }
        view?.finish()
    }

    fun doCancel() {
        view?.finish()
    }
    fun doDelete() {
        app.hillforts.delete(hillfort)
        view?.finish()
    }
    fun doDeleteImage(){
        app.hillforts.deleteImage(hillfort, 0)
        view?.finish()
    }
    fun doSelectImage() {
        view?.let {
            showImagePicker(view!!, IMAGE_REQUEST)
        }
    }

    fun doSetLocation() {
        if (hillfort.zoom != 0f) {
            location.lat = hillfort.lat
            location.lng = hillfort.lng
            location.zoom = hillfort.zoom
        }
        view?.startActivityForResult(view?.intentFor<MapView>()?.putExtra("location", location), LOCATION_REQUEST)
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when(requestCode) {
            IMAGE_REQUEST -> {
                hillfort.images.add(data.getData().toString())
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