// SPDX-License-Identifier: GPL-3.0-only
package com.mboard.keyboard.settings.preferences

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.mboard.keyboard.latin.R
import com.mboard.keyboard.latin.utils.DictionaryInfoUtils
import com.mboard.keyboard.latin.utils.Log
import com.mboard.keyboard.latin.utils.getActivity
import com.mboard.keyboard.latin.utils.prefs
import com.mboard.keyboard.settings.Setting
import com.mboard.keyboard.settings.SettingsActivity
import com.mboard.keyboard.settings.dialogs.InfoDialog
import androidx.core.content.edit

@Composable
fun SwitchPreference(
    setting: Setting,
    default: Boolean,
    allowCheckedChange: (Boolean) -> Boolean = { true },
    onCheckedChange: (Boolean) -> Unit = { }
) {
    SwitchPreference(
        name = setting.title,
        description = setting.description,
        key = setting.key,
        default = default,
        allowCheckedChange = allowCheckedChange,
        onCheckedChange = onCheckedChange
    )
}

@Composable
fun SwitchPreference(
    name: String,
    modifier: Modifier = Modifier,
    key: String,
    default: Boolean,
    description: String? = null,
    allowCheckedChange: (Boolean) -> Boolean = { true }, // true means ok, usually for showing some dialog
    onCheckedChange: (Boolean) -> Unit = { },
) {
    val ctx = LocalContext.current
    val prefs = ctx.prefs()
    val b = (ctx.getActivity() as? SettingsActivity)?.prefChanged?.collectAsState()
    if ((b?.value ?: 0) < 0)
        Log.v("irrelevant", "stupid way to trigger recomposition on preference change")
    var value = prefs.getBoolean(key, default)
    fun switched(newValue: Boolean) {
        if (!allowCheckedChange(newValue)) {
            value = !newValue
            return
        }
        value = newValue
        prefs.edit { putBoolean(key, newValue) }
        onCheckedChange(newValue)
    }
    Preference(
        name = name,
        onClick = { switched(!value) },
        modifier = modifier,
        description = description
    ) {
        Switch(
            checked = value,
            onCheckedChange = { switched(it) },
        )
    }
}

@Composable
fun SwitchPreferenceWithEmojiDictWarning(setting: Setting, default: Boolean) {
    val context = LocalContext.current
    var showWarningDialog by rememberSaveable { mutableStateOf(false) }
    val hasEmojiDict = DictionaryInfoUtils.getLocalesWithEmojiDicts(context).isNotEmpty()
    SwitchPreference(setting, default && hasEmojiDict) { showWarningDialog = it && !hasEmojiDict }
    if (showWarningDialog) {
        val message = stringResource(R.string.emoji_dictionary_required_local)
        InfoDialog(message, onDismissRequest = { showWarningDialog = false })
    }
}
