package com.ls.train.files_encryption.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * c3p0 for derby
 */
@Configuration
@EnableTransactionManagement
@ConfigurationProperties(prefix = "spring.datasource.derby")
@MapperScan(
        basePackages = {"com.ls.train.files_encryption.dao.derbydao"}
)
public class C3p0Config {

    @Value("${spring.datasource.derby.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.derby.url}")
    private String url;

    @Value("${spring.datasource.derby.username}")
    private String username;

    @Value("${spring.datasource.derby.password}")
    private String password;

    @Bean("derbyDataSource")
    public DataSource createDataSource() throws PropertyVetoException{
        // ComboPooledDataSource dataSource = DataSourceBuilder.create().type(ComboPooledDataSource.class).build();
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(driverClassName);
        dataSource.setJdbcUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setAutoCommitOnClose(false);//关闭自动提交
        return dataSource;
    }

    /**
     * 配置事物管理器
     *
     * @param dataSource
     * @return
     */
    @Bean(name = "transactionManagerDatabaseDerby")
    public PlatformTransactionManager transactionManagerDatabase(@Qualifier("derbyDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean("derbySqlSessionFactory")
    public SqlSessionFactory derbySqlSessionFactory(@Qualifier("derbyDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapping/derby/*.xml"));
        return bean.getObject();
    }

    @Primary
    @Bean("derbySqlSessionTemplate")
    public SqlSessionTemplate db1SqlSessionTemplate(@Qualifier("derbySqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
