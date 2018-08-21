package com.example.osama.baking.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.osama.baking.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by osama on 4/13/2018.
 */
/*
class for adapt data to recipes recyclerview
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
private final  RecipeOnClickHandler mClickHandler;
private String[] mNames;
private String[] mImages;
private Context mContext;

public RecipeAdapter(Context context,RecipeOnClickHandler handler){
    mClickHandler=handler;
    mContext=context;
}
    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_cards_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        String name = mNames[position];
        holder.textView.setText(name);

        if (TextUtils.isEmpty(mImages[position])) {
            // option 1: cancel Picasso request and clear ImageView
            Picasso
                    .with(mContext)
                    .cancelRequest(holder.imageView);

            holder.imageView.setImageDrawable(null);

        }
        else {
            Picasso
                    .with(mContext)
                    .load(mImages[position])
                    .fit() // will explain later
                    .into(holder.imageView);
        }

    }


    @Override
    public int getItemCount() {

        if (null == mNames) {
            return 0;
        }
        return mNames.length;
    }
    public  void setNamesAndImages (String[][] array){
        String[] list = new String[array.length];
        String[] list1=new String[array.length];
        for(int i = 0; i<array.length;i++) {
            list[i] = array[i][0];
            list1[i] = array[i][1];
        }
        mNames=list;
        mImages=list1;
        notifyDataSetChanged();

    }

    public interface RecipeOnClickHandler{
        void onListItemClck(int position);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.recipe_card_text)TextView textView;
        @BindView(R.id.recipe_image_view) ImageView imageView;
        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mClickHandler.onListItemClck(position);
        }
    }
}
