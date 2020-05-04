package com.chixel.chixelapp.database;

import androidx.annotation.ColorInt
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.*;

@Entity
data class ImageData(@PrimaryKey val id :UUID = UUID.randomUUID(),
                     var pictureName: String = "",
                     @ColorInt var colorOne: Int = 0,
                     @ColorInt var colorTwo: Int = 0,
                     var date: Date = Date())
