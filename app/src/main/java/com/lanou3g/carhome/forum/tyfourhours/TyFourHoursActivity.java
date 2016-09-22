package com.lanou3g.carhome.forum.tyfourhours;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lanou3g.carhome.R;
import com.lanou3g.carhome.baseclass.BaseActivity;
import com.lanou3g.carhome.networkrequest.GsonRequest;
import com.lanou3g.carhome.networkrequest.URLValues;
import com.lanou3g.carhome.networkrequest.VolleySingleton;

/**
 *
 */
public class TyFourHoursActivity extends BaseActivity{

    private PullToRefreshListView plvTyHours;
    private TextView tvTitle;
    private ImageButton ibtnBack;
    private TyFourHoursAdapter adapter;

    @Override
    protected int setLayout() {
        return R.layout.activity_tyfour_hours;
    }

    @Override
    protected void initView() {
        plvTyHours = bindView(R.id.pLv_tyfour_hours);
        tvTitle = bindView(R.id.tv_title_custom_title_tyfour_hours);
        ibtnBack = bindView(R.id.ibtn_back_custom_title_tyfour_hours);

        View headView = LayoutInflater.from(this).inflate(R.layout.headview_tyfour_hours, null);

        ListView listView = plvTyHours.getRefreshableView();
        listView.addHeaderView(headView);
    }

    @Override
    protected void initData() {

        plvTyHours.setMode(PullToRefreshBase.Mode.BOTH);
        plvTyHours.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                tfHoursSendInterent();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        adapter = new TyFourHoursAdapter(this);
        plvTyHours.setAdapter(adapter);

        tfHoursSendInterent();
        onClickBack();
    }

    private void tfHoursSendInterent() {
        GsonRequest<TyFourHoursBean> gsonRequest = new GsonRequest<TyFourHoursBean>(URLValues.TFHOURS_URL,
                TyFourHoursBean.class,
                new Response.Listener<TyFourHoursBean>() {
                    @Override
                    public void onResponse(TyFourHoursBean response) {
                        adapter.setBean(response);
                        plvTyHours.onRefreshComplete();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TyFourHoursActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance().addRequest(gsonRequest);
    }

    private void onClickBack() {
        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
