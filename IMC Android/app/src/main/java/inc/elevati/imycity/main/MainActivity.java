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
import inc.elevati.imycity.main.all_report_fragment.AllReportsFragment;
import inc.elevati.imycity.main.new_report_fragment.NewReportFragment;

public class MainActivity extends AppCompatActivity {

    private final static int PAGE_ALL = 0;
    private final static int PAGE_NEW = 1;
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

    public void scrollToPage(int page) {
        pager.setCurrentItem(page, true);
    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                menuDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        final MenuItem sortButton = menu.findItem(R.id.bn_sort);

        // ImageView to be inflated to sortButton
        ImageView imageSort = (ImageView) getLayoutInflater().inflate(R.layout.button_sort, null);
        sortButton.setActionView(imageSort);

        // Page change listener to know if the button should be shown
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int i, float v, int i1) { }

            @Override
            public void onPageSelected(int i) {
                // The button should be shown only if we're in PAGE_ALL fragment
                if (i == PAGE_ALL) {
                    sortButton.setVisible(true);
                    TranslateAnimation animate = new TranslateAnimation(200, 0, 0, 0);
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    sortButton.getActionView().startAnimation(animate);
                    sortButton.getActionView().setEnabled(true);
                } else {
                    // Here sortButton is still visible, but it hides with the animation
                    TranslateAnimation animate = new TranslateAnimation(0, 300, 0, 0);
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    sortButton.getActionView().startAnimation(animate);
                    sortButton.getActionView().setEnabled(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) { }
        });

        // Hide the button if we're not in PAGE_ALL (happens when activity is restarted after orientation change)
        if (pager.getCurrentItem() != PAGE_ALL) sortButton.setVisible(false);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pager.clearOnPageChangeListeners();
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
