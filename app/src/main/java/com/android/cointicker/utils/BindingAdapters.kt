package com.android.cointicker.utils

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.android.cointicker.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import java.text.DecimalFormat


@BindingAdapter("setFavIcon")
fun setFavIcon(view: ImageView, isFav:Boolean) {
    view.bindFav(isFav, placeholderProgressBar(view.context))
}



@BindingAdapter("formatPrice")
fun setPrice( view: TextView,price: Double){
//    val format = DecimalFormat("#.######")
//    var formattedPrice = format.format(price)
    view.text= "$"+price
}

@BindingAdapter("currentPercentage")
fun setPercentage( view: TextView,value: Double){
//    val format = DecimalFormat("#.######")
//    var formattedPrice = format.format(price)
    view.text= "Price Change in 24H : %"+value
}

@BindingAdapter("setHashingAlgo")
fun sethashAlgo( view: TextView,value: String?){
     var hashingAlgo = "null"
    if(!value.isNullOrEmpty())
        hashingAlgo=value

    view.text= "Hashing Algorithm :" +value
}
@BindingAdapter("setDescription")
fun setDescription( view: TextView,value: String?){
    var description = "There is no description for related coin"
    if(!value.isNullOrEmpty())
        description=value

    view.text= description
}


@BindingAdapter("setIcon")
fun setIcon(view: ImageView, url:String?) {
    view.bindImage(url, placeholderProgressBar(view.context))
}

fun ImageView.bindImage(url:String?, progress: CircularProgressDrawable) {
    url?.let {
        val options = RequestOptions()
            .placeholder(progress)
            .error(R.mipmap.ic_launcher)
        Glide.with(context)
            .setDefaultRequestOptions(options).load(url)
            .into(this)
    }
}
fun ImageView.bindFav(isFav:Boolean, progress: CircularProgressDrawable) {
    var drawIcon:Int=R.drawable.favorite
    if(!isFav)
        drawIcon =R.drawable.not_favorite

    val options = RequestOptions()
        .placeholder(progress)
        .error(R.mipmap.ic_launcher)
    Glide.with(context)
        .setDefaultRequestOptions(options).load(drawIcon)
        .into(this)
}

fun placeholderProgressBar(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}
