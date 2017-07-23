package com.funlisten.business.search.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.adapter.ZYBaseRecyclerAdapter;
import com.funlisten.base.mvp.ZYBaseActivity;
import com.funlisten.base.view.FZListPopupWindow;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.view.viewHolder.ZYCommentItemVH;
import com.funlisten.business.login.model.ZYUserManager;
import com.funlisten.business.play.view.ZYPlayFragment;
import com.funlisten.business.search.contract.ZYSearchContract;
import com.funlisten.business.search.model.ZYSearchHistoryManager;
import com.funlisten.business.search.model.ZYSearchModel;
import com.funlisten.business.search.model.bean.ZYSearchHistory;
import com.funlisten.business.search.presenter.ZYSearchPresenter;
import com.funlisten.business.search.view.WarpLinearLayout;
import com.funlisten.business.search.view.viewholder.ZYHistoryVH;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gd on 2017/7/18.
 */

public class ZYSearchActivity extends ZYBaseActivity<ZYSearchContract.IPresenter> implements ZYSearchContract.IView {
    @Bind(R.id.album_tv)
    TextView albumType;

    @Bind(R.id.select_album)
    ImageView selectBtn;

    @Bind(R.id.keyword)
    EditText keyword;

    @Bind(R.id.search_cancel)
    TextView searchCancel;

    @Bind(R.id.warpLinearLayout)
    WarpLinearLayout warpLinearLayout;

    @Bind(R.id.history_list)
    RecyclerView hisList;

    @Bind(R.id.history_line)
    LinearLayout history_line;

    FZListPopupWindow fzListPopupWindow;
    String textKey;
    boolean onBack = true;
    ZYSearchPresenter searchPresenter;

    List<ZYSearchHistory> historyList = new ArrayList<>();

    ZYBaseRecyclerAdapter<Object> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gd_search_layout);
        searchPresenter = new ZYSearchPresenter(this,new ZYSearchModel());
        searchPresenter.loadHotWord();
        historyList.addAll(ZYSearchHistoryManager.getInsatnce().getAllHistory());
        hideActionBar();
        initView();
    }
    private void initView(){
        fzListPopupWindow = new FZListPopupWindow(this,listPopupWindowListener);
        keyword.addTextChangedListener(textWatcher);
        adapter = new ZYBaseRecyclerAdapter(historyList) {
            @Override
            public ZYBaseViewHolder createViewHolder(int type) {
                return new ZYHistoryVH();
            }
        };
        hisList.setLayoutManager(new LinearLayoutManager(ZYSearchActivity.this));
        hisList.setAdapter(adapter);
        adapter.setOnItemClickListener(itemListener);
    }

    ZYBaseRecyclerAdapter.OnItemClickListener  itemListener = new ZYBaseRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            keyword.setText("");
            ZYSearchHistory his = historyList.get(position);
            keyword.setText(his.history);
            keyword.setSelection(his.history.length());
            history_line.setVisibility(View.GONE);
        }
    };

    @OnClick({R.id.search_cancel,R.id.select_album,R.id.refresh_hot_word,R.id.delete_history})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.search_cancel:
                if(onBack){
                    finish();
                }else {
                    String key = keyword.getText().toString();
                    String userId = ZYUserManager.getInstance().getUser().userId;
                    if(TextUtils.isEmpty(userId))break;
                    ZYSearchHistoryManager.getInsatnce().save(new ZYSearchHistory(userId,key));
                }
                break;
            case R.id.select_album:
                if(!fzListPopupWindow.isShowing()){
                    fzListPopupWindow.showWindow(albumType,300, Arrays.asList("专辑","音频"));
                }
                break;
            case R.id.refresh_hot_word:
                searchPresenter.loadHotWord();
                break;
            case R.id.delete_history:
                ZYSearchHistoryManager.getInsatnce().deleteHistory();
                historyList.clear();
                adapter.notifyDataSetChanged();
                break;
        }

    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            textKey =s.toString();
            if (textKey !=null && textKey.trim().length() > 0) {
                searchCancel.setText("搜索");
                if(history_line.getVisibility() == View.INVISIBLE || history_line.getVisibility() == View.GONE){
                    history_line.setVisibility(View.VISIBLE);
                }
                onBack = false;
            }else {
                searchCancel.setText("取消");
                onBack = true;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    FZListPopupWindow.FZListPopupWindowListener listPopupWindowListener = new FZListPopupWindow.FZListPopupWindowListener() {
        @Override
        public void onItemClick(String menuStr, int selectedIndex) {
            albumType.setText(menuStr);
        }

        @Override
        public void onDismiss() {

        }
    };

    private void addHotWord(List<String> list){
        warpLinearLayout.removeAllViews();
        list.addAll((Arrays.asList("大学生的恋爱","逻辑思维","天天向上")));
        for(String hot:list){
            TextView tv = new TextView(this);
            tv.setText(hot);
            tv.setBackgroundResource(R.drawable.shape_white_stroke);
            tv.setPadding(20,20,20,20);
            tv.setTextSize(14f);
            tv.setOnClickListener(onclikListener);
            tv.setTag(hot);
            warpLinearLayout.addView(tv);
        }
    }

    View.OnClickListener onclikListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String str = (String) v.getTag();
            keyword.setText("");
            keyword.setText(str);
            keyword.setSelection(str.length());
        }
    };

    @Override
    public void showList(boolean isHasMore) {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void success(List<String> data) {

    }

    @Override
    public void showHotWord(List<String> data) {
        addHotWord(data);
    }
}
