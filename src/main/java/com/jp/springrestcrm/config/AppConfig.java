package com.jp.springrestcrm.config;

import java.beans.PropertyVetoException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages="com.jp.springrestcrm")
@PropertySource({ "classpath:persistence-mysql.properties" })
public class AppConfig implements WebMvcConfigurer {

	@Autowired
	private Environment env;
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Bean
	public DataSource securityDataSource() {
		
		// create connection pool
		ComboPooledDataSource securityDataSource = new ComboPooledDataSource();
		
		// set the jdbc Driver
		
		try {
			securityDataSource.setDriverClass(env.getProperty("jdbc.driver"));
		} catch (PropertyVetoException e) {
			throw new RuntimeException (e);
			
		}
		
		// log url and user to make sure we are rading db
		
		logger.info(">>>>>>>> jdbc.url" + env.getProperty("jdbc.url"));
		logger.info(">>>>>>>> jdbc.user" + env.getProperty("jdbc.user"));
		
		// set database connection props
		
		securityDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
		securityDataSource.setUser(env.getProperty("jdbc.user"));
		securityDataSource.setPassword(env.getProperty("jdbc.password"));
				
		// set connection pool props
		securityDataSource.setInitialPoolSize(Integer.parseInt(env.getProperty("connection.pool.initialPoolSize")));
		securityDataSource.setMinPoolSize(Integer.parseInt(env.getProperty("connection.pool.minPoolSize")));
		securityDataSource.setMaxPoolSize(Integer.parseInt(env.getProperty("connection.pool.maxPoolSize")));
		securityDataSource.setMaxIdleTime(Integer.parseInt(env.getProperty("connection.pool.maxIdleTime")));
		
		return securityDataSource;
	}
	
	private Properties getHibernateProperties() {
		
		Properties props = new Properties();
		
		props.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
		props.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		
		return props;
	}
	
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		
		// create session factory
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		
		// set the properties 
		sessionFactory.setDataSource(securityDataSource());
		sessionFactory.setPackagesToScan(env.getProperty("hibernate.packagesToScan"));
		sessionFactory.setHibernateProperties(getHibernateProperties());
		
		return sessionFactory;
	}
	
	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		
		// setup transcation manager based on session factory 
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory);
		
		return txManager;
		
	}
	
}
