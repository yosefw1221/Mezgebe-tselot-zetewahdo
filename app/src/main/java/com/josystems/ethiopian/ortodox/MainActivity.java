package com.josystems.ethiopian.ortodox;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.josystems.ethiopian.ortodox.utils.Util;

public class MainActivity extends AppCompatActivity {
    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_maincopy);
        Util.showAgreementDialog(this);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        FloatingActionButton mainFab = findViewById(R.id.main_fab);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_store, R.id.navigation_home, R.id.navigation_notifications)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        mainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.navigation_home);
            }
        });
        try {
            int tab = getIntent().getIntExtra("nav",0);
            if (tab!=0)
                navController.navigate(tab);
        }catch (Exception e){
            FirebaseCrashlytics.getInstance().recordException(e);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nightmode_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

}
