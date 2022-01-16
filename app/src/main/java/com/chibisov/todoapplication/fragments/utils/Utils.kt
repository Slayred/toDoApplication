package com.chibisov.todoapplication.fragments.utils

import android.app.Activity
import android.content.Context
import android.hardware.input.InputManager
import android.view.inputmethod.InputMethodManager

fun hideKeyboard(activity: Activity) {
    val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    val currentFocusView = activity.currentFocus
    currentFocusView?.let {
        inputMethodManager.hideSoftInputFromWindow(
            currentFocusView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}