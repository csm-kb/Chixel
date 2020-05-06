package com.chixel.chixelapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors
//

class ImageDataRepository(private val imageDataDao: ImageDataDao) {

    fun getColors() : LiveData<List<ImageData>> = imageDataDao.getColors()
    //fun getColorOne() : LiveData<List<ImageData>> = imageDataDao.getColorOne()
    fun getSingleColor(id: UUID): LiveData<ImageData?> = imageDataDao.getSingleImageData(id)
    private val executor = Executors.newSingleThreadExecutor()

    fun addSingleImageData(imageData: ImageData) {
        executor.execute {
            imageDataDao.addSingleImageData(imageData)
        }
    }

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
        fun get(): ImageDataRepository {
            return instance ?:
            throw IllegalStateException("ImageDataRepository must be initialized")
        }
    }
}