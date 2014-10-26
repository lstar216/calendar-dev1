package com.mycompany.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import com.mycompany.myapp.domain.CalendarUser;
import com.mysql.jdbc.Statement;

@Repository
public class JdbcCalendarUserDao implements CalendarUserDao {

	private DataSource dataSource;

    // --- constructors ---
    public JdbcCalendarUserDao() {

    }
    	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}

    // --- CalendarUserDao methods ---
    @Override
    public CalendarUser getUser(int id) {
    	CalendarUser user = new CalendarUser();
    	//등록된 id값으로 사용자 검색
    	try{
    		Connection c = dataSource.getConnection();	

    		PreparedStatement ps = c.prepareStatement( "select * from calendar_users where id = ?");		
    		ps.setInt(1, id);

    		ResultSet rs = ps.executeQuery();

    		if( rs.next()) {
    			user.setId(rs.getInt("id"));
    			user.setName(rs.getString("name"));
    			user.setPassword(rs.getString("password"));
    			user.setEmail(rs.getString("email"));
    		}

    		rs.close();
    		ps.close();
    		c.close();
    	} catch(SQLException e){
    		e.printStackTrace();
    	}

		return user;    	
    }

    @Override
    public CalendarUser findUserByEmail(String email) {    	
    	CalendarUser user = new CalendarUser();
    	//email로 사용자 검색
    	try{
    		Connection c = dataSource.getConnection();	

    		PreparedStatement ps = c.prepareStatement( "select * from calendar_users where email = ?");		
    		ps.setString(1, email);

    		ResultSet rs = ps.executeQuery();

    		if( rs.next()) {
    			user.setId(rs.getInt("id"));
    			user.setName(rs.getString("name"));
    			user.setPassword(rs.getString("password"));
    			user.setEmail(rs.getString("email"));
    		}

    		rs.close();
    		ps.close();
    		c.close();
    	} catch (SQLException e){
    		e.printStackTrace();
    	}
		    	
		return user;  
    }

    @Override
    public List<CalendarUser> findUsersByEmail(String email) {
    	List<CalendarUser> Users= new ArrayList<CalendarUser>();
    	//like를 이용하여 문구가 들어간 사용자들 다수 검색해 객체리스트에 받음.
    	try{
    		Connection c = dataSource.getConnection();	
    		
    		PreparedStatement ps = c.prepareStatement( "select * from calendar_users where email like ?");		
    		ps.setString(1, "%" + email + "%");

    		ResultSet rs = ps.executeQuery();

    		while(rs.next()) {
    			CalendarUser user=new CalendarUser();
    			user = new CalendarUser();
    			user.setId(rs.getInt("id"));
    			user.setName(rs.getString("name"));
    			user.setPassword(rs.getString("password"));
    			user.setEmail(rs.getString("email"));
    			
    			Users.add(user);
    		}

    		rs.close();
    		ps.close();
    		c.close();		
    	} catch(SQLException e){
    		e.printStackTrace();
    	}
		return Users;
    }

    @Override
    public int createUser(final CalendarUser userToAdd) {
    	//이름 비밀번호 이메일을 통해 사용자를 등록하고 등록시 받은 아이디값을 반환
    	int auto_id = -1;
    	try{
    		Connection c = dataSource.getConnection();

    		PreparedStatement ps = c.prepareStatement("insert into calendar_users(name, password, email) values(?,?,?)",Statement.RETURN_GENERATED_KEYS);

    		ps.setString(1, userToAdd.getName()); 
    		ps.setString(2, userToAdd.getPassword()); 
    		ps.setString(3, userToAdd.getEmail()); 

    		ps.executeUpdate();

    		ResultSet rs = ps.getGeneratedKeys();

    		if(rs.next()){
    			auto_id=rs.getInt(1);
    		}

    		rs.close();
    		ps.close();
    		c.close();
    	} catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    	return auto_id;
    }
}