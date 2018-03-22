package com.wtree.scrollortable.helper;

import android.text.TextUtils;
import android.util.Log;

import com.wtree.scrollortable.member.ItemInfo;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WTree on 2018/3/22.
 */

public class DataHelper {

    public static List<ItemInfo> createData(int pos){
        List<ItemInfo> data=new ArrayList<>(20);
        for(int i=pos;i<pos+20;i++){
            ItemInfo info=createInfo(i);
            data.add(info);
        }
        return data;
    }


    private static ItemInfo createInfo(int i){

        ItemInfo info=new ItemInfo();
        Class<?> cls=info.getClass();
        Field[] fields=cls.getFields();
        for(Field field:fields){
            field.setAccessible(true);
            String name=field.getName();
            if(!field.getType().isInstance(name)){
                continue;
            }
            if(TextUtils.isEmpty(name)){
                continue;
            }
            name=name+"_"+i;
            try {
                field.set(info,name);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return info;
    }
}
