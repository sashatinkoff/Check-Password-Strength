package com.isidroid.basic.password_checker

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.annotation.CallSuper

open class PasswordTextWatcher(
    private val passwordCheckView: PasswordCheckView,
    private val checkRule: ((String) -> PasswordCheckView.Strength)? = null
) : TextWatcher {

    private fun onTextChanged(text: String) = checkRule
        ?.invoke(text)
        ?: run {
            when {
                text.isEmpty() -> PasswordCheckView.Strength.NONE
                text.length < 5 -> PasswordCheckView.Strength.VERY_WEAK
                text.length < 9 -> PasswordCheckView.Strength.WEAK
                text.length < 12 -> PasswordCheckView.Strength.REASONABLE
                else -> PasswordCheckView.Strength.STRONG
            }
        }

    @CallSuper override fun afterTextChanged(edit: Editable?) {}
    @CallSuper override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    @CallSuper override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        val text = p0.toString()
        passwordCheckView.onTextChanged(strength = onTextChanged(text))
    }
}

/**
 * Call in onResume in Activity / Fragment to subscribe
 */
fun EditText.attach(watcher: PasswordTextWatcher) {
    addTextChangedListener(watcher)
}

/**
 * Call in onPause in Activity / Fragment to unsubscribe
 */
fun EditText.detach(watcher: PasswordTextWatcher) {
    removeTextChangedListener(watcher)
}