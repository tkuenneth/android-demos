package com.thomaskuenneth.splashscreendemo

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.window.SplashScreen
import androidx.appcompat.app.AppCompatActivity


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "--> ${splashScreen::class.java.name}")
        setContentView(R.layout.activity_main)
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
                animationSet.play(animator);
                animationSet.start()
            }
        }
        splashScreen.setOnExitAnimationListener(l)
    }
}