package com.funlisten.base.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYBaseActivity;
import com.funlisten.utils.ZYToast;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ZY on 17/3/21.
 */

public class ZYEditDescActivity extends ZYBaseActivity {

    public static final int RESULT_CODE_OK = 1703210524;

    public static final String RESULT_VALUE = "result_value";

    static final String MAX = "max";

    static final String MIN = "min";

    static final String DEF_TEXT = "def_text";

    static final String TITLE = "title";

    static final String HINT_TEXT = "hint_text";

    @Bind(R.id.editDesc)
    EditText editDesc;

    @Bind(R.id.textMax)
    TextView textMax;


    public static Intent createIntent(Context context, int max, String defText, String title, String hintText) {
        Intent intent = new Intent(context, ZYEditDescActivity.class);
        intent.putExtra(MAX, max);
        intent.putExtra(DEF_TEXT, defText);
        intent.putExtra(TITLE, title);
        intent.putExtra(HINT_TEXT, hintText);
        return intent;
    }

    public static Intent createIntent(Context context, int max, int min, String defText, String title, String hintText) {
        Intent intent = new Intent(context, ZYEditDescActivity.class);
        intent.putExtra(MAX, max);
        intent.putExtra(MIN, min);
        intent.putExtra(DEF_TEXT, defText);
        intent.putExtra(TITLE, title);
        intent.putExtra(HINT_TEXT, hintText);
        return intent;
    }

    protected int max;

    protected int min;

    protected String defText;

    protected String hintText;

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sr_activity_edit_desc);

        ButterKnife.bind(this);

        max = getIntent().getIntExtra(MAX, 0);
        min = getIntent().getIntExtra(MIN, 0);
        defText = getIntent().getStringExtra(DEF_TEXT);
        title = getIntent().getStringExtra(TITLE);
        hintText = getIntent().getStringExtra(HINT_TEXT);

        setHintText();

        if (title != null) {
            mActionBar.showTitle(title);
        }

        if (defText != null) {
            textMax.setText(defText.length() + "/" + max);
            editDesc.setText(defText);
        }

        if (max > 0) {
            editDesc.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max)});
        }

        mActionBar.showActionRightTitle("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = editDesc.getText().toString();
                Intent data = new Intent();
                data.putExtra(RESULT_VALUE, value);
                setResult(RESULT_CODE_OK, data);
                finish();
            }
        });

        editDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= max) {
                    textMax.setTextColor(getResources().getColor(R.color.c10));
                    ZYToast.show(ZYEditDescActivity.this, "最多输入" + max + "字");
                } else {
                    textMax.setTextColor(getResources().getColor(R.color.c6));
                }
                textMax.setText(s.length() + "/" + max);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setHintText() {
        if (hintText != null) {
            editDesc.setHint(hintText);
        }
    }

    @Override
    public void onBackPressed() {
        final String value = editDesc.getText().toString();

        if (min > 0 && value.trim().length() < min) {
            new AlertDialog.Builder(this).setMessage("最少输入" + min + "字").setPositiveButton("继续编辑", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            }).create().show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(RESULT_VALUE, value);
        setResult(RESULT_CODE_OK, data);
        super.onBackPressed();
    }
}
