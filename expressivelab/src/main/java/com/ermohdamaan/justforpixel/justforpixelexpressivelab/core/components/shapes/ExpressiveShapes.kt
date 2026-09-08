package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.shapes

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.cos
import kotlin.math.sin

/**
 * Collection of Advanced Material 3 Expressive Organic & Geometric Shapes.
 * Includes Star Polygon, Heart, Teardrop, Superellipse, and Organic Blob shapes.
 *
 * @author Er. Mohd Amaan
 */

/**
 * 5-Pointed Star Polygon Shape.
 */
class StarPolygonShape(private val points: Int = 5) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val outerRadius = minOf(centerX, centerY)
        val innerRadius = outerRadius * 0.45f
        val angleStep = Math.PI / points

        var angle = -Math.PI / 2.0
        path.moveTo(
            (centerX + outerRadius * cos(angle)).toFloat(),
            (centerY + outerRadius * sin(angle)).toFloat()
        )

        for (i in 0 until (points * 2)) {
            val radius = if (i % 2 == 0) innerRadius else outerRadius
            val x = (centerX + radius * cos(angle)).toFloat()
            val y = (centerY + radius * sin(angle)).toFloat()
            path.lineTo(x, y)
            angle += angleStep
        }
        path.close()
        return Outline.Generic(path)
    }
}

/**
 * Mathematical Heart Shape.
 */
class HeartShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val width = size.width
        val height = size.height

        path.moveTo(width / 2f, height * 0.80f)
        path.cubicTo(
            width * 0.10f, height * 0.50f,
            0f, height * 0.25f,
            width * 0.25f, height * 0.10f
        )
        path.cubicTo(
            width * 0.40f, 0f,
            width * 0.50f, height * 0.20f,
            width * 0.50f, height * 0.20f
        )
        path.cubicTo(
            width * 0.50f, height * 0.20f,
            width * 0.60f, 0f,
            width * 0.75f, height * 0.10f
        )
        path.cubicTo(
            width, height * 0.25f,
            width * 0.90f, height * 0.50f,
            width / 2f, height * 0.80f
        )
        path.close()
        return Outline.Generic(path)
    }
}

/**
 * Water Droplet / Teardrop Shape.
 */
class TeardropShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val width = size.width
        val height = size.height

        path.moveTo(width / 2f, 0f)
        path.cubicTo(
            width, height * 0.40f,
            width, height * 0.85f,
            width / 2f, height
        )
        path.cubicTo(
            0f, height * 0.85f,
            0f, height * 0.40f,
            width / 2f, 0f
        )
        path.close()
        return Outline.Generic(path)
    }
}
