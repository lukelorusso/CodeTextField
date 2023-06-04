package com.lukelorusso.codetextfield.extension

import android.app.Activity

fun Activity.hideKeyboard() = currentFocus?.also { it.hideKeyboard() }
