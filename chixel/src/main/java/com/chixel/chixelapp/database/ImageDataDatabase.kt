package com.chixel.chixelapp.database

import  android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

private const val dbName = "ImageData-database"
@Database(entities = [ImageData::class], version = 1)
@TypeConverters(ImageDataConverter::class)
abstract class ImageDataDatabase : RoomDatabase(){
    abstract  fun imageDataDao() : ImageDataDao
    companion object{
        private var instance : ImageDataDatabase ?= null
        fun getInstance(context: Context): ImageDataDatabase{
            return instance?:let {
                instance?:Room.databaseBuilder(context, ImageDataDatabase::class.java, dbName).build()
            }
        }
    }
}