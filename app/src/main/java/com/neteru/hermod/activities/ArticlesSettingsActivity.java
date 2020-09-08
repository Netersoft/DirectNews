package com.neteru.hermod.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.neteru.hermod.R;

import java.util.Locale;

public class ArticlesSettingsActivity extends AppCompatActivity {
    SharedPreferences preferences;
    String domains, from, to, language, sortBy;
    EditText domainsEdit, fromEdit, toEdit;
    RadioGroup languageRg, sortByRg;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_settings);

        save = findViewById(R.id.articles_settings_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("domains", domainsEdit.getText().toString());
                editor.putString("from", fromEdit.getText().toString());
                editor.putString("to", toEdit.getText().toString());

                switch (languageRg.getCheckedRadioButtonId()){
                    case R.id.fr:
                        editor.putString("language", "fr");
                        break;
                    case R.id.en:
                        editor.putString("language", "en");
                        break;
                    case R.id.pt:
                        editor.putString("language", "pt");
                        break;
                    case R.id.es:
                        editor.putString("language", "es");
                        break;
                    case R.id.de:
                        editor.putString("language", "de");
                        break;
                    default:
                        editor.putString("language", "fr");
                }

                switch (sortByRg.getCheckedRadioButtonId()){
                    case R.id.relevancy:
                        editor.putString("sortBy", "relevancy");
                        break;
                    case R.id.popularity:
                        editor.putString("sortBy", "popularity");
                        break;
                    case R.id.publishedAt:
                        editor.putString("sortBy", "publishedAt");
                        break;
                    default:
                        editor.putString("sortBy", "relevancy");
                }

                editor.apply();

                Toast.makeText(ArticlesSettingsActivity.this, R.string.saved_str, Toast.LENGTH_SHORT).show();
            }
        });

        domainsEdit = findViewById(R.id.domains);
        fromEdit = findViewById(R.id.from);
        toEdit = findViewById(R.id.to);

        languageRg = findViewById(R.id.language);
        sortByRg = findViewById(R.id.sortBy);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        domains = preferences.getString("domains", "");
        from = preferences.getString("from", "");
        to = preferences.getString("to", "");
        language = preferences.getString("language", Locale.getDefault().getLanguage().equals("fr") ? "fr" : "en");
        sortBy = preferences.getString("sortBy", "relevancy");

        initialize();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.search_settings_str);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void initialize(){
        domainsEdit.setText(domains);
        fromEdit.setText(from);
        toEdit.setText(to);

        switch (language){
            case "fr":
                languageRg.check(R.id.fr);
                break;
            case "en":
                languageRg.check(R.id.en);
                break;
            case "pt":
                languageRg.check(R.id.pt);
                break;
            case "es":
                languageRg.check(R.id.es);
                break;
            case "de":
                languageRg.check(R.id.de);
                break;
            default:
                languageRg.check(R.id.fr);
        }

        switch (sortBy){
            case "relevancy":
                sortByRg.check(R.id.relevancy);
                break;
            case "popularity":
                sortByRg.check(R.id.popularity);
                break;
            case "publishedAt":
                sortByRg.check(R.id.publishedAt);
                break;
            default:
                sortByRg.check(R.id.relevancy);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
