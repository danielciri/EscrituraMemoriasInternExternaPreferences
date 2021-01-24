package com.danielcirilo.preferenciasficheros;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class FragmentPreferences extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences,rootKey);
    }
}
