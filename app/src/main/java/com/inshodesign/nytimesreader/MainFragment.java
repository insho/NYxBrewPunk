package com.inshodesign.nytimesreader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

/**
 * Created by JClassic on 2/21/2017.
 */

public class MainFragment extends ListFragment implements AdapterView.OnItemClickListener {
    OnMainOptionSelectedListener mCallback;

    public interface OnMainOptionSelectedListener {
        void onMainOptionSelected(int position);
    }


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
            mCallback = (OnMainOptionSelectedListener) context;
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