package com.surajrathod.bcaprogram.utils

import android.graphics.Typeface
import android.view.View
import com.getkeepsafe.taptargetview.TapTarget
import com.surajrathod.bcaprogram.R

class TapTargetMaker {
    val color = R.color.unit_color
    fun createTapTarget(v : View,msg : String,info : String = "",color : Int = this.color): TapTarget? {
        return TapTarget.forView(v,msg,info)
            .outerCircleColor(color)
            .transparentTarget(true)
            .descriptionTextColor(R.color.white)
            .descriptionTypeface(Typeface.SANS_SERIF)
            .outerCircleAlpha(0.9f)
    }
}