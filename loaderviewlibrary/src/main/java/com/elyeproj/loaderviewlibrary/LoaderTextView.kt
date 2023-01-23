package com.elyeproj.loaderviewlibrary

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextUtils
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
 */   class LoaderTextView : AppCompatTextView, LoaderView {
    private var loaderController: LoaderController? = null
    private var defaultColorResource = 0
    private var darkerColorResource = 0

    constructor(context: Context?) : super(context!!) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        loaderController = LoaderController(this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.loader_view, 0, 0)
        loaderController!!.setWidthWeight(typedArray.getFloat(R.styleable.loader_view_width_weight, LoaderConstant.MAX_WEIGHT))
        loaderController!!.setHeightWeight(typedArray.getFloat(R.styleable.loader_view_height_weight, LoaderConstant.MAX_WEIGHT))
        loaderController!!.setUseGradient(typedArray.getBoolean(R.styleable.loader_view_use_gradient, LoaderConstant.USE_GRADIENT_DEFAULT))
        loaderController!!.setCorners(typedArray.getInt(R.styleable.loader_view_corners, LoaderConstant.CORNER_DEFAULT))
        defaultColorResource = typedArray.getColor(R.styleable.loader_view_custom_color, ContextCompat.getColor(context, R.color.default_color))
        darkerColorResource = typedArray.getColor(R.styleable.loader_view_custom_color, ContextCompat.getColor(context, R.color.darker_color))
        typedArray.recycle()
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        loaderController!!.onSizeChanged()
    }

    fun resetLoader() {
        if (!TextUtils.isEmpty(text)) {
            super.setText(null)
            loaderController!!.startLoading()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        loaderController!!.onDraw(
            canvas, compoundPaddingLeft.toFloat(),
            compoundPaddingTop.toFloat(),
            compoundPaddingRight.toFloat(),
            compoundPaddingBottom.toFloat()
        )
    }

    override fun setText(text: CharSequence, type: BufferType) {
        super.setText(text, type)
        if (loaderController != null) {
            loaderController!!.stopLoading()
        }
    }

    override fun setRectColor(rectPaint: Paint?) {
        val typeface = typeface
        if (typeface != null && typeface.style == Typeface.BOLD) {
            rectPaint!!.color = darkerColorResource
        } else {
            rectPaint!!.color = defaultColorResource
        }
    }

    override fun valueSet(): Boolean {
        return !TextUtils.isEmpty(text)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        loaderController!!.removeAnimatorUpdateListener()
    }
}