/*
 * Copyright (c) 2018 by AllenZhang
 */

package com.AllenZhang;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MAIN {


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Map<String, Mission> map = new TreeMap<>();

        ObjectFileMap<String,Mission> ofm=new ObjectFileMap<>();

        Mission m1 = new Mission();
        m1.setName("hh");

        Mission m2 = new Mission();
        m2.setName("我的天");

        for (int i = 0; i < 1000; i++) {
            m1.setName("hh"+i);
            ofm.putObject(i+"",m1);
        }

        System.out.println(ofm.getObject("0"));
        System.out.println(ofm.getObject("1"));
    }
}
