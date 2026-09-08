package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme

import androidx.compose.ui.unit.dp
import racra.compose.smooth_corner_rect_library.AbsoluteSmoothCornerShape

/**
 * Optimized cache for Material 3 Expressive smooth corner shapes (Squircles).
 *
 * Re-using instances of [AbsoluteSmoothCornerShape] prevents unnecessary allocations
 * during recomposition and layout passes, guaranteeing butter-smooth 60fps animations.
 *
 * @author Er. Mohd Amaan
 * @see AbsoluteSmoothCornerShape
 */
object ShapeCache {
    /** Smooth corner shape with 8dp radius and 60% smoothness */
    val smooth8 = AbsoluteSmoothCornerShape(cornerRadius = 8.dp, smoothnessAsPercent = 60)

    /** Smooth corner shape with 12dp radius and 60% smoothness */
    val smooth12 = AbsoluteSmoothCornerShape(cornerRadius = 12.dp, smoothnessAsPercent = 60)

    /** Smooth corner shape with 16dp radius and 60% smoothness */
    val smooth16 = AbsoluteSmoothCornerShape(cornerRadius = 16.dp, smoothnessAsPercent = 60)

    /** Smooth corner shape with 20dp radius and 60% smoothness */
    val smooth20 = AbsoluteSmoothCornerShape(cornerRadius = 20.dp, smoothnessAsPercent = 60)

    /** Smooth corner shape with 24dp radius and 60% smoothness */
    val smooth24 = AbsoluteSmoothCornerShape(cornerRadius = 24.dp, smoothnessAsPercent = 60)

    /** Smooth corner shape with 32dp radius and 60% smoothness */
    val smooth32 = AbsoluteSmoothCornerShape(cornerRadius = 32.dp, smoothnessAsPercent = 60)

    /** Fully smooth Pill shape (50dp corner radius) for buttons and chips */
    val smoothPill = AbsoluteSmoothCornerShape(cornerRadius = 50.dp, smoothnessAsPercent = 60)
}
