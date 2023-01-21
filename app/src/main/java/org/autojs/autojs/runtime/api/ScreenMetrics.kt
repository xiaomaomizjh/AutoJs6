package org.autojs.autojs.runtime.api

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.Display
import android.view.Surface
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

/**
 * Created by Stardust on 2017/4/26.
 */
class ScreenMetrics(private var designWidth: Int, private var designHeight: Int) {

    constructor() : this(0, 0)

    fun setScreenMetrics(width: Int, height: Int) {
        designWidth = width
        designHeight = height
    }

    @JvmOverloads
    fun scaleX(x: Int, width: Int = designWidth) = when {
        width == 0 || activity == null -> x
        else -> x * deviceScreenWidth / width
    }

    @JvmOverloads
    fun scaleY(y: Int, height: Int = designHeight) = when {
        height == 0 || activity == null -> y
        else -> y * deviceScreenHeight / height
    }

    @JvmOverloads
    fun rescaleX(x: Int, width: Int = designWidth) = when {
        width == 0 || activity == null -> x
        else -> x * width / deviceScreenWidth
    }

    @JvmOverloads
    fun rescaleY(y: Int, height: Int = designHeight) = when {
        height == 0 || activity == null -> y
        else -> y * height / deviceScreenHeight
    }

    companion object {

        private val metrics: DisplayMetrics = DisplayMetrics()

        private var activity: Activity? = null

        @Suppress("DEPRECATION")
        private val defaultDisplay: Display?
            get() = activity?.windowManager?.defaultDisplay

        @JvmStatic
        val rotation: Int
            get() = defaultDisplay?.rotation ?: 0

        @JvmStatic
        val deviceScreenWidth: Int
            get() = toOriAwarePoint(metrics.widthPixels, metrics.heightPixels).x

        @JvmStatic
        val deviceScreenHeight: Int
            get() = toOriAwarePoint(metrics.widthPixels, metrics.heightPixels).y

        @JvmStatic
        val deviceScreenDensity: Int
            get() = metrics.densityDpi

        @JvmStatic
        fun initIfNeeded(activity: Activity) {
            this.activity ?: let {
                this.activity = activity
                @Suppress("DEPRECATION")
                defaultDisplay?.getRealMetrics(this.metrics)
            }
        }

        @JvmStatic
        fun isScreenLandscape() = listOf(Surface.ROTATION_90, Surface.ROTATION_270).contains(rotation)

        private fun toOriAwarePoint(a: Int, b: Int) = arrayOf(minOf(a, b), maxOf(a, b))
            .apply { if (isScreenLandscape()) reverse() }
            .let { Point(it[0], it[1]) }

        @JvmStatic
        fun getOrientationAwareScreenWidth(orientation: Int) = when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> deviceScreenHeight
            else -> deviceScreenWidth
        }

        @JvmStatic
        fun getOrientationAwareScreenHeight(orientation: Int) = when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> deviceScreenWidth
            else -> deviceScreenHeight
        }

        @JvmOverloads
        fun toRoundIntX(value: Double, enableMinusOneMetric: Boolean = true) = when {
            value == -1.0 && enableMinusOneMetric -> deviceScreenWidth
            -1.0 < value && value < 1.0 -> (value * deviceScreenWidth).roundToInt()
            else -> value.roundToInt()
        }

        @JvmOverloads
        fun toRoundIntY(value: Double, enableMinusOneMetric: Boolean = true) = when {
            value == -1.0 && enableMinusOneMetric -> deviceScreenHeight
            -1.0 < value && value < 1.0 -> (value * deviceScreenHeight).roundToInt()
            else -> value.roundToInt()
        }

        @JvmOverloads
        fun toFloorIntX(value: Double, enableMinusOneMetric: Boolean = true) = when {
            value == -1.0 && enableMinusOneMetric -> deviceScreenWidth
            -1.0 < value && value < 1.0 -> floor(value * deviceScreenWidth).toInt()
            else -> floor(value).toInt()
        }

        @JvmOverloads
        fun toFloorIntY(value: Double, enableMinusOneMetric: Boolean = true) = when {
            value == -1.0 && enableMinusOneMetric -> deviceScreenHeight
            -1.0 < value && value < 1.0 -> floor(value * deviceScreenHeight).toInt()
            else -> floor(value).toInt()
        }

        @JvmOverloads
        fun toCeilIntX(value: Double, enableMinusOneMetric: Boolean = true) = when {
            value == -1.0 && enableMinusOneMetric -> deviceScreenWidth
            -1.0 < value && value < 1.0 -> ceil(value * deviceScreenWidth).toInt()
            else -> ceil(value).toInt()
        }

        @JvmOverloads
        fun toCeilIntY(value: Double, enableMinusOneMetric: Boolean = true) = when {
            value == -1.0 && enableMinusOneMetric -> deviceScreenHeight
            -1.0 < value && value < 1.0 -> ceil(value * deviceScreenHeight).toInt()
            else -> ceil(value).toInt()
        }

    }

}