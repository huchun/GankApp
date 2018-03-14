package com.example.gankapp.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.gankapp.R;
import com.example.gankapp.ui.imagebrowser.MNImageBrowserActivity;

import java.util.ArrayList;

/**
 * Created by chunchun.hu on 2018/3/14.
 * 列表弹框Dialog
 */

public class ListFragmentDialog extends DialogFragment implements View.OnClickListener {

    private static Context mContext;
    private FragmentManager manager;
    private ArrayList<String> mDatas = new ArrayList<>();
    private ListDialogAdapter listDialogAdapter;

    private View view;
    private RelativeLayout rl_root = null;
    private RecyclerView recyclerView = null;
    private Button  btnCancle = null;

    private OnItemClickListener onItemClickListener;

    public static ListFragmentDialog newInstance(Context context) {
         mContext = context;
         ListFragmentDialog fragmentDialog = new ListFragmentDialog();
         return fragmentDialog;
    }

    public void showDialog(FragmentManager manager, ArrayList<String> mDatas, OnItemClickListener onItemClickListener) {
         this.onItemClickListener = onItemClickListener;
         this.mDatas = mDatas;
         this.manager = manager;
        //显示
        show(this.manager,"");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // gone title
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setWindowAnimations(R.style.animate_list_dialog);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.translate));

        view = inflater.inflate(R.layout.dialog_list, null);

        rl_root = view.findViewById(R.id.rl_root);
        recyclerView = view.findViewById(R.id.recyclerViewList);
        btnCancle = view.findViewById(R.id.btn_cancle);

        rl_root.setOnClickListener(this);
        btnCancle.setOnClickListener(this);

        initRecyclerView();
        initAdapter();

        return view;
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initAdapter() {
        if (listDialogAdapter == null){
            listDialogAdapter = new ListDialogAdapter(mDatas,mContext);
            recyclerView.setAdapter(listDialogAdapter);
            listDialogAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (onItemClickListener != null){
                        onItemClickListener.onItemClick(view,position);
                    }
                    dismiss();
                }
            });
        }else {
            listDialogAdapter.updateDatas(mDatas);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.5f;
        window.setAttributes(windowParams);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    /*public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }*/

    @Override
    public void onClick(View v) {
       if (v.getId() == R.id.btn_cancle){
           dismiss();
       }else if (v.getId() == R.id.rl_root){
           dismiss();
       }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public class ListDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private Context  mContext;
        private ArrayList<String> mDatas;
        private LayoutInflater  layoutInflater;
        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public ListDialogAdapter(ArrayList<String> datas, Context context) {
            this.mDatas = datas;
            this.mContext = context;
            layoutInflater = LayoutInflater.from(context);
        }

        public void updateDatas(ArrayList<String> datas) {
                 this.mDatas = datas;
                 notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflater = layoutInflater.inflate(R.layout.layout_list_dialog_item, parent,false);
            return new MyDialogViewHolder(inflater);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
             if (holder instanceof MyDialogViewHolder){
                 MyDialogViewHolder myDialogViewHolder = (MyDialogViewHolder) holder;
                 myDialogViewHolder.btn_item.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         if (onItemClickListener != null){
                             onItemClickListener.onItemClick(v,position);
                         }
                     }
                 });
                 myDialogViewHolder.btn_item.setText(mDatas.get(position));
             }
        }

        @Override
        public int getItemCount() {
            if (mDatas != null && mDatas.size() > 0) {
                return mDatas.size();
            }else {
                return 0;
            }
        }

       public class  MyDialogViewHolder extends RecyclerView.ViewHolder{

            public Button btn_item = null;

           public MyDialogViewHolder(View itemView) {
               super(itemView);
               btn_item = itemView.findViewById(R.id.btn_item);
           }
       }
    }
}
