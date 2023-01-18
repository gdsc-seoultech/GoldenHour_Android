package com.gdsc.goldenhour.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class GuideItem(
    @StringRes val stringResourceId: Int,
    @DrawableRes val imageResourceId: Int
)