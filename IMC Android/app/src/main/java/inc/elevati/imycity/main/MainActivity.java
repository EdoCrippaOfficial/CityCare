package inc.elevati.imycity.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import inc.elevati.imycity.R;
import inc.elevati.imycity.login.LoginActivity;
import inc.elevati.imycity.main.allreports.AllReportsFragment;
import inc.elevati.imycity.main.completedreports.CompletedReportsFragment;
import inc.elevati.imycity.main.myreports.MyReportsFragment;
import inc.elevati.imycity.main.newreport.NewReportFragment;
import inc.elevati.imycity.main.starredreports.StarredReportsFragment;

import static inc.elevati.imycity.main.MainContracts.PAGE_ALL;
import static inc.elevati.imycity.main.MainContracts.PAGE_COMPLETED;
import static inc.elevati.imycity.main.MainContracts.PAGE_MY;
import static inc.elevati.imycity.main.MainContracts.PAGE_NEW;
import static inc.elevati.imycity.main.MainContracts.PAGE_STARRED;

/** The main activity that contains the ViewPager with the fragments */
public class MainActivity extends AppCompatActivity implements MainContracts.MainView {

    /** Total number of fragments */
    private final static int NUM_FRAGMENTS = 5;

    /** The presenter associated to this view */
    private MainPresenter presenter;

    /** The object that organizes fragments in pages */
    private ViewPager pager;

    /** Main menu drawer */
    private DrawerLayout menuDrawer;

    /** Main menu navigator */
    private NavigationView menuNavigator;

    /** The last page selected */
    private int previousPage;

    /**
     * Called when app starts and after orientation changes or activity re-creations,
     * here all the activity components are initialized and layout is shown
     * @param savedInstanceState a Bundle containing saved data to be restored
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Presenter creation
        presenter = (MainPresenter) getLastCustomNonConfigurationInstance();
        if (presenter == null) presenter = new MainPresenter();
        presenter.attachView(this);

        // Action Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        // Main menu
        menuDrawer = findViewById(R.id.drawer_layout);
        menuNavigator = findViewById(R.id.nav_view);
        menuNavigator.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuNavigator.setCheckedItem(menuItem);
                menuDrawer.closeDrawers();
                presenter.onMenuItemClicked(menuItem.getItemId());
                return true;
            }
        });

        // UserName and email in navigator header
        View headerView = menuNavigator.getHeaderView(0);
        TextView tvUser = headerView.findViewById(R.id.tv_username);
        TextView tvEmail = headerView.findViewById(R.id.tv_email);
        tvUser.setText(presenter.getCurrentUserName());
        tvEmail.setText(presenter.getCurrentUserEmail());

        // View pager for fragments
        pager = findViewById(R.id.view_pager);
        pager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));

        // Listener to redraw the menu bar when page is changed and update checked item in left menu
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) { }
            @Override
            public void onPageSelected(int i) {
                if (i == PAGE_NEW) invalidateOptionsMenu();
                presenter.onPageScrolled(i);
            }
            @Override
            public void onPageScrollStateChanged(int i) {}
        });

        // Set starting page
        pager.setCurrentItem(PAGE_ALL);
        presenter.onPageScrolled(PAGE_ALL);
        previousPage = PAGE_ALL;

        // Tab layout showing pages below menu bar
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);
    }

    /**
     * Called at orientation changes or activity re-creations, it retains the presenter
     * @return the presenter associated to the view
     */
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    /**
     * @return the presenter associated to this view
     */
    public MainContracts.MainPresenter getPresenter() {
        return presenter;
    }

    /** {@inheritDoc} */
    @Override
    public void setCheckedMenuItem(int itemId) {
        menuNavigator.setCheckedItem(itemId);
    }

    /** {@inheritDoc} */
    @Override
    public void scrollToPage(int page) {
        pager.setCurrentItem(page, true);
    }

    /**
     * Called when users click back button on device, if AllReportsFragment is shown
     * the app closes, otherwise AllReportsFragment is shown / menu is closed
     */
    @Override
    public void onBackPressed() {
        // If menu is open then we only close it
        if (menuDrawer.isDrawerOpen(GravityCompat.START)) {
            menuDrawer.closeDrawer(GravityCompat.START, true);
            return;
        }

        // If we're on main fragment the app closes, else returns to main fragment
        if (pager.getCurrentItem() == PAGE_ALL) super.onBackPressed();
        else scrollToPage(PAGE_ALL);
    }

    /** {@inheritDoc} */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                menuDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return true;
    }

    /** Called when activity is destroyed, all onPageChangeListeners are removed from ViewPager */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        pager.clearOnPageChangeListeners();
        presenter.detachView();
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
        if (pager.getCurrentItem() != PAGE_NEW && previousPage == PAGE_NEW) {
            sortButton.setEnabled(true);
            TranslateAnimation animate = new TranslateAnimation(200, 0, 0, 0);
            animate.setDuration(500);
            animate.setFillAfter(true);
            sortButton.getActionView().startAnimation(animate);
        } else if (pager.getCurrentItem() == PAGE_NEW && previousPage != PAGE_NEW){
            sortButton.setEnabled(false);
            TranslateAnimation animate = new TranslateAnimation(0, 200, 0, 0);
            animate.setDuration(500);
            animate.setFillAfter(true);
            sortButton.getActionView().startAnimation(animate);
        }
        previousPage = pager.getCurrentItem();
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /** Adapter class used by ViewPager to show fragments in pages */
    private class FragmentAdapter extends FragmentPagerAdapter {

        private FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case PAGE_NEW: return NewReportFragment.newInstance();
                case PAGE_ALL: return AllReportsFragment.newInstance();
                case PAGE_MY: return MyReportsFragment.newInstance();
                case PAGE_STARRED: return StarredReportsFragment.newInstance();
                case PAGE_COMPLETED: return CompletedReportsFragment.newInstance();
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
                case PAGE_NEW:
                    return getString(R.string.menu_new);
                case PAGE_ALL:
                    return getString(R.string.menu_all);
                case PAGE_MY:
                    return getString(R.string.menu_my);
                case PAGE_STARRED:
                    return getString(R.string.menu_starred);
                case PAGE_COMPLETED:
                    return getString(R.string.menu_completed);
            }
            return null;
        }
    }
}
