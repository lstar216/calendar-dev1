package com.mycompany.myapp.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;


@Configuration
public class DaoFactory {
	@Bean
	public JdbcEventDao eventDao() {
		JdbcEventDao eventDao = new JdbcEventDao(calendarDao());
		eventDao.setDataSource(dataSource());
		return eventDao;
	}
	//DB연결을 담당하는 DataSource와 CalenderUser를 관리해주는  CalendarUserDao를 DI
	@Bean
	public JdbcCalendarUserDao calendarDao() {
		JdbcCalendarUserDao calendarDao = new JdbcCalendarUserDao();
		calendarDao.setDataSource(dataSource());
		return calendarDao;
	}
	//DB연결을 담당하는  DataSource를 DI
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/calendar");
		dataSource.setUsername("spring");
		dataSource.setPassword("book");
		//JDBC 연결
		return dataSource;
	}
}
