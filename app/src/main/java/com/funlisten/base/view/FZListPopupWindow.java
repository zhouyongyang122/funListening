package com.funlisten.base.view;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.funlisten.R;

import java.util.List;

/**
 * Created by zhouyongyang on 16/10/19.
 */

public class FZListPopupWindow extends ListPopupWindow implements AdapterView.OnItemClickListener, PopupWindow.OnDismissListener {

    private FZListPopupWindowListener listPopupWindowListener;

    private List<String> menuData;

    private FZListPopupWindwoAdapter adapter;

    private Context context;

    private String selectedValue;

    private int resLayoutID;

    private int defaultTextColor;

    public FZListPopupWindow(Context context, FZListPopupWindowListener listPopupWindowListener) {
        super(context);
        this.context = context;
        this.listPopupWindowListener = listPopupWindowListener;
        this.setModal(true);
        this.setOnItemClickListener(this);
        this.setOnDismissListener(this);
        this.resLayoutID = R.layout.fz_view_popup_def_item;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listPopupWindowListener != null) {
            listPopupWindowListener.onItemClick(menuData.get(position), position);
            this.dismiss();
        }
    }

    public void showWindow(View view, int width, List<String> menuData) {
        show(view,width,menuData);
    }

    /**
     * 显示
     * @param view
     * @param alignRight true表示跟view右对齐 false表示跟view左对齐
     * @param width
     * @param menuData
     */
    public void showWindow(View view, boolean alignRight, int width, List<String> menuData) {

        if(alignRight){
            if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.KITKAT) {
                setDropDownGravity(Gravity.END);
            }
        }else {
            if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.KITKAT) {
                setDropDownGravity(Gravity.START);
            }
        }
        show(view,width,menuData);
    }

    private void show(View view, int width, List<String> menuData){
        this.menuData = menuData;
        if (width > 0) {
            this.setWidth(width);
        }
        if (adapter == null) {
            adapter = new FZListPopupWindwoAdapter();
            setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        this.setAnchorView(view);
        setModal(true);
        this.show();
    }


    @Override
    public void onDismiss() {
        if (listPopupWindowListener != null) {
            listPopupWindowListener.onDismiss();
        }
    }

    public void setResLayoutID(int resLayoutID) {
        this.resLayoutID = resLayoutID;
    }

    public void setDefaultTextColor(int defaultTextColor) {
        this.defaultTextColor = defaultTextColor;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    public class FZListPopupWindwoAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return menuData.size();
        }

        @Override
        public Object getItem(int position) {
            return menuData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHodler viewHodler;
            if (convertView == null) {
                viewHodler = new ViewHodler();
                convertView = LayoutInflater.from(context).inflate(resLayoutID, null);
                viewHodler.popupText = (TextView) convertView.findViewById(R.id.popupText);
                viewHodler.viewLine = convertView.findViewById(R.id.viewLine);
                convertView.setTag(viewHodler);
            } else {
                viewHodler = (ViewHodler) convertView.getTag();
            }
            String value = menuData.get(position);
            viewHodler.popupText.setText(menuData.get(position));
            if (selectedValue != null && selectedValue.equals(value)) {
                viewHodler.popupText.setTextColor(context.getResources().getColor(R.color.c1));
            } else {
                if (defaultTextColor > 0) {
                    viewHodler.popupText.setTextColor(defaultTextColor);
                } else {
                    viewHodler.popupText.setTextColor(context.getResources().getColor(R.color.c5));
                }

            }

            if (position >= menuData.size() - 1) {
                viewHodler.viewLine.setVisibility(View.GONE);
            } else {
                viewHodler.viewLine.setVisibility(View.VISIBLE);
            }
            return convertView;
        }
    }

    static class ViewHodler {
        public TextView popupText;
        public View viewLine;
    }


    public interface FZListPopupWindowListener {
        void onItemClick(String menuStr, int selectedIndex);

        void onDismiss();
    }
}
