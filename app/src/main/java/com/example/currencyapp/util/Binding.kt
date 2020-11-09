package com.example.currencyapp.util

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("android:setBitmap")
fun ImageView.setBitmap(bitmap: Bitmap?) {
    setImageBitmap(bitmap)
}