package com.mycompany.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

import com.mycompany.myapp.domain.CalendarUser;
import com.mycompany.myapp.domain.Event;
import com.mysql.jdbc.Statement;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
@Repository
public class JdbcEventDao implements EventDao {
    private DataSource dataSource;
    
    private JdbcCalendarUserDao cal_user;
    // --- constructors ---
    public JdbcEventDao(JdbcCalendarUserDao cal_user) {
    	this.cal_user=cal_user;
    }
    //의존관계 주입. EventDao는 CalendarUserDao에서 CalendarUser 객체만 받으면 됨. 방법은 CalendarUserDao에서 결정.
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}

    // --- EventService ---
    @Override
    public Event getEvent(int eventId) {
		Event event = new Event();
		CalendarUser owner = new CalendarUser();
		CalendarUser attendee = new CalendarUser();
		
    	try{
    	Connection c = dataSource.getConnection();	
    	
		PreparedStatement ps = c.prepareStatement( "select * from events where id = ?");		
		ps.setInt(1, eventId);
				
		ResultSet rs = ps.executeQuery();
		
		if( rs.next()) {
			Calendar cal = Calendar.getInstance();
			cal.clear();
			cal.setTimeInMillis(rs.getTimestamp("when").getTime());
						 
			//calendar형 으로 변환

			owner=cal_user.getUser(rs.getInt("owner"));
			attendee=cal_user.getUser(rs.getInt("attendee"));
			//CalendarUserDao에서 CalendarUser를 받아옴!
			
			event.setId(rs.getInt("id"));
			event.setWhen(cal);
			event.setSummary(rs.getString("summary"));
			event.setDescription(rs.getString("description"));		
			event.setOwner(owner);
			event.setAttendee(attendee);			
		}
		
		rs.close();
		ps.close();
		c.close();
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
		   	
		return event;    	
    }

    @Override
    public int createEvent(final Event event) {
		int auto_id= -1;
		//등록 후 받은 id값을 반환
    	try{
    	Connection c = dataSource.getConnection();

		PreparedStatement ps = c.prepareStatement
				("insert into events (`when`, summary, description, owner, attendee) values (?,?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS); 
	
		Date date = event.getWhen().getTime();
		Timestamp timestamp = new Timestamp(date.getTime());

		//Calender에서 Date형 변환후 Timestamp형으로
		ps.setTimestamp(1, timestamp);
		ps.setString(2, event.getSummary());
		ps.setString(3, event.getDescription());
		ps.setInt(4, event.getOwner().getId());
		ps.setInt(5, event.getAttendee().getId());

		ps.executeUpdate();

		ResultSet rs = ps.getGeneratedKeys();

		if(rs.next()){
			auto_id=rs.getInt(1);
		}
		
		rs.close();
		ps.close();
		c.close();
		}catch(SQLException e){
    		e.printStackTrace();
    	}
		   	
    	return auto_id;
    }

    @Override
    public List<Event> findForUser(int userId) {
    	List<Event> Events= new ArrayList<Event>();
		CalendarUser owner = new CalendarUser();
		CalendarUser attendee = new CalendarUser();
		
    	try{
    		Connection c = dataSource.getConnection();	

    		PreparedStatement ps = c.prepareStatement("select * from events where id ?");		
    		ps.setInt(1, userId);
    		
    		ResultSet rs = ps.executeQuery();

    		while(rs.next()) {
    			Calendar cal = Calendar.getInstance(); 
    			Event event=new Event(); 
    			cal.clear();
    			cal.setTimeInMillis(rs.getTimestamp("when").getTime());
    			owner=cal_user.getUser(rs.getInt("owner"));
    			attendee=cal_user.getUser(rs.getInt("attendee"));
    			
    			event.setId(rs.getInt("id"));
    			event.setWhen(cal);
    			event.setSummary(rs.getString("summary"));
    			event.setDescription(rs.getString("description"));
    			event.setOwner(owner);
    			event.setAttendee(attendee);
    			Events.add(event);
    		}
    		
    		rs.close();
    		ps.close();
    		c.close();
    	}catch(SQLException e){
    		e.printStackTrace();	
    	}
	   	
    	return null;
    }

    @Override
    public List<Event> getEvents() {
    	List<Event> Events= new ArrayList<Event>();
    	 
		CalendarUser owner = new CalendarUser();
		CalendarUser attendee = new CalendarUser();
		
    	try{
    		Connection c = dataSource.getConnection();	

    		PreparedStatement ps = c.prepareStatement( "select * from events");		
    		ResultSet rs = ps.executeQuery();

    		while(rs.next()) {
    			Calendar cal = Calendar.getInstance();
    			Event event=new Event();
    			cal.clear();
    			cal.setTimeInMillis(rs.getTimestamp("when").getTime());
    			owner=cal_user.getUser(rs.getInt("owner"));
    			attendee=cal_user.getUser(rs.getInt("attendee"));
 
    			event.setId(rs.getInt("id"));
    			event.setWhen(cal);
    			event.setSummary(rs.getString("summary"));
    			event.setDescription(rs.getString("description"));
    			event.setOwner(owner);
    			event.setAttendee(attendee);
    			Events.add(event);
    		}

    		rs.close();
    		ps.close();
    		c.close();
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    	
		return Events;
    }

    /*
    private static final String EVENT_QUERY = "select e.id, e.summary, e.description, e.when, " +
            "owner.id as owner_id, owner.email as owner_email, owner.password as owner_password, owner.name as owner_name, " +
            "attendee.id as attendee_id, attendee.email as attendee_email, attendee.password as attendee_password, attendee.name as attendee_name " +
            "from events as e, calendar_users as owner, calendar_users as attendee " +
            "where e.owner = owner.id and e.attendee = attendee.id";
     */
}
