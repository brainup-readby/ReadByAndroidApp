/*
 * Copyright 2017 Arthur Ivanets, arthur.ivanets.l@gmail.com
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

@file:JvmName("ProgressBarExtensions")

package com.brainup.readbyapp.com.brainup.readbyapp.utils.extensions

import android.widget.ProgressBar
import androidx.annotation.ColorInt
import com.brainup.readbyapp.com.brainup.readbyapp.utils.extensions.applyColor


fun ProgressBar.setColor(@ColorInt color : Int) {
    this.indeterminateDrawable = this.indeterminateDrawable?.applyColor(color)
}