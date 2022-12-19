package com.filmsaboinf.ofnimlif.repository.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class FilmPreview(
    val name: String,
    val img: String,
    val urlDetail: String,
    var description: String? = null,
    var country: String? = null,
    var rate: String? = null,
) : Parcelable
