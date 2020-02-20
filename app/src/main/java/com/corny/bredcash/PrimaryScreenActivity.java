package com.corny.bredcash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PrimaryScreenActivity extends AppCompatActivity
{
    CurrentUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_screen);
        currentUser = new CurrentUser();

        BottomNavigationView nav = (BottomNavigationView)findViewById(R.id.bottomNavigation);
        nav.setOnNavigationItemSelectedListener(navlistener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                new homeFragment()).commit();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener navlistener = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()) {

                        case R.id.bottomNav_home:
                            selectedFragment = new homeFragment();
                            break;

                        case R.id.bottomNav_Account:
                            selectedFragment = new accountFragment();
                            break;

                        case R.id.bottomNav_AddProduct:
                            selectedFragment = new addProductFragment();
                            break;

                        case R.id.bottomNav_YourAuctions:
                            selectedFragment = new yourAuctionsFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                            selectedFragment).commit();
                    return true;
                }
            };

}
