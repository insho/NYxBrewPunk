
package com.inshodesign.nytimesreader;

        import android.content.Context;
        import android.os.Bundle;
        import android.support.v4.app.ListFragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;


public class SubFragment extends ListFragment implements AdapterView.OnItemClickListener {


    OnSubOptionSelectedListener mCallback;


    // Container Activity must implement this interface
    public interface OnSubOptionSelectedListener {
        void getArticles(String requesttype, String apikey);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.subListOptions_Popular, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
//        mCallback.onSubOptionSelected(position,"all-sections","1");

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
            mCallback = (OnSubOptionSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

//        Activity a;
//
//        if (context instanceof Activity){
//            a=(Activity) context;
//        }

    }
//    public interface OnNewsItemSelectedListener{
//        public void onNewsItemPicked(int position);
//    }
}