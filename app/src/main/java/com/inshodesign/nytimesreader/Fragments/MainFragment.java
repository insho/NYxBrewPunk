package com.inshodesign.nytimesreader.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.inshodesign.nytimesreader.API_Interfaces.FragmentInteractionListener;
import com.inshodesign.nytimesreader.R;

/**
 * Fragment with a list of the main NYTimes article categories (of which there is currently only one: "Popular NYTimes Articles").
 * Selecting an options takes the user to the {@link SubFragment}, with sub-criteria ("most shared", "most emailed" etc) to select.
 */
public class MainFragment extends ListFragment implements AdapterView.OnItemClickListener {
    FragmentInteractionListener mCallback;

@Override
public View onCreateView(LayoutInflater inflater,
                         ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_main, container, false);
}


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
            ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.mainListOptions, android.R.layout.simple_list_item_1);
            setListAdapter(adapter);
            getListView().setOnItemClickListener(this);

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        mCallback.onMainOptionSelected(position);
    }





}