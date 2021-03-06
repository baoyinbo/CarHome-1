package com.lanou3g.carhome.recommend.tblobbyists;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lanou3g.carhome.R;
import com.lanou3g.carhome.baseclass.BaseFragment;
import com.lanou3g.carhome.networkrequest.GsonRequest;
import com.lanou3g.carhome.networkrequest.VolleySingleton;
import com.lanou3g.carhome.networkrequest.WebViewActivity;
import com.lanou3g.carhome.recommend.TabURLBean;

import java.util.List;

/**
 *
 */
public class TabLobbyistsFragment extends BaseFragment{

    private PullToRefreshListView plvlobbyists;
    private TabLobbyistsAdapter adapter;
    private int position;
    private String url;

    public static TabLobbyistsFragment newInstance(int position) {

        List<String> urls = TabURLBean.getUrls();

        Bundle args = new Bundle();
        args.putString("url", urls.get(position));
        args.putInt("position",position);
        TabLobbyistsFragment fragment = new TabLobbyistsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_lobbyists;
    }

    @Override
    protected void initView() {
        plvlobbyists = bindView(R.id.pLv_recommend_lobbyists);
    }

    @Override
    protected void initData() {
        Bundle args = getArguments();
        position = args.getInt("position");
        url = args.getString("url");
        plvlobbyists.setMode(PullToRefreshBase.Mode.BOTH);
        plvlobbyists.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                initSendInterent();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        adapter = new TabLobbyistsAdapter(context, position);
        plvlobbyists.setAdapter(adapter);
        initSendInterent();

        plvlobbyists.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                int urlId = 0;
                if (2 == position) {
                    TabLobbysitsBean tabLobbysitsBean = (TabLobbysitsBean) parent.getItemAtPosition(i);
                    urlId = tabLobbysitsBean.getResult().getList().get(i - 1).getId();
                } else if(4 == position) {

                } else {
                    TabNewsBean newsBean = (TabNewsBean) parent.getItemAtPosition(i);
                    urlId = newsBean.getResult().getNewslist().get(i - 1).getId();
                }
                String url = "http://cont.app.autohome.com.cn/autov4.2.5/content/News/newscontent-a2-pm1-v4.2.5-n"
                        + urlId + "-lz0-sp0-nt0-sa1-p0-c1-fs0-cw320.html";
                intent.putExtra("urlWv", url);
                intent.putExtra("isAccordingShare", true);
                getActivity().startActivity(intent);
            }
        });
    }

    private void initSendInterent() {

        if (2 == position ) {
            GsonRequest<TabLobbysitsBean> gsonRequest = new GsonRequest<TabLobbysitsBean>(url,
                    TabLobbysitsBean.class,
                    new Response.Listener<TabLobbysitsBean>() {
                        @Override
                        public void onResponse(TabLobbysitsBean response) {
                            adapter.setBean(response);

                            plvlobbyists.onRefreshComplete();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            VolleySingleton.getInstance().addRequest(gsonRequest);
        } else if (4 == position) {
            GsonRequest<TabLettersBean> gsonRequest = new GsonRequest<TabLettersBean>(url,
                    TabLettersBean.class,
                    new Response.Listener<TabLettersBean>() {
                        @Override
                        public void onResponse(TabLettersBean response) {
                            adapter.setLettersBean(response);

                            plvlobbyists.onRefreshComplete();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            VolleySingleton.getInstance().addRequest(gsonRequest);
        } else {
            GsonRequest<TabNewsBean> gsonRequest = new GsonRequest<TabNewsBean>(url,
                    TabNewsBean.class,
                    new Response.Listener<TabNewsBean>() {
                        @Override
                        public void onResponse(TabNewsBean response) {
                            adapter.setNewsBean(response);

                            plvlobbyists.onRefreshComplete();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            VolleySingleton.getInstance().addRequest(gsonRequest);
        }
    }
}
