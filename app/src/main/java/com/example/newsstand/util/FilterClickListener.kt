package com.example.newsstand.util

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.MenuItem
import android.view.View
import android.view.animation.Interpolator

class FilterClickListener @JvmOverloads internal constructor(
    val context: Context,
    private val sheet: View,
    private val interpolator: Interpolator? = null,
    private val vis: () -> Unit) : MenuItem.OnMenuItemClickListener{

    private val animatorSet = AnimatorSet()
    private val height: Int
    private var backdropShown = false

    init {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        vis.invoke()
        backdropShown = !backdropShown

        animatorSet.removeAllListeners()
        animatorSet.end()
        animatorSet.cancel()
        val translateY = height
        val animator = ObjectAnimator.ofFloat(
            sheet,
            "translationY",
            (if (backdropShown) translateY else 0).toFloat())

        animator.duration = 500
        if (interpolator != null) {
            animator.interpolator = interpolator
        }
        animatorSet.play(animator)
        animator.start()
        return true
    }

}