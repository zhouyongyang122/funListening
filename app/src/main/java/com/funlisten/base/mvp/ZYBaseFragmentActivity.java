package com.funlisten.base.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.funlisten.R;

/**
 * Created by ZY on 17/3/15.
 */

public abstract class ZYBaseFragmentActivity<T extends Fragment> extends ZYBaseActivity {

    protected T mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_base_fragment);
        addFragment();
    }

    protected void addFragment() {
        mFragment = (T) getSupportFragmentManager().findFragmentById(R.id.layoutContent);
        if (mFragment == null) {
            mFragment = createFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.layoutContent, mFragment);
            transaction.commit();
        }
    }

    protected abstract T createFragment();
}
