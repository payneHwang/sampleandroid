package com.sample_android.modules.popup_sample;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sample_android.R;

import java.util.List;

/**
 * Created by huang_jin on 2018/3/13.
 */

public class ListPopupWindow extends PopupWindow {
    /**
     * context
     */
    private Context mContext;
    /**
     * contentView
     */
    private View mContentView;
    /**
     * widgets
     */
    private TextView mTopTag;
    private ListView mLvList;
    private TextView mBottomBtn;

    /**
     * adapter
     */
    private List<String> mData;

    public ListPopupWindow(Context context, List<String> data) {
        this.mContext = context;
        this.mData = data;
        initView();
        initData();
    }

    private void initData() {
        if (mData != null && mData.size() != 0) {

        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.list_popup_window, null);
        this.setContentView(mContentView);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.T_mini_black)));
        this.setFocusable(true);
        mTopTag = mContentView.findViewById(R.id.tv_tag);
        mLvList = mContentView.findViewById(R.id.lv_list);
        mBottomBtn = mContentView.findViewById(R.id.tv_buttom);
    }


    private class ListDataAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.list_popup_item, null);

            } else {

            }
            return convertView;
        }
    }

}
