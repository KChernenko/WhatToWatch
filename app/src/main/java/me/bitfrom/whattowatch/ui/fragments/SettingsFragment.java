package me.bitfrom.whattowatch.ui.fragments;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.WWApplication;
import me.bitfrom.whattowatch.sync.FilmsSyncAdapter;

/**
 * Created by Constantine on 05.10.2015.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

    private static final String LOG_TAG = SettingsFragment.class.getSimpleName();

    private ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void onResume() {
        super.onResume();
        initToolbar();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_number_of_movies_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_frequency_of_updates_key)));

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.fragment_background));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringValue = newValue.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }

        FilmsSyncAdapter.initializeSyncAdapter(WWApplication.getAppContext());

        return true;
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private void initToolbar() {
        if (actionBar != null) {
            actionBar.setTitle(R.string.pref_settings_screen);
        }
    }
}
