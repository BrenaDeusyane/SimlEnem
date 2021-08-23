package com.example.simulenem;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottom_navigation_view;
    private FrameLayout main_frame;
    private TextView drawer_profile_name, drawer_profile_text;

    private final BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            (MenuItem menuItem) -> {

                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        bottom_navigation_view.setSelectedItemId(R.id.nav_home);
                        return true;

                    case R.id.nav_leaderboard:
                        bottom_navigation_view.setSelectedItemId(R.id.nav_leaderboard);
                        return true;

                    case R.id.nav_account:
                        bottom_navigation_view.setSelectedItemId(R.id.nav_account);
                        return true;
                }
                return false;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        main_frame = findViewById(R.id.main_frame);

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer_profile_name = navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_name);
        drawer_profile_text = navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_text_img);

        String name = DbQuery.my_profile.getName();
        drawer_profile_name.setText(name);

        drawer_profile_text.setText(name.toUpperCase().substring(0,1));

        setFragment(new CategoryFragment());
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            setFragment(new CategoryFragment());

        } else if (id == R.id.nav_account){
            setFragment(new AccountFragment());

        } else if (id == R.id.nav_leaderboard){
            setFragment(new LeaderBoardFragment());

        } else if (id == R.id.nav_share){

        }else if (id == R.id.nav_send){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(main_frame.getId(), fragment);
        transaction.commit();
    }
}