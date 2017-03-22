package com.best.limingxing.best.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private static final String TAG = WeiXinFragment.class.getSimpleName();
    final static int ACTION_DOWNLOAD = 0;
    final static int ACTION_PULL_DOWN = 1;
    final static int ACTION_PULL_UP = 2;

    private final static String ROOT_URL = "http://gank.io/api/data/Android/10/";
    //    String url = "http://gank.io/api/data/Android/10/1";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_refreshHint)
    TextView tvRefreshHint;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;


    ArrayList<Person> mGankList;
    RecyclerViewAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    int PageId = 1;
    int mNewState;
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
        setListener();
        return view;
    }

    private void setListener() {
        setPullDownListener();
        setPullUpListener();

    }

    /*
    下拉刷新
    */
    private void setPullDownListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setEnabled(true);
                swipeRefreshLayout.setRefreshing(true);
                tvRefreshHint.setVisibility(View.VISIBLE);
                PageId = 1;
                downloadData(ACTION_PULL_DOWN,PageId);
            }
        });

    }

    /*
    上拉加载
    */
    private void setPullUpListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mNewState = newState;
                lastPosition = mLayoutManager.findLastVisibleItemPosition();
                if (lastPosition >= mAdapter.getItemCount() - 1 && newState == RecyclerView.SCROLL_STATE_IDLE
                        && mAdapter.isMore()) {
                    PageId++;
                    downloadData(ACTION_PULL_UP,PageId);
                }
                if (newState != RecyclerView.SCROLL_STATE_DRAGGING) {
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastPosition = mLayoutManager.findLastVisibleItemPosition();
            }
        });

    }

    private void initView(View view) {
        mGankList = new ArrayList<>();
        mAdapter = new RecyclerViewAdapter(view.getContext(), mGankList);
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        downloadData(ACTION_DOWNLOAD,1);
    }

    private void downloadData(final int actionDown , int pageId) {
        Log.d(TAG, "downloadData: " + actionDown + ", " + pageId);
        String url = ROOT_URL + pageId;
        Log.d(TAG, "downloadData: " + url);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                GankPerson gankPerson = JSONArray.parseJson(response);
                List<Person> list = gankPerson.getResults();
                mAdapter.setMore(list != null && list.size() > 0);
                if (!mAdapter.isMore()) {
                    if (actionDown == ACTION_PULL_UP) {
                        mAdapter.setFooter("没有更多数据");
                    }
                    return;
                }
                switch (actionDown) {
                    case ACTION_DOWNLOAD:
                        mAdapter.initData(list);
                        mAdapter.setFooter("加载更多数据");
                        break;
                    case ACTION_PULL_DOWN:
                        mAdapter.initData(list);
                        mAdapter.setFooter("加载更多数据");
                        swipeRefreshLayout.setRefreshing(false);
                        tvRefreshHint.setVisibility(View.GONE);
                        break;
                    case ACTION_PULL_UP:
                        mAdapter.addData(list);
                        break;
                }
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
        String footerText;
        boolean isMore;

        public RecyclerViewAdapter(Context context, ArrayList<Person> GankList) {
            this.context = context;
            this.GankList = GankList;
        }

        public boolean isMore() {
            return isMore;
        }

        public void setMore(boolean more) {
            isMore = more;
        }

        public void setFooter(String footerText) {
            this.footerText = footerText;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            RecyclerView.ViewHolder holder = null;
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = null;
            switch (i) {
                case TYPE_FOOTER:
                    layout = inflater.inflate(R.layout.item_footer, viewGroup, false);
                    holder = new FooterViewHolder(layout);
                    break;
                case TYPE_ITEM:
                    layout = inflater.inflate(R.layout.item_content, viewGroup, false);
                    holder = new MyViewHolder(layout);
                    break;
            }
            return holder;
        }

        //绑定数据
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
            if (i == getItemCount() - 1) {
                return;
            }
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            Person gankPerson = GankList.get(i);
            myViewHolder.textDesc.setText(gankPerson.getDesc());
            myViewHolder.textWho.setText(gankPerson.getWho());
            myViewHolder.textCreatedat.setText(gankPerson.getCreatedat());
            if (gankPerson.getimages().size() > 0) {
                Glide.with(context).load(gankPerson.getimages().get(0))
                        .placeholder(R.mipmap.ic_launcher).into(((MyViewHolder) holder).ivImage);
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

        public void addData(List<Person> list) {
            this.GankList.addAll(list);
            notifyDataSetChanged();
        }

    }
}
