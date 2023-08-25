package com.genband.mobilesdkdemo.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IceServers(
    var servers : List<String>
) : Parcelable {
    constructor() : this(arrayListOf())
}
