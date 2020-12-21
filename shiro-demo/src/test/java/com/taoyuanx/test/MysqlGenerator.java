package com.taoyuanx.test;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = BootApplication.class)
@Slf4j
public class MysqlGenerator {
    @Autowired
    DataSource dataSource;

    @Test
    public void generatorTest() {
        AutoGenerator mpg = new AutoGenerator();

        GlobalConfig gc = new GlobalConfig();
        //util时间
        gc.setDateType(DateType.ONLY_DATE);
        //文件覆盖
       // gc.setFileOverride(true);
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("taoyuan");   // 作者
        gc.setOpen(false);      //生成代码后是否打开文件夹
        gc.setServiceName("%sService");
        gc.setEntityName("%sEntity");
        mpg.setGlobalConfig(gc);

        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://10.10.10.132:3306/eaem?useUnicode=true&serverTimezone=GMT&useSSL=false&characterEncoding=utf8");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        mpg.setDataSource(dsc);

        PackageConfig pc = new PackageConfig();
        pc.setModuleName("common");
        pc.setParent("com.ncs.rm");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setMapper("mapper");
        pc.setEntity("entity");
        pc.setXml("mapper.xml");
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
      strategy.setSuperControllerClass("com.lcy.demo.sys.controller.BaseController");
        strategy.setSuperEntityClass("com.lcy.demo.sys.entity.BaseEntity");

        strategy.setEntityLombokModel(true);
        strategy.setInclude("user", "role", "permission");  // 如果要生成多个,这里可以传入String[]

        mpg.setStrategy(strategy);
        //不生成controller
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setController("");
        mpg.setTemplate(templateConfig);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);

        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }


}
