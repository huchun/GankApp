package com.example.gankapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gankapp.R;
import com.example.gankapp.ui.activity.CitysActivity;
import com.example.gankapp.util.SkinManager;

import java.util.List;

/**
 * Created by chunchun.hu on 2018/3/22.
 */

public class RecycleCitysAdapter extends RecyclerView.Adapter<RecycleCitysAdapter.MyViewHolder> {

    private Context context;
    private List<String>  mDatas;
    private LayoutInflater layoutInflater;
    private int currentPosition = -1;
    private int currentSkinType;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public RecycleCitysAdapter(Context context, List<String> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        layoutInflater = LayoutInflater.from(context);
        currentSkinType = SkinManager.getCurrentSkinType(context);
    }

    public void updateDatas(List<String> citysList) {
          this.mDatas = citysList;
          notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = layoutInflater.inflate(R.layout.item_time, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
          holder.tvTime.setText(mDatas.get(position));

         if (onItemClickListener != null){
             holder.itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     currentPosition = position;
                     onItemClickListener.onItemClick(holder.itemView, position);
                     notifyDataSetChanged();
                 }
             });
         }

         if (currentSkinType == SkinManager.THEME_DAY){
             if (currentPosition == position){
                 holder.tvTime.setTextColor(context.getResources().getColor(R.color.main_color));
             }else{
                 holder.tvTime.setTextColor(context.getResources().getColor(R.color.black_text2_color));
             }
         }else{
             if (currentPosition == position){
                 holder.tvTime.setTextColor(context.getResources().getColor(R.color.gank_text1_color_night));
             }else{
                 holder.tvTime.setTextColor(context.getResources().getColor(R.color.gank_text3_color_night));
             }
         }
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public String getPositionValue(int position) {
        return mDatas.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}
