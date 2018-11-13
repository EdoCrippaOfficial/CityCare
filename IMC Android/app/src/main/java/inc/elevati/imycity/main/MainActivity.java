package inc.elevati.imycity.main;

import android.content.Context;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import inc.elevati.imycity.R;
import inc.elevati.imycity.main.all_report_fragment.AllReportsFragment;
import inc.elevati.imycity.main.new_report_fragment.NewReportFragment;

public class MainActivity extends AppCompatActivity {

    private final static int NUM_FRAGMENTS = 2;
    private ViewPager pager;
    private DrawerLayout menuDrawer;
    private NavigationView menuNavigator;

    ImageView iv_sort;
    MyPageListrner mpl = new MyPageListrner();
    MenuItem sort_menu;
    static int lastPage = 0;

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

    }

    @Override
    public void onBackPressed() {
        // If ic_menu is open then we only close it
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        sort_menu = menu.findItem(R.id.sort);
        // Do animation start
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        iv_sort= (ImageView)inflater.inflate(R.layout.iv_sort, null);
        iv_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "SELECT SORTING METHOD", Toast.LENGTH_LONG).show();
            }
        });
        sort_menu.setActionView(iv_sort);
        pager.addOnPageChangeListener(mpl);
        if (!(lastPage == 0)){
            sort_menu.setVisible(false);
            iv_sort.setEnabled(false);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        pager.removeOnPageChangeListener(mpl);
        super.onDestroy();
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

    private class MyPageListrner implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            if (i == 0){    //pagina 0: all --> visibile
                sort_menu.setVisible(true);
                TranslateAnimation animate = new TranslateAnimation(200,0,0,0);
                animate.setDuration(500);
                animate.setFillAfter(true);
                iv_sort.startAnimation(animate);
                iv_sort.setEnabled(true);
            }

            else{           //altre pagine  --> invisibile
                TranslateAnimation animate = new TranslateAnimation(0,300,0,0);
                animate.setDuration(500);
                animate.setFillAfter(true);
                iv_sort.startAnimation(animate);
                iv_sort.setEnabled(false);
            }
            lastPage = i;
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }
}
