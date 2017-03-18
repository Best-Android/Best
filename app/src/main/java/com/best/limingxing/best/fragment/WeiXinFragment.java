package com.best.limingxing.best.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.best.limingxing.best.R;
import com.best.limingxing.best.bean.GankPerson;
import com.best.limingxing.best.bean.JSONArray;
import com.best.limingxing.best.bean.Person;
import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeiXinFragment extends Fragment {
    String url = "http://gank.io/api/data/Android/10/1";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    ArrayList<Person> mGankList;
    RecyclerViewAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    Unbinder unbinder;

    public WeiXinFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wei_xin, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mGankList = new ArrayList<>();
        mAdapter = new RecyclerViewAdapter(view.getContext(),mGankList);
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                GankPerson gankPerson = JSONArray.parseJson(response);
                List<Person> list = gankPerson.getResults();
                mAdapter.initData(list);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //ViewHolder类
     class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView textDesc;
        TextView textWho;
        TextView textCreatedat;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            textDesc = (TextView) itemView.findViewById(R.id.text_desc);
            textWho = (TextView) itemView.findViewById(R.id.text_who);
            textCreatedat = (TextView) itemView.findViewById(R.id.text_createdat);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView tvFooter;

        public FooterViewHolder(View itemView) {
            super(itemView);
            tvFooter = (TextView) itemView.findViewById(R.id.tvFooter);
        }
    }

     class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
         final static int TYPE_ITEM = 0;//代表图片item类型的布局
         final static int TYPE_FOOTER = 1;//代表页脚item类型的布局

         Context context;
         ArrayList<Person> GankList;

         public RecyclerViewAdapter(Context context, ArrayList<Person> GankList) {
             this.context = context;
             this.GankList = GankList;
         }


         @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
             RecyclerView.ViewHolder holder = null;
             LayoutInflater inflater = LayoutInflater.from(context);
             View layout = null;
             switch (i){
                 case TYPE_FOOTER:
                     layout = inflater.inflate(R.layout.item_footer,viewGroup,false);
                     holder = new FooterViewHolder(layout);
                     break;
                 case TYPE_ITEM:
                     layout = inflater.inflate(R.layout.item_content,viewGroup,false);
                     holder = new MyViewHolder(layout);
                     break;
             }
             return  holder;
        }

        //绑定数据
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
            if (i == getItemCount() - 1){
                return;
            }
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            Person gankPerson = GankList.get(i);
            myViewHolder.textDesc.setText(gankPerson.getDesc());
            myViewHolder.textWho.setText(gankPerson.getWho());
            myViewHolder.textCreatedat.setText(gankPerson.getCreatedat());
            if (gankPerson.getimages().size() > 0){
                Glide.with(context).load(gankPerson.getimages().get(0))
                        .placeholder(R.mipmap.ic_launcher).into(((MyViewHolder)holder).ivImage);
            }
        }

        @Override
        public int getItemCount() {
            return GankList == null ? 0 : GankList.size() + 1;
        }

         @Override
         public int getItemViewType(int position) {
             if (position == getItemCount() - 1) {
                 return TYPE_FOOTER;
             } else {
                 return TYPE_ITEM;
             }
         }

         public void initData(List<Person> list) {
             if (GankList != null) {
                 GankList.clear();
             }
             GankList.addAll(list);
             notifyDataSetChanged();
         }

    }
}
