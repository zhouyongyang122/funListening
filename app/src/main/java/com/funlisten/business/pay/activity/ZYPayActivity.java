package com.funlisten.business.pay.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.funlisten.R;
import com.funlisten.base.event.ZYEventPaySuc;
import com.funlisten.base.mvp.ZYBaseActivity;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.pay.contract.ZYPayContract;
import com.funlisten.business.pay.model.ZYPayModel;
import com.funlisten.business.pay.model.bean.PayInfo;
import com.funlisten.business.pay.presenter.ZYPayPresenter;
import com.funlisten.business.pay.view.viewholder.ZYPayFooterVH;
import com.funlisten.business.pay.view.viewholder.ZYPayHeaderDetails;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.utils.ZYToast;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by gd on 2017/7/28.
 */

public class ZYPayActivity extends ZYBaseActivity implements ZYPayFooterVH.PayListener, ZYPayContract.View {
    LinearLayout view;
    ZYPayHeaderDetails headerDetails;
    ZYPayFooterVH footerVH;
    ArrayList<ZYAudio> audioList;
    public  static  String type;
    ZYPayPresenter payPresenter;
    ZYAlbumDetail zyAlbum;
    static  int index; // 1 专辑  2 音频 list
    public static Intent createIntent(Context context, ArrayList<ZYAudio> audio){
        Intent intent  = new Intent(context,ZYPayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("audioList",audio);
        index = 2;
        type = "audioList";
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent createIntent(Context context, ZYAlbumDetail zyAlbum){
        Intent intent  = new Intent(context,ZYPayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("album",zyAlbum);
        index = 1;
        type = "album";
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.gd_pay_layout, null);
        setContentView(view);
        showTitle("支付订单");
        if(index == 1)
            zyAlbum = (ZYAlbumDetail) getIntent().getSerializableExtra("album");
        else
            audioList = (ArrayList<ZYAudio>) getIntent().getSerializableExtra("audioList");

        payPresenter = new ZYPayPresenter(new ZYPayModel(),this);
        initView();
    }

    private  void initView(){
        headerDetails = new ZYPayHeaderDetails();
        headerDetails.attachTo(view);
        headerDetails.updateView(zyAlbum,0);
        footerVH = new ZYPayFooterVH(this);
        footerVH.attachTo(view);
        footerVH.updateView(null,0);
    }

    /***
     * 支付
     * **/
    @Override
    public void onPay(int payType) {
        String  gson;
        if(index == 1){
            gson = new Gson().toJson(Arrays.asList(new PayInfo(zyAlbum.id+"","album")));
        }else {
            ArrayList<PayInfo> payInfos = new ArrayList<>();
            for(ZYAudio audio:audioList)payInfos.add(new PayInfo(audio.id+"","audio"));
            gson = new Gson().toJson(payInfos);
        }

        if(payType ==1 ){
            payPresenter.getSignALiAudio(gson);
        }else if(payType ==2){
            payPresenter.getWeChatSign(gson);
        }
    }

    @Override
    public void onPayFaild() {
        ZYToast.show(ZYPayActivity.this,"支付失败");
    }

    @Override
    public void onPaySuccess() {
        EventBus.getDefault().post(new ZYEventPaySuc());
        finish();
    }
}
