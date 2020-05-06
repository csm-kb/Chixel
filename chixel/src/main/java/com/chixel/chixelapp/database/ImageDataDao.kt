package com.chixel.chixelapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface ImageDataDao {
    @Query("SELECT * FROM ImageData")
    fun getAllImages(): LiveData<List<ImageData>>
    @Query("SELECT * FROM ImageData where id=(:id)")
    fun getSingleImageData(id: UUID): LiveData<ImageData?>
    @Update
    fun updateSingleImageData(imageData: ImageData)
    @Insert
    fun addSingleImageData(imageData: ImageData)
    @Query("DELETE FROM ImageData")
    fun deleteImageData()
    @Query("SELECT * FROM ImageData")
    fun getColors(): LiveData<List<ImageData>>
    //@Query("SELECT colorOne FROM ImageData")
    //fun getColorOne(): LiveData<List<ImageData>>

}