package m.v.nixoscompanionapp.ui.preferences

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import m.v.nixoscompanionapp.R

class PreferencesFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}