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
@MapperScan(basePackages = "com.**.mapper.second", sqlSessionTemplateRef = "secondSqlSessionTemplate")
public class SecondDataSourceConfig {

    /**
     * dahua数据源
     *
     * @return
     */
    @Bean(name = "secondDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.second")
    public DataSource odcDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * dahua数据源SQL session factory
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "secondSqlSessionFactory")
    public SqlSessionFactory odcSqlSessionFactory(@Qualifier("secondDataSource") DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(
                "classpath*:/mapper/second/*.xml"));
        SqlSessionFactory sessionFactory = bean.getObject();
        if (sessionFactory != null) {
            sessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
        }
        return bean.getObject();
    }

    /**
     * dahua数据与事务管理器
     *
     * @param dataSource
     * @return
     */
    @Bean(name = "secondTransactionManager")
    public PlatformTransactionManager odcTransactionManager(@Qualifier("secondDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * dahua数据源 sql template
     * @param sqlSessionFactory
     * @return
     */
    @Bean(name = "secondSqlSessionTemplate")
    public SqlSessionTemplate visualSqlSessionTemplate(@Qualifier("secondSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
