package com.genband.mobilesdkdemo.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Configurations(
    var ICECollectionTimeout: Int,
    var restServerIP: String,
    var restServerPort: String,
    var webSocketServerIP: String,
    var webSocketServerPort: String
) : Parcelable {
    constructor() : this(3,"","","","")
}
