package com.chixel.chixelapp

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chixel.chixelapp.database.ImageData

class ImageHolder(val view: View): RecyclerView.ViewHolder(view) {
    private lateinit var imageData: ImageData
    val imageNameTextView : TextView = itemView.findViewById(R.id.picture_name)
    val imageDate : TextView = itemView.findViewById(R.id.picture_date)

    fun bind(imageData: ImageData, clickListener: (ImageData) -> Unit) {
        this.imageData = imageData
        itemView.setOnClickListener { clickListener(this.imageData) }
        imageNameTextView.text = this.imageData.pictureName
        imageDate.text = this.imageData.date.toString()
    }
}