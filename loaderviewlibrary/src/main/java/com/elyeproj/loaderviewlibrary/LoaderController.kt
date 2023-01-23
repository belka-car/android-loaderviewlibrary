package com.elyeproj.loaderviewlibrary

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.view.animation.LinearInterpolator
import com.elyeproj.loaderviewlibrary.LoaderConstant.COLOR_DEFAULT_GRADIENT

/*
 * Copyright 2016 Elye Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class LoaderController(private val loaderView: LoaderView) : AnimatorUpdateListener {

    private val rect = RectF()
    private var rectPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

    private var linearGradient: LinearGradient? = null
    private var progress = 0f
    private var valueAnimator: ValueAnimator? = null
    private var widthWeight = LoaderConstant.MAX_WEIGHT
    private var heightWeight = LoaderConstant.MAX_WEIGHT
    private var useGradient = LoaderConstant.USE_GRADIENT_DEFAULT
    private var corners = LoaderConstant.CORNER_DEFAULT

    init {
        init()
    }

    private fun init() {
        rectPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        loaderView.setRectColor(rectPaint)
        valueAnimator = newValueAnimator(0.5f, 1f, ObjectAnimator.INFINITE)
    }

    @JvmOverloads
    fun onDraw(canvas: Canvas, paddingLeft: Float = 0f, paddingTop: Float = 0f, paddingRight: Float = 0f, paddingBottom: Float = 0f) {
        val marginHeight = canvas.height * (1 - heightWeight) / 2
        rectPaint.alpha = (progress * MAX_COLOR_CONSTANT_VALUE).toInt()

        if (useGradient) {
            prepareGradient(canvas.width * widthWeight)
        }

        rect.left = 0 + paddingLeft
        rect.top = marginHeight + paddingTop
        rect.right = canvas.width * widthWeight - paddingRight
        rect.bottom = canvas.height - marginHeight - paddingBottom

        canvas.drawRoundRect(
            rect,
            corners.toFloat(),
            corners.toFloat(),
            rectPaint,
        )
    }

    fun onSizeChanged() {
        linearGradient = null
        startLoading()
    }

    private fun prepareGradient(width: Float) {
        val linearGradient = linearGradient ?: LinearGradient(
            0f, 0f, width, 0f, rectPaint.color,
            COLOR_DEFAULT_GRADIENT, Shader.TileMode.MIRROR
        )
        rectPaint.shader = linearGradient
    }

    fun startLoading() {
        if (valueAnimator != null && !loaderView.valueSet()) {
            valueAnimator!!.cancel()
            init()
            valueAnimator!!.start()
        }
    }

    fun setHeightWeight(heightWeight: Float) {
        this.heightWeight = validateWeight(heightWeight)
    }

    fun setWidthWeight(widthWeight: Float) {
        this.widthWeight = validateWeight(widthWeight)
    }

    fun setUseGradient(useGradient: Boolean) {
        this.useGradient = useGradient
    }

    fun setCorners(corners: Int) {
        this.corners = corners
    }

    private fun validateWeight(weight: Float) = weight.coerceIn(LoaderConstant.MIN_WEIGHT, LoaderConstant.MAX_WEIGHT)

    fun stopLoading() {
        if (valueAnimator != null) {
            valueAnimator!!.cancel()
            valueAnimator = newValueAnimator(progress, 0f, 0)
                .apply { start() }
        }
    }

    private fun newValueAnimator(begin: Float, end: Float, repeatCount: Int): ValueAnimator =
        ValueAnimator.ofFloat(begin, end).apply {
            setRepeatCount(repeatCount)
            duration = ANIMATION_CYCLE_DURATION.toLong()
            repeatMode = ValueAnimator.REVERSE
            interpolator = LinearInterpolator()
            addUpdateListener(this@LoaderController)
        }

    override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
        progress = valueAnimator.animatedValue as Float
        loaderView.invalidate()
    }

    fun removeAnimatorUpdateListener() {
        if (valueAnimator != null) {
            valueAnimator!!.removeUpdateListener(this)
            valueAnimator!!.cancel()
        }
        progress = 0f
    }

    companion object {
        private const val MAX_COLOR_CONSTANT_VALUE = 255
        private const val ANIMATION_CYCLE_DURATION = 750 //milis
    }
}