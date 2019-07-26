package com.ckw.contactindexview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ckw
 * on 2019/7/26.
 */
public class ContactView extends RelativeLayout {

    private ListView mListView;
    private IndexPathView mIndexPathView;
    private ImageView mImageView;
    private List<TreePoint> pointList = new ArrayList<>();
    private HashMap<String, TreePoint> pointMap = new HashMap<>();
    private TreeAdapter adapter;

    public ContactView(Context context) {
        this(context,null);
    }

    public ContactView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ContactView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view  = LayoutInflater.from(context).inflate(R.layout.contact_view,null);
        mListView = view.findViewById(R.id.listView);
        mIndexPathView = view.findViewById(R.id.index_path_view);
        mImageView = view.findViewById(R.id.iv_header);
        adapter = new TreeAdapter(context, pointList, pointMap);
        adapter.setOperateMode(1);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.onItemClick(position);
                List<Integer> currentList = getCurrentList();
                mIndexPathView.updateView(currentList);
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int offset = getOffset();
                mIndexPathView.updateOffset(offset);
            }
        });
        addView(view);
    }

    /*
    * 获得listview的偏移量
    * */
    private int getOffset() {
        View c = mListView.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = mListView.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight() ;
    }

    public void setHeadImg(int resId){
        mImageView.setImageResource(resId);
        invalidate();
    }

    /*
    * 设置数据
    * */
    public void setData(List<TreePoint> pointList, HashMap<String, TreePoint> pointMap){
        this.pointList.addAll(pointList);
        this.pointMap.putAll(pointMap);
        adapter.notifyDataSetChanged();
        mIndexPathView.updateView(getCurrentList());
        invalidate();
    }

    /*
    * 获得当前展示的列表
    * */
    public List<Integer> getCurrentList(){
        List<Integer> list = new ArrayList<>();
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            TreePoint treePoint = (TreePoint) adapter.getItem(i);
            if("0".equals(treePoint.getPARENTID())){
                list.add(0);
            }else {
                list.add(-1);
            }
        }
        return list;
    }




}
