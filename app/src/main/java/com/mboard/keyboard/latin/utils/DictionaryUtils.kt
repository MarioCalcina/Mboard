// SPDX-License-Identifier: GPL-3.0-only

package com.mboard.keyboard.latin.utils

import android.content.Context
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.edit
import com.mboard.keyboard.compat.locale
import com.mboard.keyboard.latin.R
import com.mboard.keyboard.latin.common.LocaleUtils.constructLocale
import com.mboard.keyboard.latin.settings.Defaults
import com.mboard.keyboard.latin.settings.Settings
import com.mboard.keyboard.settings.dialogs.ConfirmationDialog
import java.io.File
import java.util.Locale

fun getDictionaryLocales(context: Context): MutableSet<Locale> {
    val locales = HashSet<Locale>()

    // get cached dictionaries: extracted or user-added dictionaries
    DictionaryInfoUtils.getCacheDirectories(context).forEach { directory ->
        if (!hasAnythingOtherThanExtractedMainDictionary(directory)) return@forEach
        val locale = DictionaryInfoUtils.getWordListIdFromFileName(directory.name).constructLocale()
        locales.add(locale)
    }
    // get assets dictionaries
    val assetsDictionaryList = DictionaryInfoUtils.getAssetsDictionaryList(context)
    if (assetsDictionaryList != null) {
        for (dictionary in assetsDictionaryList) {
            locales.add(DictionaryInfoUtils.extractLocaleFromAssetsDictionaryFile(dictionary))
        }
    }
    return locales
}

@Composable
fun MissingDictionaryDialog(onDismissRequest: () -> Unit, locale: Locale) {
    val prefs = LocalContext.current.prefs()
    if (prefs.getBoolean(Settings.PREF_DONT_SHOW_MISSING_DICTIONARY_DIALOG, Defaults.PREF_DONT_SHOW_MISSING_DICTIONARY_DIALOG)) {
        onDismissRequest()
        return
    }
    // Mboard: sin enlaces de descarga; solo se muestra la primera frase (todas las traducciones la separan con <br>)
    val message = stringResource(R.string.no_dictionary_message, "", locale.toString(), "").substringBefore("<br>")
    val annotatedString = message.htmlToAnnotated()

    ConfirmationDialog(
        onDismissRequest = onDismissRequest,
        cancelButtonText = stringResource(R.string.dialog_close),
        onConfirmed = { prefs.edit { putBoolean(Settings.PREF_DONT_SHOW_MISSING_DICTIONARY_DIALOG, true) } },
        confirmButtonText = stringResource(R.string.no_dictionary_dont_show_again_button),
        content = { Text(annotatedString) }
    )
}

fun cleanUnusedMainDicts(context: Context) {
    val dictionaryDir = File(DictionaryInfoUtils.getWordListCacheDirectory(context))
    val dirs = dictionaryDir.listFiles() ?: return
    val usedLocaleLanguageTags = hashSetOf<String>()
    SubtypeSettings.getEnabledSubtypes().forEach { subtype ->
        val locale = subtype.locale()
        usedLocaleLanguageTags.add(locale.toLanguageTag())
    }
    SubtypeSettings.getAdditionalSubtypes().forEach { subtype ->
        getSecondaryLocales(subtype.extraValue).forEach { usedLocaleLanguageTags.add(it.toLanguageTag()) }
    }
    for (dir in dirs) {
        if (!dir.isDirectory) continue
        if (dir.name in usedLocaleLanguageTags) continue
        if (hasAnythingOtherThanExtractedMainDictionary(dir))
            continue
        dir.deleteRecursively()
    }
}

private fun hasAnythingOtherThanExtractedMainDictionary(dir: File) =
    dir.listFiles()?.any { it.name != DictionaryInfoUtils.MAIN_DICT_FILE_NAME } != false
