/*
 * Copyright (C) 2012 The Android Open Source Project
 * modified
 * SPDX-License-Identifier: Apache-2.0 AND GPL-3.0-only
 */

package com.mboard.keyboard.latin.define

object ProductionFlags {
    // supporting hardware keyboard still has a bunch of issues:
    // crashes, e.g. LatinIME.isInputViewShown() returns true when there is no input view, thus crashing in onUpdateSelection
    // physical layout ignored, in some cases only for uppercase letters
    const val IS_HARDWARE_KEYBOARD_SUPPORTED = false

    /**
     * Include all suggestions from all dictionaries in
     * [com.mboard.keyboard.latin.SuggestedWords.mRawSuggestions].
     */
    const val INCLUDE_RAW_SUGGESTIONS = false
}
