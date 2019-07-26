package com.ckw.contactindexview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<TreePoint> pointList = new ArrayList<>();
    private HashMap<String, TreePoint> pointMap = new HashMap<>();
    private ContactView mContactView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContactView = findViewById(R.id.contact_view);
        initData();
    }

    private void initData() {
        pointList.clear();
        int id =1000;
        int parentId = 0;
        int parentId2 = 0;
        int parentId3 = 0;
        for(int i=1;i<6;i++){
            id++;
            pointList.add(new TreePoint(""+id,"分类"+i,"" + parentId,"0",i));
            for(int j=1;j<5;j++){
                if(j==1){
                    parentId2 = id;
                }
                id++;
                pointList.add(new TreePoint(""+id,"分类"+i+"_"+j,""+parentId2,"0",j));
                for(int k=1;k<5;k++){
                    if(k==1){
                        parentId3 = id;
                    }
                    id++;
                    pointList.add(new TreePoint(""+id,"分类"+i+"_"+j+"_"+k,""+parentId3,"1",k));
                }
            }
        }
        //打乱集合中的数据
//        Collections.shuffle(pointList);
        //对集合中的数据重新排序
        for (TreePoint treePoint : pointList) {
            pointMap.put(treePoint.getID(), treePoint);
        }

        mContactView.setData(pointList,pointMap);
        mContactView.setHeadImg(R.mipmap.icon_house);
    }
}
