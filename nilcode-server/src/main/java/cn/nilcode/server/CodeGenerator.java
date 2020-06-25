/*
package cn.nilcode.server;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

*/
/**
 * 演示例子
 * 执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
 * 常用配置已提取为参数
 *//*

public class CodeGenerator {
    */
/**
     * 代码生成器
     *//*

    private static AutoGenerator mpg = new AutoGenerator();

    public static void main(String[] args) {

        String[] dataConfig = {
                "mysql",
                "47.100.97.72",
                "6603",
                "nilcode",
                //               default: com.mysql.jdbc.Driver
                "driverName",
                "nilcode",
                "nilcode"
        };
        String authorName = "zero";
        // 此处不要留空 否则生成文件包路径以 . 开头
        String parentPackage = "cn.nilcode";
        //        全局配置
        String projectPath = globalConfig(authorName);
        //        数据源配置
        dataSourceConfig(dataConfig);
        //         包配置
        PackageConfig pc = packageConfig(parentPackage);
        //         自定义配置
        injectionConfig(projectPath, pc);
        //        模板配置
        templateConfig();
        //        策略配置
        strategyConfig(pc);
    }

    private static void injectionConfig(String projectPath, PackageConfig pc) {
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        //         如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        //        如果模板引擎是 velocity
        //         String templatePath = "/templates/mapper.xml.vm";
        //         自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        //         自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                //                 自定义输出文件名 ,  如果你 Entity 设置了前后缀, 此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

             */
/*
             cfg.setFileCreate(new IFileCreate() {
                 @Override
                 public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                     // 判断自定义文件夹是否需要创建
                     checkDir("调用默认方法创建的目录");
                     return false;
                 }
             });
             *//*

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
    }

    private static void templateConfig() {
        //         配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        //         配置自定义输出模板
        //        指定自定义模板路径, 注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        //         templateConfig.setEntity("templates/entity2.java");
        //         templateConfig.setService();
        //         templateConfig.setController();

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);
    }

    private static void strategyConfig(PackageConfig pc) {
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //表名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //列名生成策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //        自定义实体公共父类
        //        strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        //         自定义Controller父类
        //        strategy.setSuperControllerClass("com.baomidou.ant.common.BaseController");
        //         写于父类中的公共字段
        strategy.setSuperEntityColumns("id");
        //        根据 表 生成
        strategy.setInclude(scanner("表名, 多个英文逗号分割(根据 表 生成)").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

    */
/**
     * 读取控制台内容
     *//*

    private static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入").append(tip).append("：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    private static PackageConfig packageConfig(String parentPackage) {
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(scanner("模块名"));
        pc.setParent(parentPackage);
        mpg.setPackageInfo(pc);
        return pc;
    }

    private static void dataSourceConfig(String[] dataConfig) {
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();

        dsc.setUrl(
                new StringBuffer()
                        .append("jdbc:")
                        //                db
                        .append(dataConfig[0])
                        .append("://")
                        //                ip
                        .append(dataConfig[1])
                        .append(":")
                        //                port
                        .append(dataConfig[2])
                        .append("/")
                        //                dbName
                        .append(dataConfig[3])
                        .append("?useUnicode=true&useSSL=false&characterEncoding=utf8")
                        .toString()
        );

        //         dsc.setSchemaName("public");
        dsc.setDriverName(("".equals(dataConfig[4]) || "driverName".equals(dataConfig[4])) ?
                "com.mysql.jdbc.Driver" :
                dataConfig[4]);
        dsc.setUsername(dataConfig[5]);
        dsc.setPassword(dataConfig[6]);
        mpg.setDataSource(dsc);
    }

    private static String globalConfig(String authorName) {
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        //        项目目录
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(authorName);
        gc.setOpen(false);
        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);
        return projectPath;
    }
}*/
