package com.meta.levinriegner.mediaview.data.whatsnew.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewFeature(
    val id: Long,
    val title: String,
    val description: String,
) : Parcelable