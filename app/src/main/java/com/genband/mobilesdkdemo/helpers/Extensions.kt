package com.genband.mobilesdkdemo.helpers

import android.view.View

// view extensions


// hide view
fun View.hide() {
    visibility = View.GONE
}

// show view
fun View.show() {
    visibility = View.VISIBLE
}

// invisible view
fun View.invisible() {
    visibility = View.INVISIBLE
}

// set view visibility with boolean
fun View.visible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}
