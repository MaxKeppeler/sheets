/*
 *  Copyright (C) 2020. Maximilian Keppeler (https://www.maxkeppeler.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

@file:Suppress("unused")

package com.maxkeppeler.sheets.info

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.maxkeppeler.sheets.core.Sheet
import com.maxkeppeler.sheets.core.PositiveListener
import com.maxkeppeler.sheets.core.utils.getIconColor
import com.maxkeppeler.sheets.info.databinding.SheetsInfoBinding

/**
 * The [InfoSheet] lets you display an information or warning.
 */
class InfoSheet : Sheet() {

    override val dialogTag = "InfoSheet"

    companion object {
        private const val STATE_CONTENT_TEXT = "state_content_text"
        private const val STATE_DISPLAY_BUTTONS = "state_display_buttons"
        private const val STATE_DRAWABLE = "state_drawable"
        private const val STATE_DRAWABLE_COLOR = "state_drawable_color"
    }

    private lateinit var binding: SheetsInfoBinding

    private var contentText: String? = null
    private var displayButtons = true

    @DrawableRes
    private var drawableRes: Int? = null

    @ColorInt
    private var drawableColor: Int? = null

    /**
     * Set the content of the sheet.
     *
     * @param content The text for the content.
     */
    fun content(content: String) {
        this.contentText = content
    }

    /**
     * Set the content of the sheet.
     *
     * @param contentRes The String resource id for the content.
     */
    fun content(@StringRes contentRes: Int) {
        this.contentText = windowContext.getString(contentRes)
    }

    /**
     * Set the content of the sheet.
     *
     * @param contentRes Resource id for the format string
     * @param formatArgs The format arguments that will be used for
     *                   substitution.
     */
    fun content(@StringRes contentRes: Int, vararg formatArgs: Any?) {
        this.contentText = windowContext.getString(contentRes, *formatArgs)
    }

    /** Set a drawable left of the text. */
    fun drawable(@DrawableRes drawableRes: Int) {
        this.drawableRes = drawableRes
    }

    /** Set the color of the drawable. */
    fun drawableColor(@ColorRes colorRes: Int) {
        this.drawableColor = ContextCompat.getColor(windowContext, colorRes)
    }

    /**
     * Set a listener.
     *
     * @param positiveListener Listener that is invoked when the positive button is clicked.
     */
    fun onPositive(positiveListener: PositiveListener) {
        this.positiveListener = positiveListener
    }

    /**
     * Set the text of the positive button and optionally a listener.
     *
     * @param positiveText The text for the positive button.
     * @param positiveListener Listener that is invoked when the positive button is clicked.
     */
    fun onPositive(positiveText: String, positiveListener: PositiveListener? = null) {
        this.positiveText = positiveText
        this.positiveListener = positiveListener
    }

    /**
     * Set the text of the positive button and optionally a listener.
     *
     * @param positiveRes The String resource id for the positive button.
     * @param positiveListener Listener that is invoked when the positive button is clicked.
     */
    fun onPositive(@StringRes positiveRes: Int, positiveListener: PositiveListener? = null) {
        this.positiveText = windowContext.getString(positiveRes)
        this.positiveListener = positiveListener
    }

    /**
     * Set the text and icon of the positive button and optionally a listener.
     *
     * @param positiveText The text for the positive button.
     * @param drawableRes The drawable resource for the button icon.
     * @param positiveListener Listener that is invoked when the positive button is clicked.
     */
    fun onPositive(
        positiveText: String,
        @DrawableRes drawableRes: Int,
        positiveListener: PositiveListener? = null
    ) {
        this.positiveText = positiveText
        this.positiveButtonDrawableRes = drawableRes
        this.positiveListener = positiveListener
    }

    /**
     * Set the text and icon of the positive button and optionally a listener.
     *
     * @param positiveRes The String resource id for the positive button.
     * @param drawableRes The drawable resource for the button icon.
     * @param positiveListener Listener that is invoked when the positive button is clicked.
     */
    fun onPositive(
        @StringRes positiveRes: Int,
        @DrawableRes drawableRes: Int,
        positiveListener: PositiveListener? = null
    ) {
        this.positiveText = windowContext.getString(positiveRes)
        this.positiveButtonDrawableRes = drawableRes
        this.positiveListener = positiveListener
    }

    /** Display buttons and require a positive button click. */
    fun displayButtons(displayButtons: Boolean = true) {
        this.displayButtons = displayButtons
    }

    override fun onCreateLayoutView(): View =
        SheetsInfoBinding.inflate(LayoutInflater.from(activity)).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayButtonsView(displayButtons)
        with(binding) {
            contentText?.let { content.text = it }
            drawableRes?.let { res ->
                val drawable = ContextCompat.getDrawable(requireContext(), res)
                icon.setImageDrawable(drawable)
                icon.setColorFilter(drawableColor ?: getIconColor(requireContext()))
                icon.visibility = View.VISIBLE
            }
        }
    }

    override fun onRestoreCustomViewInstanceState(savedState: Bundle?) {
        savedState?.let { saved ->
            contentText = saved.getString(STATE_CONTENT_TEXT)
            drawableColor = saved.get(STATE_DRAWABLE_COLOR) as Int?
            drawableRes = saved.get(STATE_DRAWABLE) as Int?
        }
    }

    override fun onSaveCustomViewInstanceState(outState: Bundle) {
        with(outState) {
            drawableColor?.let { outState.putInt(STATE_DRAWABLE_COLOR, it) }
            drawableRes?.let { putInt(STATE_DRAWABLE, it) }
            putString(STATE_CONTENT_TEXT, contentText)
        }
    }

    /** Build [InfoSheet] and show it later. */
    fun build(ctx: Context, viewWidth: Int = ViewGroup.LayoutParams.MATCH_PARENT, func: InfoSheet.() -> Unit): InfoSheet {
        this.windowContext = ctx
        this.viewWidth = viewWidth
        this.func()
        return this
    }

    /** Build and show [InfoSheet] directly. */
    fun show(ctx: Context, viewWidth: Int = ViewGroup.LayoutParams.MATCH_PARENT, func: InfoSheet.() -> Unit): InfoSheet {
        this.windowContext = ctx
        this.viewWidth = viewWidth
        this.func()
        this.show()
        return this
    }
}