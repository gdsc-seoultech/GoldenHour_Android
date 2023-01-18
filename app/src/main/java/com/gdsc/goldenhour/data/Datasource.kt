package com.gdsc.goldenhour.data

import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.model.GuideItem

class Datasource {
    fun loadGuideItems(): List<GuideItem> {
        return listOf(
            GuideItem(R.string.guide_title_1, R.drawable.guide_img_1),
            GuideItem(R.string.guide_title_2, R.drawable.guide_img_1),
            GuideItem(R.string.guide_title_3, R.drawable.guide_img_1),
            GuideItem(R.string.guide_title_4, R.drawable.guide_img_1),
            GuideItem(R.string.guide_title_5, R.drawable.guide_img_1),
            GuideItem(R.string.guide_title_6, R.drawable.guide_img_1),
            GuideItem(R.string.guide_title_7, R.drawable.guide_img_1),
            GuideItem(R.string.guide_title_8, R.drawable.guide_img_1)
        )
    }
}