package com.inshodesign.nytimesreader.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inshodesign.nytimesreader.Fragments.ArticleListFragment;
import com.inshodesign.nytimesreader.Models.NYTimesArticle;
import com.inshodesign.nytimesreader.R;

import java.util.List;

/**
 * Adapter for displaying {@link NYTimesArticle} articles in the {@link ArticleListFragment}
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private RxBus _rxbus;
    private List<NYTimesArticle> mDataset;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTitle;
        public TextView txtDescription;

        public ViewHolder(View v) {
            super(v);
            txtTitle = (TextView) v.findViewById(R.id.title);
            txtDescription = (TextView) v.findViewById(R.id.description);
        }
    }

    public ArticleAdapter(List<NYTimesArticle> myDataset, RxBus rxBus) {
        mDataset = myDataset;
        _rxbus = rxBus;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_articlesrecycler_rowlayout, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.txtTitle.setText(mDataset.get(position).getTitle());
        holder.txtDescription.setText(mDataset.get(position).getByLine());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _rxbus.send(holder.getAdapterPosition());
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}