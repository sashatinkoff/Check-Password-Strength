package com.isidroid.basic.password_checker

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.animation.*
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import com.google.android.material.textfield.TextInputLayout
import com.isidroid.basic.R
import kotlin.math.roundToInt


class PasswordCheckView : TextInputLayout {
    enum class Strength {
        NONE, VERY_WEAK, WEAK, REASONABLE, STRONG
    }

    @ColorInt private var colorVeryWeak = Color.RED
    @ColorInt private var colorWeak = Color.CYAN
    @ColorInt private var colorReasonable = Color.BLUE
    @ColorInt private var colorStrong = Color.GREEN

    var nameVeryWeak: String? = null
    var nameWeak: String? = null
    var nameReasonable: String? = null
    var nameStrong: String? = null

    private var indicatorTextSize = -1
    private var indicatorHeight = 0
    private var indicatorMarginTop = 0
    private var animationDuration = 500
    private var animInterpolator: Interpolator = LinearInterpolator()
    private var indicatorTextGravity = Gravity.NO_GRAVITY

    private var currentStrength = Strength.NONE
    private lateinit var indicator: View
    private lateinit var indicatorTextView: TextView

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        onCreate(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        onCreate(context, attrs)
    }

    private fun onCreate(context: Context, attrs: AttributeSet) {
        style(context, attrs)

        indicator = View(context)
        addView(indicator)
        indicator.updateLayoutParams<LayoutParams> {
            height = indicatorHeight
            width = 0
            updateMargins(top = indicatorMarginTop)
        }

        indicatorTextView = TextView(context)
        addView(indicatorTextView)

        indicatorTextView.updateLayoutParams<LayoutParams> {
            indicatorTextView.width = LayoutParams.MATCH_PARENT
            indicatorTextView.height =
                if (indicatorTextSize > 0) (indicatorTextSize * 1.3).toInt() else
                    with(resources.displayMetrics) { (16 * (xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt() }

            updateMargins(top = indicatorMarginTop)

            indicatorTextView.apply {
                gravity = indicatorTextGravity
                if (indicatorTextSize > 0)
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, indicatorTextSize.toFloat())
            }
        }
    }

    private fun style(context: Context, attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.PasswordCheckView)

        colorVeryWeak =
            a.getColor(R.styleable.PasswordCheckView_pass_check_color_very_weak, colorVeryWeak)
        colorWeak = a.getColor(R.styleable.PasswordCheckView_pass_check_color_weak, colorVeryWeak)
        colorReasonable =
            a.getColor(R.styleable.PasswordCheckView_pass_check_color_reasonable, colorVeryWeak)
        colorStrong =
            a.getColor(R.styleable.PasswordCheckView_pass_check_color_strong, colorVeryWeak)

        nameVeryWeak = a.getString(R.styleable.PasswordCheckView_pass_check_name_very_weak)
        nameWeak = a.getString(R.styleable.PasswordCheckView_pass_check_name_weak)
        nameReasonable = a.getString(R.styleable.PasswordCheckView_pass_check_name_reasonable)
        nameStrong = a.getString(R.styleable.PasswordCheckView_pass_check_name_strong)

        animationDuration =
            a.getInteger(R.styleable.PasswordCheckView_pass_check_animation_duration, 500)
        indicatorHeight =
            a.getDimensionPixelSize(R.styleable.PasswordCheckView_pass_check_indicator_height, 2)
        indicatorTextSize =
            a.getDimensionPixelSize(
                R.styleable.PasswordCheckView_pass_check_indicator_text_size,
                -1
            )

        indicatorMarginTop =
            a.getDimensionPixelSize(R.styleable.PasswordCheckView_pass_check_indicator_marginTop, 2)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            animInterpolator =
                when (a.getInteger(
                    R.styleable.PasswordCheckView_pass_check_anim_interpolator,
                    100
                )) {
                    0 -> DecelerateInterpolator()
                    1 -> AccelerateDecelerateInterpolator()
                    2 -> AccelerateInterpolator()
                    3 -> BounceInterpolator()
                    4 -> AnticipateInterpolator()
                    5 -> AnticipateOvershootInterpolator()
                    else -> LinearInterpolator()
                }
        }

        indicatorTextGravity =
            when (a.getInteger(
                R.styleable.PasswordCheckView_pass_check_indicator_text_gravity,
                100
            )) {
                0 -> Gravity.START
                1 -> Gravity.CENTER
                2 -> Gravity.END
                else -> Gravity.NO_GRAVITY
            }

        a.recycle()
    }

    fun onTextChanged(strength: Strength) {
        if (strength != currentStrength) {
            indicatorTextView.text = indicatorText(strength)
            currentStrength = strength
            indicator.setBackgroundColor(indicatorColor(strength))
            indicatorTextView.setTextColor(indicatorColor(strength))

            val width =
                (this@PasswordCheckView.width * (strength.ordinal).toFloat() / (Strength.values().size - 1).toFloat()).toInt()

            startAnimation(width)
        }
    }

    private fun startAnimation(width: Int) {
        ValueAnimator.ofInt(indicator.width, width).apply {
            duration = animationDuration.toLong()
            interpolator = animInterpolator
            addUpdateListener { animation ->
                indicator.layoutParams.width = animation.animatedValue as Int
                indicator.requestLayout()
            }

            start()
        }
    }

    private fun indicatorColor(strength: Strength) = when (strength) {
        Strength.VERY_WEAK, Strength.NONE -> colorVeryWeak
        Strength.WEAK -> colorWeak
        Strength.REASONABLE -> colorReasonable
        Strength.STRONG -> colorStrong
    }

    private fun indicatorText(strength: Strength) = when (strength) {
        Strength.NONE -> ""
        Strength.VERY_WEAK -> nameVeryWeak
        Strength.WEAK -> nameWeak
        Strength.REASONABLE -> nameReasonable
        Strength.STRONG -> nameStrong
    }.orEmpty()
}