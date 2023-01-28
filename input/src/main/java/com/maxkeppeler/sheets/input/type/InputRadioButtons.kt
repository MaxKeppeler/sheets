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

package com.maxkeppeler.sheets.input.type

import android.os.Bundle

/** Listener which returns the new index. */
typealias RadioButtonsInputListener = (index: Int) -> Unit

/**
 * Input of the type RadioButton.
 */
class InputRadioButtons(key: String? = null, func: InputRadioButtons.() -> Unit) : Input(key) {

    init {
        func()
    }

    private var changeListener: RadioButtonsInputListener? = null
    private var resultListener: RadioButtonsInputListener? = null

    internal var radioButtonOptions: MutableList<String>? = null
        private set

    var value: Int? = null
        internal set(value) {
            value?.let { changeListener?.invoke(it) }
            field = value
        }

    /** Set the by default selected index. */
    fun selected(selectedIndex: Int) {
        this.value = selectedIndex
    }

    /** Set the options to be displays as RadioButtons within a group. */
    fun options(options: MutableList<String>) {
        this.radioButtonOptions = options
    }
    
    /** Set a listener which returns the new value when it changed. */
    fun changeListener(listener: RadioButtonsInputListener) {
        this.changeListener = listener
    }
    
    /** Set a listener which returns the final value when the user clicks the positive button. */
    fun resultListener(listener: RadioButtonsInputListener) {
        this.resultListener = listener
    }

    override fun invokeResultListener() =
        resultListener?.invoke(value ?: -1)

    override fun valid(): Boolean = value != -1 && value != null

    override fun putValue(bundle: Bundle, index: Int) {
        value?.let { bundle.putInt(getKeyOrIndex(index), it) }
    }
}