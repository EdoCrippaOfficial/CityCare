package inc.elevati.imycity.main;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import inc.elevati.imycity.R;
import inc.elevati.imycity.firebase.FirebaseAuthHelper;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MainPresenter.class, FirebaseAuthHelper.class})
public class MainPresenterTest {

    @Mock
    private MainContracts.MainView view;

    private MainContracts.MainPresenter presenter;

    @Before
    public void setUp() {
        view = mock(MainActivity.class);
        presenter = new MainPresenter();
        presenter.attachView(view);
    }

    @Test
    public void menuItemClickedTest() {

        // Test with "menu_my" as argument (same behaviour for "menu_new", "menu_all", "menu_completed", "menu_starred"
        presenter.menuItemClicked(R.id.menu_my);
        verify(view).scrollToPage(MainContracts.PAGE_MY);

        // Mock static method called in code under test
        mockStatic(FirebaseAuthHelper.class);

        // Test with "menu_logout" as argument
        presenter.menuItemClicked(R.id.menu_logout);
        verify(view).startLoginActivity();
    }

    @Test
    public void pageScrolledTest() {

        // Test with "PAGE_NEW" as argument (same behaviour for "PAGE_ALL", "PAGE_MY", "PAGE_STARRED", "PAGE_COMPLETED"
        presenter.pageScrolled(MainContracts.PAGE_NEW);
        verify(view).setCheckedMenuItem(R.id.menu_new);
    }
}
