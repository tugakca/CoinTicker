package com.android.cointicker.view

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.cointicker.App
import com.android.cointicker.R
import com.android.cointicker.model.CoinDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_profile.*


class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private  var coinDetail:CoinDetail?=null
    private var i =1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        if(intent.getSerializableExtra("coinDetail")!=null)
           coinDetail = intent.getSerializableExtra("coinDetail") as CoinDetail
        loginButtonTv.setOnClickListener {
            errorTv.visibility=View.GONE
            signIn()
        }
    }
    private fun signIn() {

        var userName=""
           if( !userEt.text.isNullOrEmpty()){
               userName=userEt.text.toString()
           }
        var password =""
        if(!passwordEt.text.isNullOrEmpty()){
            password=passwordEt.text.toString()
        }

        if(!userName.isNullOrEmpty() && !password.isNullOrEmpty())
        {
            minimizeButton(loginButtonTv, progressBar)
            mAuth.signInWithEmailAndPassword(userName, password)
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        App.userId = mAuth.currentUser!!.uid
                        if(coinDetail!=null){
                            val db = Firebase.firestore
                            val userV = hashMapOf(
                                "name" to coinDetail!!.name,
                                "currentPrice" to coinDetail!!.marketData.currentPrice.usd,
                            )
                            db.collection(App.userId!!)
                                .add(userV)
                                .addOnSuccessListener { documentReference ->
                                }
                                .addOnFailureListener { e ->
                                }
                        }
                        enlargeButton(loginButtonTv, progressBar)
                        val intent = Intent()
                        intent.putExtra("value", true)
                        setResult(RESULT_OK, intent)
                        val handler = Handler()
                        handler.postDelayed(Runnable {
                            finish()
                        }, 700)
                    } else {
                        errorTv.visibility=View.VISIBLE
                        errorTv.text=task.exception.toString()
                        enlargeButton(loginButtonTv, progressBar)
                    }
                }

        }else{
            errorTv.visibility=View.VISIBLE
            if(userName.isNullOrEmpty() &&password.isNullOrEmpty() ){
                errorTv.text="Please enter your username and password"

            }else  if(password.isNullOrEmpty()) {
                errorTv.text="Please enter your password"
            }else{
                errorTv.text="Please enter your username"
            }


        }


    }

    fun animate(v: View, END_WIDTH: Int, DURATION: Int) {
        val anim = ValueAnimator.ofInt(v.measuredWidth, END_WIDTH)
        anim.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            if (`val` > 50) {
                val layoutParams = v.layoutParams
                layoutParams.width = `val`
                v.layoutParams = layoutParams
            } else v.visibility = View.INVISIBLE
        }
        anim.duration = DURATION.toLong()
        anim.start()
    }

    fun minimizeButton(view: TextView, progressBar: ProgressBar) {
        view.setText("");
        animate(view, 0, 300);
        progressBar.getIndeterminateDrawable().setColorFilter(
            Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN
        );
        progressBar.setVisibility(View.VISIBLE);

    }


    fun enlargeButton(view: TextView, progressBar: ProgressBar) {
        view.setVisibility(View.VISIBLE);
        animate(view, loginLay.width, 300);
        Handler(Looper.getMainLooper()).postDelayed({
            view.setText("LOGIN");
            progressBar.setVisibility(View.GONE);


        }, 300)


    }
}