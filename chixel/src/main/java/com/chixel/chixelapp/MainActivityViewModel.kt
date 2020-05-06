package com.chixel.chixelapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.chixel.chixelapp.database.ImageData
import com.chixel.chixelapp.database.ImageDataRepository
import java.util.*

class MainActivityViewModel(private val imageDataRepository: ImageDataRepository): ViewModel() {
    val allColorLiveData = imageDataRepository.getColors()
    //val allColorOneData = imageDataRepository.getColorOne()
    private val colorIdLiveData = MutableLiveData<UUID>()
    var singleColorLiveData : LiveData<ImageData?> =
        Transformations.switchMap(colorIdLiveData) { imageId ->
            imageDataRepository.getSingleColor(imageId)
        }

    fun loadColor(id: UUID) {
        colorIdLiveData.value = id
    }

    fun addSingleImageData(imageData: ImageData) {
        imageDataRepository.addSingleImageData(imageData)
    }

}