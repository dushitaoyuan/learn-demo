package com.taoyuanx;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rocksdb.*;

import java.util.*;

/**
 * @author dushitaoyuan
 * @desc rocksdb 基本测试
 * @date 2021/3/16
 */

/**
 * 源码地址:https://github.com/facebook/rocksdb/blob/master/java/src/test/java/org/rocksdb/ColumnFamilyTest.java
 */
public class RocksDBMemoryTest {

    private String dbPath = "./dbData";

    @BeforeClass
    public static void before() {
        RocksDB.loadLibrary();
    }

    @Test
    public void lruCachePutTest() {
        try (final Cache cache = new LRUCache(1 * 1024 * 1024);
             final Options options =
                     new Options()
                             .setCreateIfMissing(true)
                             .setTableFormatConfig(new BlockBasedTableConfig().setBlockCache(cache));
             RocksDB db = RocksDB.open(options, dbPath);
             final FlushOptions flushOptions =
                     new FlushOptions().setWaitForFlush(true);
        ) {
            long start = System.currentTimeMillis();
            int keySize = 2 * 100 * 1024;
            for (int i = 0; i < keySize; i++) {
                String key = "key_" + i;
                String value = "value_" + i;
                db.put(key.getBytes(), value.getBytes());
            }
            long end = System.currentTimeMillis();
            System.out.println("插入" + keySize + "个kv值,耗时:" + (end - start));
            db.flush(flushOptions);
            long flushEnd = System.currentTimeMillis();
            System.out.println("持久化" + keySize + "个kv值,耗时:" + (flushEnd - end));

        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void lruCacheGutTest() {
        try (final Cache cache = new LRUCache(1 * 1024 * 1024);
             final Options options =
                     new Options()
                             .setCreateIfMissing(true)
                             .setTableFormatConfig(new BlockBasedTableConfig().setBlockCache(cache));
             RocksDB db = RocksDB.open(options, dbPath);
        ) {
            int keySize = 2 * 100 * 1024;
            for (int i = 0; i < keySize; i++) {
                String key = "key_" + i;
                byte[] value = db.get(key.getBytes());
                if (Objects.isNull(value)) {
                    System.out.println(key + "\t不存在");
                } else {
                    System.out.println(new String(value));
                }

            }


        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }


}
