package com.inshodesign.nytimesreader;

import android.app.Application;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.test.ApplicationTestCase;
import android.widget.ArrayAdapter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by JClassic on 2/26/2017.
 */


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk=21)
public class MainActivityUnitTests {

//    public MainActivityFragmentTest() {
//        super(Application.class);
//    }



    MainActivity mainActivity;
    MainFragment mainFragment;
    SubFragment subFragment;
    ArticleListFragment articleListFragment;
//    ArrayAdapter adapter;

//    MainActivityFragment mainActivityFragment;

    @Before
    public void setUp() {
        mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainFragment = new MainFragment();
        subFragment = new SubFragment();
        articleListFragment = new ArticleListFragment();
//        mainActivityFragment = new MainActivityFragment();
        startMainFragment(mainFragment);

//        adapter = ArrayAdapter.createFromResource(mainActivity,
//                R.array.mainListOptions, android.R.layout.simple_list_item_1);


    }

    private void startMainFragment( MainFragment fragment ) {
        FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, null );
        fragmentTransaction.commit();
    }

    private void startSubFragment( SubFragment fragment ) {
        FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, null );
        fragmentTransaction.commit();
    }


    @Test
    public void testMainActivity() {
        Assert.assertNotNull(mainActivity);
    }

//    public void testGetItem() {
//        assertEquals("John was expected.", mJohn.getName(),
//                ((Contact) mAdapter.getItem(0)).getName());
//    }

    @Test
    public void testMainFragmentAdapter() {
        assertEquals(mainFragment.getListAdapter().getCount(),1);
    }

    @Test
    public void testMainFragmentTitle() {
        assertEquals(mainActivity.getSupportActionBar().getTitle(),"NYxBrewPunk");
    }


//    @Test
//    public void testCowsCounter() {
//        assertThat(mainActivityFragment.calculateYoungCows(10)).isEqualTo(2);
//        assertThat(mainActivityFragment.calculateYoungCows(99)).isEqualTo(3);
//    }

//    @Test
//    public void testSwtichtoSubFragment() {
//
//        assertTrue(subFragment.isVisible());
//    }
@Test
public void testSubFragment() {
    Assert.assertNotNull(subFragment);
}

    @Test
    public void testSubFragmentAdapter() {
        startSubFragment(subFragment);
        assertEquals(subFragment.getListAdapter().getCount(),3);
    }

    @Test
    public void testSubFragmentTitle() {
        assertEquals(mainActivity.getSupportActionBar().getTitle(),"Article Categories");
    }



}