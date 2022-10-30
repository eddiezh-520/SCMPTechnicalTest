package com.scmp.android.model

import com.google.gson.annotations.SerializedName

data class StaffList(
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    val total: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    val data: List<StaffInfo>
)

data class StaffInfo(
    val id: Int,
    val name: String,
    val year: Int,
    val color: String,
    @SerializedName("pantone_value")
    val pantoneValue: String
)

class LoadMore
