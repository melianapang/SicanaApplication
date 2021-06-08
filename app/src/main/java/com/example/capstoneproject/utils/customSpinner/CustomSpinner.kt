package com.example.capstoneproject.utils.customSpinner

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.res.ResourcesCompat
import com.example.capstoneproject.R

class CustomSpinner : AppCompatSpinner {
    private var upBackground: Drawable? = null
    private var downBackground: Drawable? = null

    interface OnSpinnerEventsListener {
        fun onPopupWindowOpened(spinner: Spinner?)
        fun onPopupWindowClosed(spinner: Spinner?)
    }

    private var mListener: OnSpinnerEventsListener? = null
    private var mOpenInitiated = false

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        downBackground = ResourcesCompat.getDrawable(resources, R.drawable.spinner_bg, null)
        upBackground = ResourcesCompat.getDrawable(resources, R.drawable.spinner_bg_up, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = downBackground
    }

    override fun performClick(): Boolean {
        mOpenInitiated = true
        if (mListener != null) {
            background = upBackground
            mListener!!.onPopupWindowOpened(this)
        }
        return super.performClick()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasBeenOpened() && hasFocus) {
            performClosedEvent()
            background = downBackground
        }
        else{
            background = upBackground
        }
    }

    fun setSpinnerEventsListener(onSpinnerEventsListener: OnSpinnerEventsListener?) {
        mListener = onSpinnerEventsListener
    }

    fun performClosedEvent() {
        mOpenInitiated = false
        if (mListener != null) {
            background = downBackground
            mListener!!.onPopupWindowClosed(this)
        }
    }

    fun hasBeenOpened(): Boolean {
        return mOpenInitiated
    }
}