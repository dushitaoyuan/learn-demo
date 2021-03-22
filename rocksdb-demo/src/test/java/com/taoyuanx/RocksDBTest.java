package com.taoyuanx;

import org.junit.BeforeClass;
import org.junit.Test;
import org.rocksdb.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author dushitaoyuan
 * @desc rocksdb 基本测试
 * @date 2021/3/16
 */

/**
 * 源码地址:https://github.com/facebook/rocksdb/blob/master/java/src/test/java/org/rocksdb/ColumnFamilyTest.java
 */
public class RocksDBTest {

    private String dbPath = "./dbData";

    @BeforeClass
    public static void before() {
        RocksDB.loadLibrary();
    }

    @Test
    public void crudTest() {
        Options options = new Options().setCreateIfMissing(true);
        try (RocksDB rocksDB = RocksDB.open(options, dbPath)) {
            List<byte[]> keyList = Arrays.asList("hello".getBytes(), "key1".getBytes(), "key2".getBytes());
            List<byte[]> valueList = Arrays.asList("World".getBytes(), "value1".getBytes(), "value2".getBytes());


            for (int i = 0, len = keyList.size(); i < len; i++) {
                rocksDB.put(keyList.get(i), valueList.get(i));
            }

            List<byte[]> multiGetAsList = rocksDB.multiGetAsList(keyList);
            System.out.println("multiGetAsList" + Arrays.toString(multiGetAsList.stream().map(value -> {
                return new String(value);
            }).toArray()));

            /**
             * 遍历所有key
             */
            RocksIterator iter = rocksDB.newIterator();
            for (iter.seekToFirst(); iter.isValid(); iter.next()) {
                System.out.println("key:" + new String(iter.key()) + ", value:" + new String(iter.value()));
            }
            /**
             * 更新
             */
            String old = new String(rocksDB.get(keyList.get(0)));
            rocksDB.put(keyList.get(0), "world2.0".getBytes());

            System.out.println("更新前:" + old + "\t更新后:" + new String(rocksDB.get(keyList.get(0))));
            //删除
            rocksDB.delete(keyList.get(0));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 在RocksDB3.0，我们增加了Column Families的支持。
     * RocksDB的每个键值对都与唯一一个列族（column family）结合。如果没有指定Column Family，键值对将会结合到“default” 列族。
     * 列族提供了一种从逻辑上给数据库分片的方法。他的一些有趣的特性包括：
     * 支持跨列族原子写。意味着你可以原子执行Write({cf1, key1, value1}, {cf2, key2, value2})。
     * 跨列族的一致性视图。
     * 允许对不同的列族进行不同的配置
     * 即时添加／删除列族。两个操作都是非常快的。
     */
    @Test
    public void columnFamilyTest() {
        Options options = new Options().setCreateIfMissing(true);
        RocksDB rocksDB = null;
        try {
            rocksDB = RocksDB.open(options, dbPath);
            //查询所有列族
            List<byte[]> columnFamilyNames = RocksDB.listColumnFamilies(options,
                    dbPath);
            columnFamilyNames.stream().forEach(cf -> {
                System.out.println(new String(cf));
            });
            //创建列族
            String cfName = "demo";
            ColumnFamilyDescriptor cfDescriptor = new ColumnFamilyDescriptor(cfName.getBytes(),
                    new ColumnFamilyOptions());
            ColumnFamilyHandle mycf = rocksDB.createColumnFamily(cfDescriptor);
            System.out.println("cf:\t" + mycf.getID() + "\t" + new String(mycf.getName()));
            rocksDB.close();
            //打开列族
            List<ColumnFamilyDescriptor> cfNames = Arrays.asList(
                    new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY),
                    new ColumnFamilyDescriptor(cfName.getBytes())
            );

            DBOptions myOp = new DBOptions()
                    .setCreateIfMissing(true)
                    .setCreateMissingColumnFamilies(true);
            List<ColumnFamilyHandle> columnFamilyHandleList =
                    new ArrayList<>();
            rocksDB = RocksDB.open(myOp,
                    dbPath, cfNames,
                    columnFamilyHandleList);
            columnFamilyHandleList.stream().forEach(cfh -> {
                System.out.println("cf:\t" + cfh.getID());
            });
            //删除列族
           // rocksDB.dropColumnFamily(mycf);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (Objects.nonNull(rocksDB)) {
                rocksDB.close();
            }
        }
    }



}
