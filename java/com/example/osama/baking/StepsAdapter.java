package com.example.osama.baking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by osama on 4/14/2018.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {
    protected static String[] mDescriptions;
    protected static String[] mShortDescriptions;
    protected static String[] mVideoUrl;
    Context mContext;
    private String[] mImages;
    private final StepsOnClickHandler mClickHandler;
    public StepsAdapter(Context context,StepsOnClickHandler handler){
        mClickHandler=handler;
    }
    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.steps_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        StepsViewHolder viewHolder = new StepsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, int position) {
       holder.textView.setText(mShortDescriptions[position]);
        if (mImages==null||TextUtils.isEmpty(mImages[position])) {

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
        if (null == mShortDescriptions) {
            return 0;
        }
        return mShortDescriptions.length;
    }
    public  void setSteps (String[][] array){
       String[] list = new String[array.length];
       String[] list1=new String[array.length];
       String[] list2 = new String[array.length];
       String[] list3= new String[array.length];

        for(int i = 0; i<array.length;i++){
           list[i]=array[i][0];
           list1[i]=array[i][1];
           list2[i]=array[i][2];
           list3[i]=array[i][3];
        }
        mVideoUrl=list2;
        mDescriptions=list1;
        mShortDescriptions=list;
        mImages=list3;
        notifyDataSetChanged();

    }
    public interface StepsOnClickHandler{
        void onListItemClck(int position,String videoUrl,String descripton,String shortDescription);
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.step_text)TextView textView;
        @BindView(R.id.step_image_view)ImageView imageView;
        public StepsViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.step_text);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            int shortDescriptionIndex= position+1;
            if(shortDescriptionIndex> mShortDescriptions.length-1){
                shortDescriptionIndex=position;
            }
            mClickHandler.onListItemClck(position,mVideoUrl[position],mDescriptions[position],mShortDescriptions[shortDescriptionIndex]);
        }
    }
}
