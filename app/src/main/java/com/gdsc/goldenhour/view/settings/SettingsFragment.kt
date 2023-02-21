package com.gdsc.goldenhour.view.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.gdsc.goldenhour.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}