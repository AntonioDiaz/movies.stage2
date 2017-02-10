package com.adiaz.movies.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.adiaz.movies.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by toni on 09/02/2017.
 */

public class PreferencesUtilities {

	public static final boolean sortByPopularity(Context context) {
		boolean popularity = false;
		String sortPopularityValue = context.getString(R.string.pref_sort_value_popularity);
		String sortKey = context.getString(R.string.pref_sort_key);
		String sortDefault = context.getString(R.string.pref_sort_value_defaul);
		String sortField = PreferenceManager.getDefaultSharedPreferences(context).getString(sortKey, sortDefault);
		if (sortPopularityValue.equals(sortField)) {
			popularity = true;
		}
		return popularity;
	}

	public static final void shaveUpdateMoviesDate(Context context) {
		SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor edit = defaultSharedPreferences.edit();
		String lastUpdateDate = context.getString(R.string.pref_last_update_date);
		edit.putLong(lastUpdateDate, new Date().getTime());
		edit.commit();
	}

	public static String getLastUpdateString(Context context) {
		String lastUpdateKey = context.getString(R.string.pref_last_update_date);
		long dateLong = PreferenceManager.getDefaultSharedPreferences(context).getLong(lastUpdateKey, 0);
		Date lastUpdateDate = new Date(dateLong);
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		String dateStr = dateFormat.format(lastUpdateDate);
		return dateStr;

	}

	/** return true if refresh movies list is required. */
	public static boolean isNecesaryRefesh(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		String lastUpdateKey = context.getString(R.string.pref_last_update_date);
		long lastUpdateMiliseconds = sharedPreferences.getLong(lastUpdateKey, 0);
		String minutesRefreshKey = context.getString(R.string.pref_refresh_key);
		String minutesRefreshDefault = context.getString(R.string.pref_refresh_default);
		String minutesRefreshStr = sharedPreferences.getString(minutesRefreshKey, minutesRefreshDefault);
		long milisecondsRefresh = Long.parseLong(minutesRefreshStr) * MoviesConstants.MINUTE_IN_MILLIS;
		long actualMiliseconds = new Date().getTime();
		boolean refreshNecesary = false;
		if (lastUpdateMiliseconds + milisecondsRefresh < actualMiliseconds) {
			refreshNecesary = true;
		}
		return refreshNecesary;
	}
}
