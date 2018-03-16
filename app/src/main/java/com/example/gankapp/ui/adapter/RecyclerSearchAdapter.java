package com.example.gankapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gankapp.R;
import com.example.gankapp.ui.bean.SearchBean;

import java.util.List;

/**
 * Created by chunchun.hu on 2018/3/16.
 */

public class RecyclerSearchAdapter extends RecyclerView.Adapter<RecyclerSearchAdapter.MyViewHolder> {

   private Context mContext;
   private List<SearchBean>  searchBeanList;
   private LayoutInflater mLayoutInflater;
   private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public RecyclerSearchAdapter(Context context, List<SearchBean> mDatas) {
        this.mContext = context;
        this.searchBeanList = mDatas;
        mLayoutInflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflater = mLayoutInflater.inflate(R.layout.item_search, parent, false);
        return new MyViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        SearchBean searchBean = searchBeanList.get(position);
        holder.tvDesc.setText(searchBean.getDesc());
        holder.tvType.setText(searchBean.getType());
        holder.tvTime.setText(searchBean.getPublishedAt().split("T")[0]);

        // Android | iOS | 休息视频 | 福利 | 拓展资源 | 前端 | 瞎推荐 | App
        String type = searchBean.getType();
        if ("Android".equals(type)){
            holder.tvType.setBackgroundColor(mContext.getResources().getColor(R.color.type_01));
        }else if (TextUtils.equals(type, mContext.getResources().getString(R.string.action_ios))){
            holder.tvType.setBackgroundColor(mContext.getResources().getColor(R.color.type_02));
        }else if (mContext.getResources().getString(R.string.action_xiuxi).equals(type)){
            holder.tvType.setBackgroundColor(mContext.getResources().getColor(R.color.type_03));
        }else if (mContext.getResources().getString(R.string.action_fuli).equals(type)){
            holder.tvType.setBackgroundColor(mContext.getResources().getColor(R.color.type_04));
        }else if ("拓展资源".equals(type)){
            holder.tvType.setBackgroundColor(mContext.getResources().getColor(R.color.type_05));
        }else if (TextUtils.equals(type, mContext.getResources().getString(R.string.action_qianduan))){
            holder.tvType.setBackgroundColor(mContext.getResources().getColor(R.color.type_06));
        }else if (TextUtils.equals(type, mContext.getResources().getString(R.string.action_tuijian))){
            holder.tvType.setBackgroundColor(mContext.getResources().getColor(R.color.type_07));
        }else if (mContext.getResources().getString(R.string.action_app).equals(type)){
            holder.tvType.setBackgroundColor(mContext.getResources().getColor(R.color.type_08));
        }

            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
            }
    }

    @Override
    public int getItemCount() {
        return searchBeanList != null ? searchBeanList.size() : 0;
    }

    public void setNewDatas(List<SearchBean> mDatas) {
             this.searchBeanList = mDatas;
             notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDesc, tvType, tvTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            tvType = itemView.findViewById(R.id.tv_type);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
