package com.neteru.hermod.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.neteru.hermod.R;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new mPreferenceFragmentCompat()).commit();
        setResult(0);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.settings_title_str);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class mPreferenceFragmentCompat extends PreferenceFragmentCompat
    {
        private Activity activity;
        private Context context;
        private String lang;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.app_preferences, rootKey);

            activity = (Activity) context;

            ListPreference langPreference = findPreference("lang");
            EditTextPreference nbPreference = findPreference("nb");

            if (langPreference != null) {
                switch (lang) {
                    case "fr":
                        langPreference.setValueIndex(0);
                        break;

                    case "us":
                        langPreference.setValueIndex(1);
                        break;

                    case "de":
                        langPreference.setValueIndex(2);
                        break;

                    case "es":
                        langPreference.setValueIndex(3);
                        break;

                    case "pt":
                        langPreference.setValueIndex(4);
                        break;
                }

                langPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object o) {

                        if (preference.getKey().equals("lang")) {
                            activity.setResult(1);
                        }
                        return true;
                    }
                });
            }

            if (nbPreference != null) {
                nbPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object o) {

                        if (preference.getKey().equals("nb")) {
                            activity.setResult(1);
                        }
                        return true;
                    }
                });
            }
        }

        @Override
        public void onAttach(@NonNull Context ctx) {
            super.onAttach(ctx);

            context = ctx;
            lang = PreferenceManager.getDefaultSharedPreferences(context).getString("lang", Locale.getDefault().getLanguage().equals("fr") ? "fr" : "us");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
