package com.adiaz.movies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.adiaz.movies.utilities.PreferencesUtilities;

import static android.content.ContentValues.TAG;

/** Created by toni on 06/02/2017. */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
		addPreferencesFromResource(R.xml.movies_preferences);
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
		int count = getPreferenceScreen().getPreferenceCount();
		for (int i = 0; i < count; i++) {
			Preference preference = getPreferenceScreen().getPreference(i);
			if (!(preference instanceof CheckBoxPreference)) {
				String value = sharedPreferences.getString(preference.getKey(), "");
				Log.d(TAG, "onCreatePreferences: " + preference.getKey());
				Log.d(TAG, "onCreatePreferences: " + value);
				setPreferenceSummary(preference, value);
			}
		}
	}

	@Override
	public void onDestroy() {
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		super.onDestroy();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Preference preference = findPreference(key);
		if (preference!=null && !(preference instanceof CheckBoxPreference)) {
			String value = sharedPreferences.getString(preference.getKey(), "");
			setPreferenceSummary(preference, value);
		}
	}

	private void setPreferenceSummary(Preference preference, String value) {
		if (preference instanceof ListPreference) {
			ListPreference listPreference = (ListPreference)preference;
			Log.d(TAG, "setPreferenceSummary: " + listPreference);
			Log.d(TAG, "setPreferenceSummary: " + value);
			int indexOfValue = listPreference.findIndexOfValue(value);
			if (indexOfValue>=0) {
				listPreference.setSummary(listPreference.getEntries()[indexOfValue]);
			}
			if (getString(R.string.pref_refresh_key).equals(preference.getKey())) {
				String strDate = PreferencesUtilities.getLastUpdateString(getContext());
				String newSummary = listPreference.getSummary() + " (last update: "+ strDate +")";
				listPreference.setSummary(newSummary);
			}
		}
	}
}
