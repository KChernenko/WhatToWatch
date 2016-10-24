package me.bitfrom.whattowatch.ui.settings;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.ui.base.BasePreferenceFragment;
import me.bitfrom.whattowatch.ui.main.MainActivity;

public class SettingsFragment extends BasePreferenceFragment implements SettingsView, Preference.OnPreferenceChangeListener {

    @Inject
    protected SettingsPresenter presenter;

    private SwitchPreference mainSwitch;
    private SwitchPreference vibSwitch;
    private SwitchPreference ledSwitch;
    private SwitchPreference soundSwitch;

    private String newValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);

        getFragmentComponent((MainActivity) getActivity()).inject(this);

        mainSwitch = (SwitchPreference) findPreference(getString(R.string.pref_enable_notifications_key));
        vibSwitch = (SwitchPreference) findPreference(getString(R.string.pref_enable_vibration_key));
        ledSwitch = (SwitchPreference) findPreference(getString(R.string.pref_enable_led_key));
        soundSwitch = (SwitchPreference) findPreference(getString(R.string.pref_enable_sound_key));

        vibSwitch.setEnabled(mainSwitch.isChecked());
        ledSwitch.setEnabled(mainSwitch.isChecked());
        soundSwitch.setEnabled(mainSwitch.isChecked());

        mainSwitch.setOnPreferenceClickListener(preference -> {
            vibSwitch.setEnabled(mainSwitch.isChecked());
            ledSwitch.setEnabled(mainSwitch.isChecked());
            soundSwitch.setEnabled(mainSwitch.isChecked());
            return true;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attachView(this);

        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_number_of_films_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_frequency_of_updates_key)));
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) presenter.detachView();
    }

    @Override
    public boolean onPreferenceChange(@NonNull Preference preference, @NonNull Object newValue) {
        String stringValue = newValue.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }

            if (preference.getKey().equals(getString(R.string.pref_frequency_of_updates_key))) {
                presenter.rescheduleSync(stringValue);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }
        return true;
    }

    @Override
    public void loadNewValue(@NonNull String newValue) {
        this.newValue = newValue;
    }

    private void bindPreferenceSummaryToValue(@NonNull Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        presenter.getNewValue(preference.getKey());
        // Trigger the listener immediately with the preference's current value.
        onPreferenceChange(preference, newValue);
    }
}