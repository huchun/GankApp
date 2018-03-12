package com.example.gankapp.ui.adapter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.DebugUtils;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.gankapp.R;
import com.example.gankapp.ui.bean.GankEntity;
import com.example.gankapp.ui.bean.PicSizeEntity;
import com.example.gankapp.util.DensityUtil;
import com.ldoublem.thumbUplib.ThumbUpView;
import com.maning.library.SwitcherView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chunchun.hu on 2018/3/12.
 */

public class RecyclePicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<GankEntity> commonDataResults;
    private List<GankEntity> headLines;
    private ArrayList<String> headLinesStrs;
    private LayoutInflater  layoutInflater;
    private int screenWidth;
    private RequestOptions options;
    private MyViewHolderHeader myViewHolderHeader;

    private ArrayMap<String, PicSizeEntity> picSizeEntityArrayMap = new ArrayMap<>();

    public OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public RecyclePicAdapter(Context context, List<GankEntity> commonDataResults) {
          this.context = context;
          this.commonDataResults = commonDataResults;
          layoutInflater = LayoutInflater.from(context);
          screenWidth = DensityUtil.getWidth(context);
          options = new RequestOptions();
          options.fitCenter();
          options.placeholder(R.mipmap.pic_gray_bg);
          options.diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    private void updateHeadLineds() {

    }

    public void updateHeadLindes(List<GankEntity> headLists) {
          this.headLines = headLists;
          updateHeadLineds();
          notifyDataSetChanged();
    }

    public void updateDatas(List<GankEntity> welFareList) {

    }

    public void destroyList() {

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (0 == viewType){
            View inflate = layoutInflater.inflate(R.layout.item_welfare_header, parent, false);
            myViewHolderHeader = new MyViewHolderHeader(inflate);
            return myViewHolderHeader;
        }else{
            View inflate = layoutInflater.inflate(R.layout.item_welfare_staggered, parent, false);
            return new MyViewHolder(inflate);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolderHeader){
            final MyViewHolderHeader viewHolderHeader = (MyViewHolderHeader) holder;
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolderHeader.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
            if (headLines != null && headLines.size() > 0){
                viewHolderHeader.tvLoadingHeadLine.setVisibility(View.GONE);
                viewHolderHeader.switcherView.setVisibility(View.VISIBLE);
                viewHolderHeader.switcherView.setResource(headLinesStrs); //设置数据源
                viewHolderHeader.switcherView.startRolling(); //开始滚动
                viewHolderHeader.switcherView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = viewHolderHeader.switcherView.getCurrentIndex();
                        GankEntity randomGankEntity = headLines.get(index);

                    }
                });
            }else{
                viewHolderHeader.tvLoadingHeadLine.setVisibility(View.VISIBLE);
                viewHolderHeader.switcherView.setVisibility(View.GONE);
            }
        }else if (holder instanceof MyViewHolder){

        }
    }

    @Override
    public int getItemCount() {
        if (commonDataResults != null){
            return commonDataResults.size() + 1;
        }else {
            return 0;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout rl_root;
        ImageView  imageView;
        TextView   tvShowTime;
        ThumbUpView btnCollect2;

        public MyViewHolder(View itemView) {
            super(itemView);
            rl_root = itemView.findViewById(R.id.rl_root);
            imageView = itemView.findViewById(R.id.image);
            tvShowTime = itemView.findViewById(R.id.tvShowTime);
            btnCollect2 = itemView.findViewById(R.id.btn_collect2);
        }
    }

    public static class MyViewHolderHeader extends RecyclerView.ViewHolder{

        RelativeLayout rl_root;
        TextView       tvLoadingHeadLine;
        SwitcherView   switcherView;

        public MyViewHolderHeader(View itemView) {
            super(itemView);
            rl_root = itemView.findViewById(R.id.rl_root);
            tvLoadingHeadLine = itemView.findViewById(R.id.tv_loading_headline);
            switcherView = itemView.findViewById(R.id.switcherView);
        }
    }


    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
}
