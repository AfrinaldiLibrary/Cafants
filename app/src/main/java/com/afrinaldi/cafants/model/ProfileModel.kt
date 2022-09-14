package com.afrinaldi.cafants.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileModel(
    var name : String
) : Parcelable