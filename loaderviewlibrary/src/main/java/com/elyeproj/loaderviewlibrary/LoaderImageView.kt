package com.elyeproj.loaderviewlibrary

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
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
class LoaderImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatImageView(context, attrs, defStyleAttr), LoaderView {

    private val loaderController = LoaderController(this)
    private var defaultColorResource = 0

    init {
        context.obtainStyledAttributes(attrs, R.styleable.loader_view, 0, 0).use { typedArray ->
            loaderController.setUseGradient(typedArray.getBoolean(R.styleable.loader_view_use_gradient, LoaderConstant.USE_GRADIENT_DEFAULT))
            loaderController.setCorners(typedArray.getInt(R.styleable.loader_view_corners, LoaderConstant.CORNER_DEFAULT))
            defaultColorResource = typedArray.getColor(R.styleable.loader_view_custom_color, ContextCompat.getColor(context, R.color.default_color))
        }
    }

    fun resetLoader() {
        if (drawable != null) {
            super.setImageDrawable(null)
            loaderController.startLoading()
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        loaderController.onSizeChanged()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        loaderController.onDraw(canvas)
    }

    override fun setRectColor(rectPaint: Paint) {
        rectPaint.color = defaultColorResource
    }

    override fun valueSet(): Boolean = drawable != null

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        loaderController.stopLoading()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        loaderController.stopLoading()
    }

    override fun setImageIcon(icon: Icon?) {
        super.setImageIcon(icon)
        loaderController.stopLoading()
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        loaderController.stopLoading()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        loaderController.removeAnimatorUpdateListener()
    }
}