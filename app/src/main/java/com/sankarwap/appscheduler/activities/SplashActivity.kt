package com.sankarwap.appscheduler.activities

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.sankarwap.appscheduler.R


class SplashActivity : AppCompatActivity() {

    lateinit var animation_view: LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        animation_view=findViewById(R.id.animation_view)
        animation_view.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                Log.e("Animation:", "start")
            }
            override fun onAnimationEnd(animation: Animator) {
                Log.e("Animation:", "end")
                startActivity(Intent(this@SplashActivity,MainActivity::class.java))
                finish()
            }
            override fun onAnimationCancel(animation: Animator) {
                Log.e("Animation:", "cancel")
            }
            override fun onAnimationRepeat(animation: Animator) {
                Log.e("Animation:", "repeat")
            }
        })
    }
}