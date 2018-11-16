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
import inc.elevati.imycity.main.all_report_fragment.AllReportsFragment;
import inc.elevati.imycity.main.new_report_fragment.NewReportFragment;

/**
 * The main activity that contains all the fragments
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Constants used to identify the shown fragment
     */
    private final static int PAGE_ALL = 0;
    private final static int PAGE_NEW = 1;

    /**
     * Total number of fragments
     */
    private final static int NUM_FRAGMENTS = 2;

    /**
     * The object that organizes fragments in pages
     */
    private ViewPager pager;

    /**
     * Main menu drawer
     */
    private DrawerLayout menuDrawer;

    /**
     * Main menu navigator
     */
    private NavigationView menuNavigator;

    /**
     * Called when app starts and after orientation changes or activity re-creations,
     * here all the activity components are initialized and layout is shown
     * @param savedInstanceState a Bundle containing saved data to be restored
     */
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
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
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
                        scrollToPage(PAGE_ALL);
                        break;
                    case R.id.menu_new:
                        scrollToPage(PAGE_NEW);
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

    /**
     * Called when a menu item is clicked, changes the current visible fragment
     * @param page the fragment to switch to
     */
    private void scrollToPage(int page) {
        pager.setCurrentItem(page, true);
    }

    /**
     * Called when users click back button on device, if AllReportsFragment is shown
     * the app closes, otherwise AllReportsFragment is shown / menu is closed
     */
    @Override
    public void onBackPressed() {
        // If ic_menu is open then we only close it
        if (menuDrawer.isDrawerOpen(GravityCompat.START)) {
            menuDrawer.closeDrawer(GravityCompat.START, true);
            return;
        }
        // If we're on main fragment the app closes, else returns to main fragment
        if (pager.getCurrentItem() == PAGE_ALL) super.onBackPressed();
        else scrollToPage(PAGE_ALL);
    }

    /**
     * Called when a menu item is clicked
     * @param item the item clicked
     * @return true if the click has been processed, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                menuDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return true;
    }

    /**
     * Called when activity is destroyed, all onPageChangeListeners
     * are removed from ViewPager
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        pager.clearOnPageChangeListeners();
    }

    /**
     * Adapter class used by ViewPager to show fragments in pages
     */
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
