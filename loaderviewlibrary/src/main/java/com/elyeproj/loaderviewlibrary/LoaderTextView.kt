package com.elyeproj.loaderviewlibrary

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat

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
class LoaderTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatTextView(context, attrs, defStyleAttr), LoaderView {

    private val loaderController: LoaderController = LoaderController(this)
    private var defaultColorResource = 0
    private var darkerColorResource = 0

    init {
        context.obtainStyledAttributes(attrs, R.styleable.loader_view, 0, 0).use { typedArray ->
            loaderController.setWidthWeight(typedArray.getFloat(R.styleable.loader_view_width_weight, LoaderConstant.MAX_WEIGHT))
            loaderController.setHeightWeight(typedArray.getFloat(R.styleable.loader_view_height_weight, LoaderConstant.MAX_WEIGHT))
            loaderController.setUseGradient(typedArray.getBoolean(R.styleable.loader_view_use_gradient, LoaderConstant.USE_GRADIENT_DEFAULT))
            loaderController.setCorners(typedArray.getInt(R.styleable.loader_view_corners, LoaderConstant.CORNER_DEFAULT))
            defaultColorResource = typedArray.getColor(R.styleable.loader_view_custom_color, ContextCompat.getColor(context, R.color.default_color))
            darkerColorResource = typedArray.getColor(R.styleable.loader_view_custom_color, ContextCompat.getColor(context, R.color.darker_color))
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        loaderController.onSizeChanged()
    }

    fun resetLoader() {
        if (text.isNotEmpty()) {
            super.setText(null)
            loaderController.startLoading()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        loaderController.onDraw(
            canvas,
            compoundPaddingLeft.toFloat(),
            compoundPaddingTop.toFloat(),
            compoundPaddingRight.toFloat(),
            compoundPaddingBottom.toFloat()
        )
    }

    override fun setText(text: CharSequence, type: BufferType) {
        super.setText(text, type)
        loaderController.stopLoading()
    }

    override fun setRectColor(rectPaint: Paint) {
        if (typeface != null && typeface.style == Typeface.BOLD) {
            rectPaint.color = darkerColorResource
        } else {
            rectPaint.color = defaultColorResource
        }
    }

    override fun valueSet(): Boolean = text.isNotEmpty()

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        loaderController.removeAnimatorUpdateListener()
    }
}