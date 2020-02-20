package com.example.newsstand.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String?){
    if (!url.isNullOrEmpty())
        Glide.with(imageView.context).load(url).into(imageView)
}

@BindingAdapter("formattedDate")
fun TextView.setFormattedDate(dateString: String?){
    if(!dateString.isNullOrEmpty()) {
        val delimiter1 = 'Z'
        val delimiter2 = '+'
        dateString.split(delimiter1, delimiter2)[0]
        var format = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.ENGLISH)
        val date = try {
            format.parse(dateString)
        } catch (e: Exception) {
            Timber.d(e)
        }
        format = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        text = if (date != null) format.format(date) else ""
    }
}

@BindingAdapter("articleDescription")
fun TextView.truncateText(description: String?){
    if (!description.isNullOrEmpty())
        text = description.smartTruncate(250)
}


private val PUNCTUATION = listOf(", ", "; ", ": ", " ")

fun String.smartTruncate(length: Int): String {
    val words = split(" ")
    var added = 0
    var hasMoreWords = false
    val builder = StringBuilder()
    for (word in words) {
        if (builder.length > length) {
            hasMoreWords = true
            break
        }
        builder.append(word)
        builder.append(" ")
        added += 1
    }
    PUNCTUATION.map {
        if (builder.endsWith(it)) {
            builder.replace(builder.length - it.length, builder.length, "")
        }
    }
    if (hasMoreWords) {
        builder.append("...")
    }
    return builder.toString()
}