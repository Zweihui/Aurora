package com.zwh.mvparms.eyepetizer;

import com.zwh.mvparms.eyepetizer.mvp.presenter.VideoPresenter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ExampleUnitTest {

    @Rule
    public DaggerRule daggerRule = new DaggerRule();
    @Mock
    VideoPresenter presenter;
    @Test
    public void addition_isCorrect() throws Exception {
        Mockito.verify(presenter).getIndexVideoList(33964, true,1);
    }

}