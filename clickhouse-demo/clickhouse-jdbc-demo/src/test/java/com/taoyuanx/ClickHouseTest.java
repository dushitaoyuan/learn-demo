package com.taoyuanx;


import com.taoyuanx.demo.DemoBootApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoBootApplication.class)
@Slf4j
public class ClickHouseTest {
    @Autowired
    DataSource dataSource;
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * 构造3千万数据
     */
    @Test
    public void testDataSql() throws Exception {
        String sql = "insert into tb_test values (?, ?,?, ?, ?)";

        ThreadPoolExecutor executorService = new ThreadPoolExecutor(3, 3,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(10), new ThreadPoolExecutor.CallerRunsPolicy());
        int limit = 30000000;
        List<Object[]> batchArgs = new ArrayList<>();
        int batchSize = 1000, count = 0;
        for (int i = 1; i < limit; i++) {
            count++;
            batchArgs.add(new Object[]{i, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()), "demo" + i, Float.valueOf(i), DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now())});
            if (count >= batchSize) {
                List<Object[]> finalBatchArgs = batchArgs;
                executorService.submit(() -> {
                    saveBatch(sql, finalBatchArgs);
                });
                count = 0;
                batchArgs = new ArrayList<>();
            }
        }

        if (batchArgs.isEmpty()) {
            List<Object[]> finalBatchArgs = batchArgs;
            executorService.submit(() -> {
                saveBatch(sql, finalBatchArgs);
            });
            batchArgs.clear();
        }
        executorService.shutdown();
        while (!executorService.awaitTermination(3, TimeUnit.SECONDS)) {
            Thread.sleep(3000L);
        }
        System.out.println("finshed");
    }

    /**
     * 关联表数据构造
     */
    @Test
    public void testDataRefSql() throws Exception {
        Random random = new Random();
        String sql = "insert into tb_test_ref values (?, ?,?, ?)";
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(3, 3,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(10), new ThreadPoolExecutor.CallerRunsPolicy());
        int limit = 3000000;
        List<Object[]> batchArgs = new ArrayList<>();
        int batchSize = 1000, count = 0;
        for (int i = 1; i < limit; i++) {
            count++;
            batchArgs.add(new Object[]{i, i + 100, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()), random.nextInt(4)});
            if (count >= batchSize) {
                List<Object[]> finalBatchArgs = batchArgs;
                executorService.submit(() -> {
                    saveBatch(sql, finalBatchArgs);
                });
                count = 0;
                batchArgs = new ArrayList<>();
            }
        }

        if (batchArgs.isEmpty()) {
            List<Object[]> finalBatchArgs = batchArgs;
            executorService.submit(() -> {
                saveBatch(sql, finalBatchArgs);
            });
            batchArgs.clear();
        }
        executorService.shutdown();
        while (!executorService.awaitTermination(3, TimeUnit.SECONDS)) {
            Thread.sleep(3000L);
        }
        System.out.println("finshed");
    }

    public void saveBatch(String sql, List<Object[]> batchArgs) {
        Long start = System.currentTimeMillis();
        jdbcTemplate.batchUpdate(sql, batchArgs);
        System.out.println("batch finsh 耗时:" + (System.currentTimeMillis() - start));
    }

    @Test
    public void queryTest() {
        /**
         * 单表多条件查询测试
         */
        time(() -> {
            System.out.println(jdbcTemplate.queryForMap("select * from tb_test where id=300000"));
        });
        time(() -> {
            System.out.println(jdbcTemplate.queryForList("select * from tb_test where  content='demo3000' and datetime  between '2020-08-03 13:55:00' and  '2020-08-03 13:56:00'"));
        });

        time(() -> {
            System.out.println(jdbcTemplate.queryForList("select * from tb_test where  content like'demo3000%' "));
        });

        time(() -> {
            System.out.println(jdbcTemplate.queryForList("select * from tb_test where  content='demo3000' and value=3000 and datetime  between '2020-08-03 13:55:00' and  '2020-08-03 13:56:00'"));
        });
        /**
         * 两表join 多条件查询测试
         */
        time(()->{
            String sql="        select * from tb_test t join  tb_test_ref tf on tf.tb_test_id=t.id  where tf.status=1 and tf.datetime between '2020-08-03 14:39:07' and  '2020-08-03 14:39:07' and tf.id =54142";
            System.out.println(jdbcTemplate.queryForList(sql));

        });

        time(()->{
            String sql="select status,count() sum  from tb_test t right join  tb_test_ref tf on tf.tb_test_id=t.id  where tf.status<10  group by status  order by sum desc";
            System.out.println(jdbcTemplate.queryForList(sql));

        });
    }

    public void time(Runnable runnable) {
        Long start = System.currentTimeMillis();
        runnable.run();
        System.out.println("耗时:" + (System.currentTimeMillis() - start));
    }


}
