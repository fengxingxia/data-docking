package com.data.docking.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @author chenjianhui
 * @version V1.0
 * @description 主数据源配置
 * @since 2021/3/23
 */
@Configuration
@MapperScan(basePackages = "com.**.mapper.primary", sqlSessionTemplateRef = "primarySqlSessionTemplate")
public class PrimaryDataSourceConfig {

    /**
     * 第三方数据源
     * @return
     */
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource odcDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 第三方数据源sql session factory
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "primarySqlSessionFactory")
    public SqlSessionFactory odcSqlSessionFactory(@Qualifier("primaryDataSource") DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(
                "classpath*:/mapper/primary/*.xml"));
        SqlSessionFactory sessionFactory = bean.getObject();
        if (sessionFactory != null) {
            sessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
        }
        return bean.getObject();
    }

    /**
     * 第三方数据源事务管理器
     *
     * @param dataSource
     * @return
     */
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager odcTransactionManager(@Qualifier("primaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 第三方数据源SQL template
     *
     * @param sqlSessionFactory
     * @return
     */
    @Bean(name = "primarySqlSessionTemplate")
    public SqlSessionTemplate visualSqlSessionTemplate(@Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
