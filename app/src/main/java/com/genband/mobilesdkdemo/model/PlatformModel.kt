package com.genband.mobilesdkdemo.model

data class PlatformModel(
    val id: Int,
    val name: String,
    val turnAddresses : List<String>,
    val accounts : List<AccountsData>
)
