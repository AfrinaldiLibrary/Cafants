package com.afrinaldi.cafants.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlantsModel(
    var image: Int,
    var water: String,
    var humedity: String
) : Parcelable