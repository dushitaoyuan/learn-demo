package com.ncs.single.boot.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.mysql.jdbc.Driver;

import java.util.*;

public class MysqlGenerator {

    /**
     * MySQL 生成演示
     */
    public static void main(String[] args) {
        // 自定义需要填充的字段
        List<TableFill> tableFillList = new ArrayList<>();
        List<String> includeTableList = scanner();
        String[] includeTableArray = null;
        if (Objects.nonNull(includeTableList) && !includeTableList.isEmpty()) {
            includeTableArray = includeTableList.toArray(new String[includeTableList.size()]);
        }

        tableFillList.add(new TableFill("id", FieldFill.INSERT_UPDATE));
        String codeBaseDir = "d://codegen/boot/";
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator().setGlobalConfig(
                // 全局配置
                new GlobalConfig()
                        .setOutputDir(codeBaseDir)//输出目录
                        .setFileOverride(true)// 是否覆盖文件
                        .setActiveRecord(true)// 开启 activeRecord 模式
                        .setEnableCache(false)// XML 二级缓存
                        .setBaseResultMap(true)// XML ResultMap
                        .setBaseColumnList(true)// XML columList
                        .setAuthor("NCS")
                        .setEntityName("%sEntity")
                        .setMapperName("%sDao")
                        .setXmlName("%sDao")
                        .setServiceName("%sService")
                        .setServiceImplName("%sServiceImpl")
                        .setControllerName("%sController")
                        .setActiveRecord(false)
                        .setDateType(DateType.ONLY_DATE)//使用util date
        ).setDataSource(
                // 数据源配置
                new DataSourceConfig()
                        .setDbType(DbType.MYSQL)
                        .setTypeConvert(new MySqlTypeConvert() {
                            @Override
                            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                                if (fieldType.toLowerCase().contains("tinyint")) {
                                    System.out.println("转换类型：" + fieldType);
                                    return DbColumnType.INTEGER;
                                }
                                return super.processTypeConvert(globalConfig, fieldType);
                            }
                        })
                        .setDbQuery(new MySqlQuery() {

                            /**
                             * 重写父类预留查询自定义字段<br>
                             * 这里查询的 SQL 对应父类 tableFieldsSql 的查询字段，默认不能满足你的需求请重写它<br>
                             * 模板中调用：  table.fields 获取所有字段信息，
                             * 然后循环字段获取 field.customMap 从 MAP 中获取注入字段如下  NULL 或者 PRIVILEGES
                             */
                            @Override
                            public String[] fieldCustom() {
                                return new String[]{"NULL", "PRIVILEGES"};
                            }
                        })
                        .setDriverName(Driver.class.getName())
                        .setUsername("root")
                        .setPassword("root")
                        .setUrl("jdbc:mysql://127.0.0.1:3306/demo?useUnicode=true&characterEncoding=utf-8")
        ).setStrategy(
                new StrategyConfig()
                        .setNaming(NamingStrategy.underline_to_camel)// 表名生成策略
                        // .setInclude(new String[] { "user" }) // 需要生成的表
                        // .setExclude(new String[]{"test"}) // 排除生成的表
                        // 自定义实体，公共字段
                        .setTableFillList(tableFillList)
                        .setEntityBooleanColumnRemoveIsPrefix(true)
                        .setInclude(includeTableArray)

                        // 自定义 mapper 父类
                        // .setSuperMapperClass("com.baomidou.demo.TestMapper")
                        // 自定义 service 父类
                        // .setSuperServiceClass("com.baomidou.demo.TestService")
                        // 自定义 service 实现类父类
                        // .setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl")
                        // 自定义 controller 父类
                        // .setSuperControllerClass("com.baomidou.demo.TestController")
                        // 【实体】是否生成字段常量（默认 false）
                        // public static final String ID = "test_id";
                        // .setEntityColumnConstant(true)
                        // 【实体】是否为构建者模型（默认 false）
                        // public User setName(String name) {this.name = name; return this;}
                        // .setEntityBuilderModel(true)
                        // 【实体】是否为lombok模型（默认 false）<a href="https://projectlombok.org/">document</a>
                        .setEntityLombokModel(true)
                // Boolean类型字段是否移除is前缀处理
                // .setEntityBooleanColumnRemoveIsPrefix(true)
                // .setRestControllerStyle(true)
                // .setControllerMappingHyphenStyle(true)
        ).setPackageInfo(
                // 包配置
                new PackageConfig()
                        .setModuleName("boot")
                        .setParent("com.ncs.single")// 自定义包路径
                        .setController("controller")// 这里是控制器包名，默认 web
                        .setXml("mybatis/mapper")
                .setMapper("dao")
        ).setCfg(
                // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
                new InjectionConfig() {
                    @Override
                    public void initMap() {
                        Map<String, Object> map = new HashMap<>();
                        this.setMap(map);
                    }
                }
        ).setTemplate(
                // 关闭默认 xml 生成，调整生成 至 根目录
                new TemplateConfig().setController(null)
                // 自定义模板配置，模板可以参考源码 /mybatis-plus/src/main/resources/template 使用 copy
                // 至您项目 src/main/resources/template 目录下，模板名称也可自定义如下配置：
                // .setController("...");
                // .setEntity("...");
                // .setMapper("...");
                // .setXml("...");
                // .setService("...");
                // .setServiceImpl("...");
        );

        // 执行生成
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

    /**
     * 读取表
     */
    public static List<String> scanner() {
        Scanner scanner = new Scanner(System.in);
        String exitFlag = "q", allFlag = "all";
        List<String> tableList = new ArrayList<>();
        String tips = "请输入要生成的表名称,英文逗号分隔,q退出,all表示所有:";
        System.out.println(tips);
        while (scanner.hasNext()) {
            String input = scanner.next();
            if (input.equalsIgnoreCase(exitFlag)) {
                break;
            } else if (allFlag.equalsIgnoreCase(input)) {
                return null;
            } else {
                Arrays.stream(input.split(",")).forEach((String table) -> {
                    if (!table.trim().isEmpty()) {
                        tableList.add(table.trim());
                    }
                });
            }
        }
        return tableList;


    }


}