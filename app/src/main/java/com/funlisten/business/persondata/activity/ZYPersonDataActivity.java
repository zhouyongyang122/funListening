package com.funlisten.business.persondata.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.ZYApplication;
import com.funlisten.base.mvp.ZYBaseActivity;
import com.funlisten.base.view.ZYPicSelect;
import com.funlisten.base.view.ZYWheelSelectDialog;
import com.funlisten.business.login.model.ZYUserManager;
import com.funlisten.business.login.model.bean.ZYUser;
import com.funlisten.business.persondata.contract.ZYPersonContract;
import com.funlisten.business.persondata.model.ZYPersonModel;
import com.funlisten.business.persondata.presenter.ZYPersonPresenter;
import com.funlisten.business.persondata.view.ZYEditDialog;
import com.funlisten.business.user.model.ZYProvince;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;
import com.funlisten.utils.ZYUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gd on 2017/7/22.
 * 个人资料
 */

public class ZYPersonDataActivity extends ZYBaseActivity<ZYPersonContract.IPresenter> implements ZYPicSelect.PicSelectListener, ZYPersonContract.IView {
    @Bind(R.id.head_img)
    ImageView headImg;

    @Bind(R.id.nick_name)
    TextView nickName;

    @Bind(R.id.sex_tv)
    TextView sex;

    @Bind(R.id.age_tv)
    TextView age;

    @Bind(R.id.zone)
    TextView zone;

    @Bind(R.id.synopsis)
    TextView synopsis;

    @Bind(R.id.change_intro)
    TextView changeIntro;

    ZYUser zyUser;

    ZYPicSelect picSelect;

    ZYPersonPresenter personPresenter;

    ZYWheelSelectDialog dialogsex;
    ZYWheelSelectDialog dialogage;
    ArrayList<ZYProvince.City> cityList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gd_person_data_layout);
        showTitle("个人资料");
        zyUser = ZYUserManager.getInstance().getUser();
        personPresenter = new ZYPersonPresenter(this, new ZYPersonModel());
        personPresenter.subscribe();
        init();
    }

    private void init() {
        if (zyUser != null) {
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, headImg, zyUser.avatarUrl, R.drawable.def_avatar, R.drawable.def_avatar);
            nickName.setText(zyUser.nickname);
            if (zyUser.sex != null && zyUser.sex.equals("female")) {
                sex.setText("女");
            } else {
                sex.setText("男");
            }
            if (TextUtils.isEmpty(zyUser.intro)) {
                changeIntro.setText("去填写");
                changeIntro.setTextColor(getResources().getColor(R.color.c9));
            } else {
                changeIntro.setText("修改");
                changeIntro.setTextColor(getResources().getColor(R.color.c3));
            }
            age.setText(TextUtils.isEmpty(zyUser.age) ? "无" : zyUser.age);
            synopsis.setText(TextUtils.isEmpty(zyUser.intro) ? "这个人很懒，什么都没写......" : zyUser.intro);
            zone.setText(zyUser.areaName);
        }

    }

    @OnClick({R.id.head_img, R.id.sex_line, R.id.age_line, R.id.area_line, R.id.nick_line, R.id.synopsis_line, R.id.finish})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.head_img:
                if (picSelect == null) {
                    picSelect = new ZYPicSelect(ZYPersonDataActivity.this, ZYPersonDataActivity.this);
                }
                picSelect.showSelectDialog();
                break;
            case R.id.nick_line:
                ZYEditDialog dialog = new ZYEditDialog(mActivity, 0);
                dialog.setOnSelectText(new ZYEditDialog.OnSelectText() {
                    @Override
                    public void onSelecte(String text) {
                        zyUser.nickname = text + "";
                        nickName.setText(text + "");
                    }
                });
                dialog.show();
                break;
            case R.id.sex_line:
                if (dialogsex == null) {
                    dialogsex = new ZYWheelSelectDialog(this, new String[]{"男", "女"}, new ZYWheelSelectDialog.WheelSelectListener() {
                        @Override
                        public void onWheelSelected(ZYWheelSelectDialog dialog, int position, String value) {
                            zyUser.sex = value.equals("男") ? "male" : "female";
                            sex.setText(value);
                        }
                    });
                }
                dialogsex.showDialog(0);
                break;
            case R.id.age_line:
                if (dialogage == null) {
                    dialogage = new ZYWheelSelectDialog(this, new String[]{"00后", "90后", "80后", "70后"}, new ZYWheelSelectDialog.WheelSelectListener() {
                        @Override
                        public void onWheelSelected(ZYWheelSelectDialog dialog, int position, String value) {
                            zyUser.age = value;
                            age.setText(TextUtils.isEmpty(zyUser.age) ? "无" : zyUser.age);
                        }
                    });
                }
                dialogage.showDialog(0);
                break;
            case R.id.area_line:
                Intent intent = new Intent(ZYPersonDataActivity.this, ZYAreaActivity.class);
                ArrayList<ZYProvince> list = personPresenter.getProvince();
                Bundle bundle = new Bundle();
                bundle.putSerializable("province", list);
                intent.putExtras(bundle);
                startActivityForResult(intent, 400);
                break;
            case R.id.synopsis_line:
                ZYEditDialog dialogs = new ZYEditDialog(mActivity, 0);
                dialogs.setOnSelectText(new ZYEditDialog.OnSelectText() {
                    @Override
                    public void onSelecte(String text) {
                        zyUser.intro = text + "";
                        synopsis.setText(text + "");
                    }
                });
                dialogs.show();
                break;
            case R.id.finish:
                HashMap<String, String> map = new HashMap();
                if (!TextUtils.isEmpty(zyUser.nickname)) map.put("nickname", zyUser.nickname);
                if (!TextUtils.isEmpty(zyUser.sex))
                    map.put("sex", "男".equals(zyUser.sex) ? "male" : "female");
                if (!TextUtils.isEmpty(zyUser.age)) map.put("ageRange", zyUser.age);
                if (!TextUtils.isEmpty(zyUser.areaCode)) map.put("areaCode", zyUser.areaCode);
                if (!TextUtils.isEmpty(zyUser.areaName)) map.put("areaName", zyUser.areaName);
                if (!TextUtils.isEmpty(zyUser.intro)) map.put("intro", zyUser.intro);
                personPresenter.updateUserDetail(map);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (picSelect != null)
            picSelect.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 400 && resultCode == 400 && data != null) {
            int index = data.getIntExtra("position", -1);
            cityList.clear();
            cityList.addAll(personPresenter.getProvince().get(index).cities);
            Intent intent = new Intent(ZYPersonDataActivity.this, ZYAreaActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("city", cityList);
            intent.putExtras(bundle);
            startActivityForResult(intent, 401);
        }
        if (requestCode == 401 && resultCode == 401 && data != null) {
            int indexs = data.getIntExtra("position", -1);
            ZYProvince.City city = cityList.get(indexs);
            zyUser.areaCode = city.cityCode;
            zyUser.areaName = city.cityName;
            zone.setText(zyUser.areaName);
        }
    }

    @Override
    public void updateUser() {
        ZYUserManager.getInstance().setUser(zyUser);
        finish();
    }

    @Override
    public void onPicSelected(Uri uri) {
        File file = new File(ZYApplication.IMG_CACHE_DIR + "temp.png");
        ZYUtils.compressToSize(uri.getPath(), file, 100 * 1024);
        ZYImageLoadHelper.getImageLoader().loadCircleImage(this, headImg, uri.getPath(), R.drawable.def_avatar, R.drawable.def_avatar);
        personPresenter.updateUserAvatar(file);
    }
}
