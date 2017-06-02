package com.inshodesign.nytimesreader.Fragments;

        import android.content.Context;
        import android.os.Bundle;
        import android.support.v4.app.ListFragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;

        import com.inshodesign.nytimesreader.API_Clients.NYTimesClient;
        import com.inshodesign.nytimesreader.API_Interfaces.FragmentInteractionListener;
        import com.inshodesign.nytimesreader.R;

/**
 * Fragment with a list of sub-criteria for an NYTimes article category ("most shared", "most emailed" etc). This fragment
 * appears after the article category is chosen in {@link MainFragment}. Selecting a sub-criteria makes
 * the API call ({@link NYTimesClient}) and takes user to
 * {@link ArticleListFragment}
 */
public class SubFragment extends ListFragment implements AdapterView.OnItemClickListener {


    private FragmentInteractionListener mCallback;



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.subListOptions_Popular, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

    }

    /*Makes callback to MainActivity getArticles method, pulling articles from NYTimes api and
      sending user to ArticleListFragment */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        switch (position) {
            case 0:
                mCallback.getArticles("mostemailed", getString(R.string.apikey_popular));
                break;
            case 1:
                mCallback.getArticles("mostshared", getString(R.string.apikey_popular));
                break;
            case 2:
                mCallback.getArticles("mostviewed", getString(R.string.apikey_popular));
                break;
            default:
                break;
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (FragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}