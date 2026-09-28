package com.mboard.keyboard.keyboard.internal

import com.mboard.keyboard.keyboard.KeyboardElement

enum class ShiftMode(@JvmField val element: KeyboardElement) {
    /** shift disabled */
    UNSHIFT(KeyboardElement.ALPHABET),
    /** shift was enabled by the user */
    MANUAL(KeyboardElement.ALPHABET_MANUAL_SHIFTED),
    /** shift was enabled automatically */
    AUTOMATIC(KeyboardElement.ALPHABET_AUTOMATIC_SHIFTED),
    /** shift locked */
    LOCKED(KeyboardElement.ALPHABET_SHIFT_LOCKED),
}
