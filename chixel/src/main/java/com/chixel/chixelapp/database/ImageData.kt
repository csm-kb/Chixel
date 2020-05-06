package com.chixel.chixelapp.database;

import androidx.annotation.ColorInt
import androidx.room.Embedded
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation
import java.util.*;

@Entity
data class ImageData(@PrimaryKey val id :UUID = UUID.randomUUID(),
                     var pictureName: String? = "",
                     var colorOne: String? = "",
                     var colorTwo: String? = "",
                     var date: Date? = Date())

class ImageBitmapData{
    @Relation(parentColumn = "id", entityColumn = "pictureName")
                    var id: String? = ""}
