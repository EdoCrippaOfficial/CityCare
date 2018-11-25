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
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import inc.elevati.imycity.R;
import inc.elevati.imycity.main.allreports.AllReportsFragment;
import inc.elevati.imycity.main.newreport.NewReportFragment;

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

        // Listener to redraw the menu bar when page is changed and update checked item in left menu
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) { }
            @Override
            public void onPageSelected(int i) {
                invalidateOptionsMenu();
                updateCheckedMenuItem(i);
            }
            @Override
            public void onPageScrollStateChanged(int i) {}
        });

        // Update current selected menu item
        updateCheckedMenuItem(pager.getCurrentItem());

        // Tab layout showing pages below menu bar
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);
    }

    /**
     * Method called to update the checked item in left menu
     * @param page the page currently shown
     */
    private void updateCheckedMenuItem(int page) {
        switch (page) {
            case PAGE_ALL:
                menuNavigator.setCheckedItem(R.id.menu_all);
                break;
            case PAGE_NEW:
                menuNavigator.setCheckedItem(R.id.menu_new);
                break;
        }
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

    /** Called when activity is destroyed, all onPageChangeListeners are removed from ViewPager*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        pager.clearOnPageChangeListeners();
    }

    /** Method called when menu bar is created, here we can inflate and animate menu buttons */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);

        // Set image on sort button, the listener is set in equivalent Fragment method
        final MenuItem sortButton = menu.findItem(R.id.bn_sort);
        ImageView imageSort = (ImageView) getLayoutInflater().inflate(R.layout.button_sort, null);
        sortButton.setActionView(imageSort);

        // Animate in or out the button depending on which page is showing
        if (pager.getCurrentItem() == PAGE_ALL) {
            sortButton.setEnabled(true);
            TranslateAnimation animate = new TranslateAnimation(200, 0, 0, 0);
            animate.setDuration(500);
            animate.setFillAfter(true);
            sortButton.getActionView().startAnimation(animate);
        } else {
            sortButton.setEnabled(false);
            TranslateAnimation animate = new TranslateAnimation(0, 200, 0, 0);
            animate.setDuration(400);
            animate.setFillAfter(true);
            sortButton.getActionView().startAnimation(animate);
        }
        return false;
    }

    /** Adapter class used by ViewPager to show fragments in pages */
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
