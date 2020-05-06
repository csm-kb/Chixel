package com.chixel.chixelapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chixel.chixelapp.database.ImageDataRepository

class ColorPickerPopupModelFactory(private val content: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ImageDataRepository::class.java)
            .newInstance(ImageDataRepository.getInstance(content))
    }

}