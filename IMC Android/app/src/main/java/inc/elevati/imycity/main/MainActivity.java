package inc.elevati.imycity.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import inc.elevati.imycity.R;

public class MainActivity extends AppCompatActivity {

    private final static int NUM_FRAGMENTS = 2;
    private ViewPager pager;
    private DrawerLayout menuDrawer;
    private NavigationView menuNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Action Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.menu_icon);
        }

        // Menu
        menuDrawer = findViewById(R.id.drawer_layout);
        menuNavigator = findViewById(R.id.nav_view);
        menuNavigator.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuNavigator.setCheckedItem(menuItem);
                menuDrawer.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.menu_all:
                        scrollToPage(MainContracts.MenuPages.PAGE_ALL);
                        break;
                    case R.id.menu_new:
                        scrollToPage(MainContracts.MenuPages.PAGE_NEW);
                        break;
                }
                return true;
            }
        });

        // View pager for fragments
        pager = findViewById(R.id.view_pager);
        pager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);
    }

    public void scrollToPage(final MainContracts.MenuPages page) {
        pager.setCurrentItem(page.position, true);
        // TODO: not working
        switch (page) {
            case PAGE_NEW:
                menuNavigator.setCheckedItem(R.id.menu_all);
                break;
            case PAGE_ALL:
                menuNavigator.setCheckedItem(R.id.menu_new);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // If menu_icon is open then we only close it
        if (menuDrawer.isDrawerOpen(GravityCompat.START)) {
            menuDrawer.closeDrawer(GravityCompat.START, true);
            return;
        }
        // If we're on main fragment the app closes, else returns to main fragment
        if (pager.getCurrentItem() == 0) super.onBackPressed();
        else scrollToPage(MainContracts.MenuPages.PAGE_ALL);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                menuDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class FragmentAdapter extends FragmentPagerAdapter {

        private FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return AllReportsFragment.newInstance();
                case 1: return NewReportFragment.newInstance();
                default: return AllReportsFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return NUM_FRAGMENTS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.menu_all);
                case 1:
                    return getString(R.string.menu_new);
                case 2:
                    return getString(R.string.menu_all);
            }
            return null;
        }
    }
}
