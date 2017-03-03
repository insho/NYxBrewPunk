//package com.inshodesign.nytimesreader;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Captor;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.robolectric.Robolectric;
//import org.robolectric.RobolectricTestRunner;
//import org.robolectric.annotation.Config;
//import org.robolectric.util.ActivityController;
//
//import java.util.List;
//
//import retrofit2.Callback;
//
///**
// * Created by JClassic on 2/26/2017.
// */
//@Config(constants = BuildConfig.class, sdk = 21,
//        manifest = "app/src/main/AndroidManifest.xml")
//@RunWith(RobolectricTestRunner.class)
//public class NYTimesAPIUnitTest {
//
//
//    public class RetrofitCallTest {
//
//        private MainActivity mainActivity;
//
//        @Mock
//        private RetrofitApi mockRetrofitApiImpl;
//
//        @Captor
//        private ArgumentCaptor<Callback<NYTimesArticleWrapper>> callbackArgumentCaptor;
//
//        @Before
//        public void setUp() {
//            MockitoAnnotations.initMocks(this);
//
//            ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class);
//            mainActivity = controller.get();
//
//            // then we need to swap the retrofit api impl. with a mock one
//            // I usually store my retrofit api impl as a static singleton in class RestClient, hence:
//            RestClient.setApi(mockRetrofitApiImpl);
//
//            controller.create();
//        }
//
//        @Test
//        public void shouldFillAdapter() throws Exception {
//            Mockito.verify(mockRetrofitApiImpl)
//                    .getYourObject(callbackAgrumentCaptor.capture());
//
//            int objectsQuantity = 10;
//            List<YourObject> list = new ArrayList<YourObject>;
//            for(int i = 0; i < objectsQuantity; ++i) {
//                list.add(new YourObject());
//            }
//
//            callbackArgumentCaptor.getValue().success(list, null);
//
//            YourAdapter yourAdapter = mainActivity.getAdapter(); // obtain adapter
//            // simple test check if adapter has as many items as put into response
//            assertThat(yourAdapter.getItemCount(), equalTo(objectsQuantity));
//        }
//
//}