package com.isidroid.basic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.isidroid.basic.password_checker.PasswordTextWatcher
import com.isidroid.basic.password_checker.attach
import com.isidroid.basic.password_checker.detach
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val passwordTextWatcher by lazy { PasswordTextWatcher(textInputLayout) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        editText.attach(passwordTextWatcher)
    }

    override fun onPause() {
        super.onPause()
        editText.detach(passwordTextWatcher)
    }
}