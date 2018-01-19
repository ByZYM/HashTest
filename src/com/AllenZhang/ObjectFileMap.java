/*
 * Copyright (c) 2018 by AllenZhang
 */

package com.AllenZhang;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO 增加一个缓冲区objMap，对于重复输入的文件不必再重新读入文件，而是直接操作
 * 通过文件读取对象
 * <p>
 * 一个文件装载对象的索引
 * 索引指向对象对应的文件名
 * <p>
 * 每个文件装载固定数量的对象
 * <p>
 * 获取对象：
 * 通过对象键值->查询到对象索引
 * 通过对象索引->查询到对象文件
 * 通过对象文件->根据对象键值查询对应对象
 *
 * @param <K> 查找对象的键
 * @param <V> 对象
 */
public class ObjectFileMap<K, V extends Serializable> {

    /**
     * 储存文件的目录
     */
    File objDirectory;

    /**
     * 索引映射文件
     */
    File indexMapFile;

    /**
     * 文件装载对象数量
     */
    int defaultFileSize = 1024;

    Integer lastIndex = 0;

    /**
     * 索引映射
     */
    Map<K, Long> indexMap;

    /**
     * 对象在文件中的映射
     */
    Map<K, V> objBufferedMap;

    public ObjectFileMap() throws IOException, ClassNotFoundException {
        this(new File("./objDirectory/"));
    }

    public ObjectFileMap(File objDirectory) throws IOException, ClassNotFoundException {
        this(objDirectory, 1024);
    }

    public ObjectFileMap(File objDirectory, int defaultFileSize) throws IOException, ClassNotFoundException {
        this.objDirectory = objDirectory;
        this.defaultFileSize = defaultFileSize;

        /* objDirectory初始化 */
        initFileMap(objDirectory);

    }

    /**
     * 1 初始化objDirectory 如果不存在则新建，如果存在则不新建
     * <p>
     * 2 初始化索引文件 如果不存在则新建，如果存在则不新建
     * <p>
     * 3 初始化indexMap 如果文件中没有 则新建
     *
     * @param objDirectory
     */
    private void initFileMap(File objDirectory) throws IOException, ClassNotFoundException {
        if (!objDirectory.exists()) {
            objDirectory.mkdirs();
        }

        String path = null;
        try {
            path = objDirectory.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        indexMapFile = new File(path + "\\indexMap.dat");

        /* 索引文件不存在 */
        if (!indexMapFile.exists()) {

            /* 获取索引文件的对象输出流 */
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(indexMapFile));

            /* 新建最大索引 */
            lastIndex = 0;
            oos.writeObject(lastIndex);

            /* 新建indexMap */
            indexMap = new HashMap<>();

            oos.writeObject(indexMap);

        }

        /* 索引文件存在 则读入indexMap */
        else {

            /* 获取索引文件的对象输入流 */
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(indexMapFile));

            System.out.println(indexMapFile);

            /* 将对应文件的对象读入lastIndex中 */
            lastIndex = (Integer) ois.readObject();

            /* 将对应文件的全部对象读入indexMap中 */
            indexMap = (Map<K, Long>) ois.readObject();

        }

        System.out.println(path + "\n" + indexMapFile);

    }

    /**
     * 映射对象到对应键，最后再保存对象到文件
     * @param collection 对象Map
     */
    public void putAll(Map<K, V> collection) {

    }

    /**
     * 映射对象到对应键，并保存对象到文件
     *
     * @param value 对象
     */
    public void putObject(K key, V value) throws IOException, ClassNotFoundException {
        Long index;

        /* 如果是新对象，新建索引 */
        if (indexMap.get(key) == null) {
            index = new Long(lastIndex++);
            indexMap.put(key, index);
        }
        /* 否则直接获取索引 */
        else {
            index = indexMap.get(key);
        }

        File objFile = getFileByIndex(index);

        /* 如果文件不存在则新建并保存 */
        if (!objFile.exists()) {
            objFile.createNewFile();
            objBufferedMap = new HashMap<>();
            objBufferedMap.put(key, value);

        }

        /* 否则读取文件再保存文件 */
        else {
            /* 读取 */
            /* 获取文件的对象输入流 */
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(objFile));

            /* 将对应文件的全部对象读入objBufferedMap中 */
            objBufferedMap = (HashMap<K, V>) ois.readObject();

            /* 从objBufferedMap设置对象 */
            objBufferedMap.put(key, value);
        }


        /* 保存对象 */
        ObjectOutputStream objectOOS = new ObjectOutputStream(new FileOutputStream(objFile));
        objectOOS.writeObject(objBufferedMap);

        /* 维护索引文件 */
        /* 获取索引文件的对象输出流 */
        ObjectOutputStream indexOOS = new ObjectOutputStream(new FileOutputStream(indexMapFile));

        /* 保存lastIndex */
        indexOOS.writeObject(lastIndex);

        /* 保存indexMap */
        indexOOS.writeObject(indexMap);

    }

    /**
     * 获取对象：
     * 通过对象键值->查询到对象索引
     * 通过对象索引->查询到对象文件
     * 通过对象文件->根据对象键值查询对应对象
     *
     * @param key 对象键值
     */
    public V getObject(K key) throws IOException, ClassNotFoundException {
        Long objIndex;

        /* 如果对象映射存在 */
        if ((objIndex = indexMap.get(key)) != null) {
            /* 找到对应文件 */
            File file = getFileByIndex(objIndex);

            /* 如果文件不存在 */
            if (!file.exists()) {
                return null;
            }

            /* 获取文件的对象输入流 */
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

            /* 将对应文件的全部对象读入objBufferedMap中 */
            objBufferedMap = (HashMap<K, V>) ois.readObject();

            /* 从objBufferedMap获取对象 */
            return objBufferedMap.get(key);

        } else return null;
    }

    /**
     * 根据索引获取文件
     *
     * @param index 索引
     * @return
     */
    File getFileByIndex(Long index) throws IOException {
        String path = objDirectory.getCanonicalPath();

        File objFile = new File(path + "\\" + (index / defaultFileSize) + ".dat");

        return objFile;
    }

}
