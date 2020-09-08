package com.neteru.hermod.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.navigation.NavigationView;
import com.neteru.hermod.R;
import com.neteru.hermod.classes.services.notificationsService;
import com.neteru.hermod.fragments.MainFragments.ArticlesSearchFragment;
import com.neteru.hermod.fragments.MainFragments.HeadlinesFragment;
import com.neteru.hermod.fragments.MainFragments.ReadlaterFragment;
import com.neteru.hermod.fragments.MainFragments.RubrikFragment;
import com.neteru.hermod.fragments.MainFragments.UneFragment;
import com.vorlonsoft.android.rate.AppRate;
import com.vorlonsoft.android.rate.OnClickButtonListener;
import com.vorlonsoft.android.rate.StoreType;
import com.vorlonsoft.android.rate.Time;

import java.util.concurrent.TimeUnit;

import static com.neteru.hermod.classes.AppUtilities.openWebview;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                hideKeyboard(MainActivity.this);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);


        if (!(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) && !preferences.getBoolean("showPermissionBox", false)){

            preferences.edit().putBoolean("showPermissionBox", true).apply();

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        }

        byDefault();

        setAppRateSystem();

        checkIntentFromNotification();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (!(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED))
                Toast.makeText(MainActivity.this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {

        if (itemId != R.id.nav_une){ byDefault(); return; }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        itemId = item.getItemId();

        switch (itemId){

            case R.id.nav_une:
                loadFragment(new UneFragment(), "general");
                navigationView.getMenu().getItem(0).setChecked(true);
                setTitle(getString(R.string.headline_str));
                break;

            case R.id.nav_world:
                loadFragment(new HeadlinesFragment(), "general");
                navigationView.getMenu().getItem(1).getSubMenu().getItem(0).setChecked(true);
                setTitle(getString(R.string.region_str));
                break;

            case R.id.nav_africa:
                loadFragment(new HeadlinesFragment(), "africa");
                navigationView.getMenu().getItem(1).getSubMenu().getItem(1).setChecked(true);
                setTitle(getString(R.string.region_str));
                break;

            case R.id.nav_europa:
                loadFragment(new HeadlinesFragment(), "europa");
                navigationView.getMenu().getItem(1).getSubMenu().getItem(2).setChecked(true);
                setTitle(getString(R.string.region_str));
                break;

            case R.id.nav_america:
                loadFragment(new HeadlinesFragment(), "america");
                navigationView.getMenu().getItem(1).getSubMenu().getItem(3).setChecked(true);
                setTitle(getString(R.string.region_str));
                break;

            case R.id.nav_asia:
                loadFragment(new HeadlinesFragment(), "asia");
                navigationView.getMenu().getItem(1).getSubMenu().getItem(4).setChecked(true);
                setTitle(getString(R.string.region_str));
                break;

            case R.id.nav_economie:
                loadFragment(new RubrikFragment(), "business");
                navigationView.getMenu().getItem(2).getSubMenu().getItem(0).setChecked(true);
                setTitle(getString(R.string.rubrik_str));
                break;

            case R.id.nav_sciences:
                loadFragment(new RubrikFragment(), "science");
                navigationView.getMenu().getItem(2).getSubMenu().getItem(1).setChecked(true);
                setTitle(getString(R.string.rubrik_str));
                break;

            case R.id.nav_health:
                loadFragment(new RubrikFragment(), "health");
                navigationView.getMenu().getItem(2).getSubMenu().getItem(2).setChecked(true);
                setTitle(getString(R.string.rubrik_str));
                break;

            case R.id.nav_techno:
                loadFragment(new RubrikFragment(), "technology");
                navigationView.getMenu().getItem(2).getSubMenu().getItem(3).setChecked(true);
                setTitle(getString(R.string.rubrik_str));
                break;

            case R.id.nav_culture:
                loadFragment(new RubrikFragment(), "entertainment");
                navigationView.getMenu().getItem(2).getSubMenu().getItem(4).setChecked(true);
                setTitle(getString(R.string.rubrik_str));
                break;

            case R.id.nav_sport:
                loadFragment(new RubrikFragment(), "sports");
                navigationView.getMenu().getItem(2).getSubMenu().getItem(5).setChecked(true);
                setTitle(getString(R.string.rubrik_str));
                break;

            case R.id.nav_search_articles:
                loadFragment(new ArticlesSearchFragment(), null);
                navigationView.getMenu().getItem(3).setChecked(true);
                setTitle(getString(R.string.articles_search_str));
                break;

            case R.id.nav_later:
                loadFragment(new ReadlaterFragment(), null);
                navigationView.getMenu().getItem(4).setChecked(true);
                setTitle(getString(R.string.read_later_str));
                break;

            case R.id.nav_evaluate:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                }else{

                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                }

                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" +getPackageName())));
                }
                break;

            case R.id.nav_settings:
                Intent i_1 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(i_1, 4320);
                break;

            case R.id.nav_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_msg)+"https://play.google.com/store/apps/details?id="+getPackageName());
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_app_str)));
                break;

            default:
                loadFragment(new UneFragment(), "general");
                navigationView.getMenu().getItem(0).setChecked(true);
                setTitle(getString(R.string.headline_str));
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    /**
     * Initialisation du système de notation
     */
    private void setAppRateSystem() {

        AppRate.with(this)
                .setStoreType(StoreType.GOOGLEPLAY)
                .setTimeToWait(Time.DAY, (short) 5) // default is 10 days, 0 means install millisecond, 10 means app is launched 10 or more time units later than installation
                .setLaunchTimes((byte) 13)          // default is 10, 3 means app is launched 3 or more times
                .setRemindTimeToWait(Time.DAY, (short) 1) // default is 1 day, 1 means app is launched 1 or more time units after neutral button clicked
                .setRemindLaunchesNumber((byte) 3)  // default is 0, 1 means app is launched 1 or more times after neutral button clicked
                .setSelectedAppLaunches((byte) 1)   // default is 1, 1 means each launch, 2 means every 2nd launch, 3 means every 3rd launch, etc
                .setShowLaterButton(true)           // default is true, true means to show the Neutral button ("Remind me later").
                .set365DayPeriodMaxNumberDialogLaunchTimes((short) 13) // default is unlimited, 3 means 3 or less occurrences of the display of the Rate Dialog within a 365-day period
                .setVersionCodeCheck(true)          // default is false, true means to re-enable the Rate Dialog if a new version of app with different version code is installed
                .setVersionNameCheck(true)          // default is false, true means to re-enable the Rate Dialog if a new version of app with different version name is installed
                .setDebug(false)                    // default is false, true is for development only, true ensures that the Rate Dialog will be shown each time the app is launched
                .setOnClickButtonListener(new OnClickButtonListener() {
                    @Override
                    public void onClickButton(byte which) {
                        Log.d(MainActivity.this.getLocalClassName(), Byte.toString(which));
                    }
                })
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);

    }

    public void loadFragment(Fragment f, String s){
        Bundle bundle = new Bundle();
        if (s != null){
            bundle.putString("rubrik", s);
            f.setArguments(bundle);
        }

        //Chargement du fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, f);
        fragmentTransaction.commit();

        invalidateOptionsMenu(); //Invalidation du menu option

        uncheckedAllItem(); //Retrait general de focus

    }

    public void byDefault(){
        itemId = R.id.nav_une;

        loadFragment(new UneFragment(), "general");
        navigationView.getMenu().getItem(0).setChecked(true);
        setTitle(getString(R.string.headline_str));
    }

    private void startNotificationsWorker(){

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(false)
                .setRequiresStorageNotLow(false)
                .build();

        PeriodicWorkRequest notificationWork = new PeriodicWorkRequest
                .Builder(notificationsService.class, 25, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).enqueue(notificationWork);
    }

    public void setTitle(String s){
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(s);
    }

    public void setItemId(int itemId){
        this.itemId = itemId;
    }

    public void uncheckedAllItem(){
        for(int i = 0;i < 5; i++ ){
            if (i == 0){

                navigationView.getMenu().getItem(i).setChecked(false);
                navigationView.getMenu().getItem(3).setChecked(false);
                navigationView.getMenu().getItem(4).setChecked(false);

            }else if (i == 1){

                for (int y = 0; y < 5; y++){
                    navigationView.getMenu().getItem(i).getSubMenu().getItem(y).setChecked(false);
                }

            }else if (i == 2){

                for (int y = 0; y < 6; y++){
                    navigationView.getMenu().getItem(i).getSubMenu().getItem(y).setChecked(false);
                }

            }
        }
    }

    private void checkIntentFromNotification(){
        if (getIntent() != null && getIntent().hasExtra("url")){

            if (getIntent().getStringExtra("url") == null) return;

            openWebview(this, getIntent().getStringExtra("url"));

        }else{
            startNotificationsWorker();
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1){ recreate(); }
    }
}
