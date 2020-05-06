package com.chixel.chixelapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import java.lang.IllegalStateException
import java.util.concurrent.Executors
//

class ImageDataRepository(private val imageDataDao: ImageDataDao) {
    private val executor = Executors.newSingleThreadExecutor()

    companion object{
        private var instance: ImageDataRepository? = null
        fun getInstance(context: Context): ImageDataRepository? {
            return instance ?: let{
                if(instance == null){
                    val databaseDB = ImageDataDatabase.getInstance(context)
                    instance = ImageDataRepository(databaseDB.imageDataDao())
                }
                return instance
            }
        }
    }
}