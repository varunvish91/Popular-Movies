package com.udacity.spotifystreamer;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


public class SettingsActivity extends PreferenceActivity {

    SharedPreferences mPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addPreferencesFromResource(R.xml.pref_general);
        SwitchPreference switchPreference = (SwitchPreference) findPreference("settings_switch");
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isPopularitySortOn = ((Boolean) newValue).booleanValue();
                SharedPreferences.Editor editor = mPrefs.edit();
                if (isPopularitySortOn) {
                    editor.putString(getString(R.string.sort), getString(R.string.sort_popularity));
                } else {
                    editor.putString(getString(R.string.sort), getString(R.string.sort_ratings));
                }
                editor.commit();


                return true;
            }
        });

    }



}
