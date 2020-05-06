package com.chixel.chixelapp.database

import  android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

private const val dbName = "ImageData-database"
@Database(entities = [ImageData::class], version = 2)
@TypeConverters(ImageDataConverter::class)
abstract class ImageDataDatabase : RoomDatabase(){
    abstract  fun imageDataDao() : ImageDataDao
    companion object{
        private var instance : ImageDataDatabase ?= null
//        private val migration_1_2 = object : Migration(1,2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE ImageData ADD COLUMN colorOne TEXT DEFAULT NULL")
//                database.execSQL("ALTER TABLE crime ADD COLUMN suspectNumber TEXT DEFAULT NULL")
//            }
//        }
        fun getInstance(context: Context): ImageDataDatabase{
            return instance?:let {
                instance?:Room.databaseBuilder(context, ImageDataDatabase::class.java, dbName).build()
            }
        }
    }
}