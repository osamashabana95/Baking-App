package com.example.osama.baking.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.osama.baking.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by osama on 4/14/2018.
 */
//class to adapt data  to ingredients list

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {


    private String[] mQuantites;


    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.ingredients_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        IngredientsViewHolder viewHolder = new IngredientsViewHolder(view);
        return viewHolder;    }

        //bind data to each item
    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {
       holder.textView.setText(mQuantites[position]);
    }


    @Override
    public int getItemCount() {
        if (null == mQuantites) {
            return 0;
        }
        return mQuantites.length;
    }

    //to set data before start binding

    public  void setIngredients (String[] array){
        mQuantites=array;
        notifyDataSetChanged();

    }



    public class IngredientsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredient_text)TextView textView;
        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
