package com.thomaskuenneth.splashscreendemo

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.window.SplashScreen
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private const val TAG = "MainActivity"

@SuppressLint("CustomSplashScreen")
class SplashScreenDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var ready = false
        lifecycleScope.launch(Dispatchers.IO) {
            delay(5000)
            ready = true
        }
        val content = findViewById<View>(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (ready) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )

        val l = SplashScreen.OnExitAnimationListener { view ->
            Log.d(TAG, view::class.java.name)
            view.iconView?.let { icon ->
                val animator = ValueAnimator
                    .ofInt(icon.height, 0)
                    .setDuration(2000)
                animator.addUpdateListener {
                    val value = it.animatedValue as Int
                    icon.layoutParams.width = value
                    icon.layoutParams.height = value
                    icon.requestLayout()
                    if (value == 0) {
                        view.remove()
                    }
                }
                val animationSet = AnimatorSet()
                animationSet.interpolator = AccelerateDecelerateInterpolator()
                animationSet.play(animator)
                animationSet.start()
            }
        }
        splashScreen.setOnExitAnimationListener(l)
    }
}