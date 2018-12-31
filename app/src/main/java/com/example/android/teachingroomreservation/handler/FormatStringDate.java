package com.example.android.teachingroomreservation.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormatStringDate {
    public String dateFormat(String yyyyDate){
        ArrayList date = new ArrayList();
        for(int i =0; i<yyyyDate.length(); i++){
            date.add(yyyyDate.charAt(i));
        }
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@" +date.size());
        String dd = date.get(8).toString()+date.get(9).toString();
        String mm = date.get(5).toString()+date.get(6).toString();
        String yyyy = date.get(0).toString()+date.get(1).toString()+date.get(2).toString()+date.get(3).toString();
        String dfm = dd + "-" + mm + "-" + yyyy;
        return dfm;
    }
}
