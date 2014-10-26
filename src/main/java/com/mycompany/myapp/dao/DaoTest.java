package com.mycompany.myapp.dao;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.mycompany.myapp.domain.CalendarUser;
import com.mycompany.myapp.domain.Event;


public class DaoTest {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		
		CalendarUserDao calendarUserDao = context.getBean("calendarDao", JdbcCalendarUserDao.class);
		EventDao eventDao = context.getBean("eventDao", JdbcEventDao.class);
		
		//1. 디폴트로 등록된 CalendarUser 3명 출력 (패스워드 제외한 모든 내용 출력)
		System.out.println("1. 디폴트로 등록된 CalendarUser 3명 출력 (패스워드 제외한 모든 내용 출력)");
		CalendarUser query = new CalendarUser();
		
		for(int i = 0; i < 3; i++){
			query=calendarUserDao.getUser(i+1);
			System.out.println((i+1) + "번째 User");
			System.out.println("id : " + query.getId());
			System.out.println("name : " + query.getName());
			System.out.println("email : " + query.getEmail());
		}
		//2. 디폴트로 등록된 Event 3개 출력 (owner와 attendee는 해당 사용자의 이메일과 이름을 출력) 
		System.out.println("\n2. 디폴트로 등록된 Event 3개 출력 (owner와 attendee는 해당 사용자의 이메일과 이름을 출력)");
		List<Event> events = eventDao.getEvents();
		
		for(int i = 0; i < events.size(); i++){
			System.out.println((i+1) + "번째 Event");
			System.out.println("id : " + events.get(i).getId());
			System.out.println("when : " + events.get(i).getWhen().getTime());
			System.out.println("summary : " + events.get(i).getSummary());
			System.out.println("description : " + events.get(i).getDescription());
			System.out.println("owner's name : " + events.get(i).getOwner().getName());
			System.out.println("owner's email : " + events.get(i).getOwner().getEmail());
			System.out.println("attendee's name : " + events.get(i).getAttendee().getName());
			System.out.println("attendee's email : " + events.get(i).getAttendee().getEmail());
		}
		//3. 새로운 CalendarUser 2명 등록 및 각각 id 추출
		System.out.println("\n3. 새로운 CalendarUser 2명 등록 및 각각 id 추출");
		
		CalendarUser account1 = new CalendarUser();
		account1.setName("Heesung");
		account1.setPassword("0000");
		account1.setEmail("lstar216@naver.com");
		
		CalendarUser account2 = new CalendarUser();
		account2.setName("YunJo");
		account2.setPassword("abcd1234");
		account2.setEmail("younjo@nate.com");
		
		int id1 = calendarUserDao.createUser(account1);
		System.out.println("account1 등록 성공");
		int id2 = calendarUserDao.createUser(account2);
		System.out.println("account2 등록 성공");
		account1.setId(id1);
		account2.setId(id2);
		
		System.out.println("등록 id 1 : " + id1);
		System.out.println("등록 id 2 : " + id2);
		//4. 추출된 id와 함께 새로운 CalendarUser 2명을 DB에서 가져와 (getUser 메소드 사용) 방금 등록된 2명의 사용자와 내용 (이메일, 이름, 패스워드)이 일치하는 지 비교
		System.out.println("\n4. 추출된 id와 함께 새로운 CalendarUser 2명을 DB에서 가져와 (getUser 메소드 사용) 방금 등록된 2명의 사용자와 내용 (이메일, 이름, 패스워드)이 일치하는 지 비교");
		
		CalendarUser newuser1 = calendarUserDao.getUser(id1);
		CalendarUser newuser2 = calendarUserDao.getUser(id2);
		
		System.out.println("account1.getName().equals(newuser1.getName()) : " + account1.getName().equals(newuser1.getName()));
		System.out.println("account1.getEmail().equals(newuser1.getEmail()) : " + account1.getEmail().equals(newuser1.getEmail()));
		System.out.println("account1.getPassword().equals(newuser1.getPassword()) : " + account1.getPassword().equals(newuser1.getPassword()));
		
		System.out.println("account2.getName().equals(newuser2.getName()) : " + account2.getName().equals(newuser2.getName()));
		System.out.println("account2.getEmail().equals(newuser2.getEmail()) : " + account2.getEmail().equals(newuser2.getEmail()));
		System.out.println("account2.getPassword().equals(newuser2.getPassword()) : " + account2.getPassword().equals(newuser2.getPassword()));
		//5. 5명의 CalendarUser 모두 출력 (패스워드 제외한 모든 내용 출력)
		System.out.println("\n5. 5명의 CalendarUser 모두 출력 (패스워드 제외한 모든 내용 출력)");	
		List<CalendarUser> users = new ArrayList<CalendarUser>();

		for(int i = 0; i < 5; i++){
			users.add(calendarUserDao.getUser(i+1));
			query = users.get(i);
			System.out.println("사용자 " + (i+1));
			System.out.println("id : " + query.getId());
			System.out.println("name : " + query.getName());
			System.out.println("email : " + query.getEmail());
		}
		//6. 새로운 Event 2개 등록 및 각각 id 추출
		System.out.println("\n6. 새로운 Event 2개 등록 및 각각 id 추출");	
		Event event1 = new Event();
		
	
		Calendar cal = Calendar.getInstance();
		cal.set(1989, 11, 11, 03, 00, 00);
		event1.setWhen(cal);
		event1.setSummary("My Birthday");
		event1.setDescription("I Born.");
		event1.setOwner(account1);
		event1.setAttendee(account1);

		int event_id1 = eventDao.createEvent(event1);
		System.out.println("event1 등록성공");
		
		Event event2 = new Event();
		cal.clear();
		cal.set(2014, 9, 19, 20, 00, 00);
		event2.setWhen(cal);
		event2.setSummary("End");
		event2.setDescription("Assignment End");
		event2.setOwner(account2);
		event2.setAttendee(account2);

		int event_id2 = eventDao.createEvent(event2);
		System.out.println("event2 등록성공");
		event1.setId(event_id1);
		event2.setId(event_id2);
		
		System.out.println("이벤트 id 1 : " + event_id1);
		System.out.println("이벤트 id 2 : " + event_id2);
		//7. 추출된 id와 함께 새로운 Event 2개를 DB에서 가져와 (getEvent 메소드 사용) 방금 추가한 2개의 이벤트와 내용 (when, summary, description, owner, attendee)이 일치하는 지 비교
		System.out.println("\n7. 추출된 id와 함께 새로운 Event 2개를 DB에서 가져와 (getEvent 메소드 사용) 방금 추가한 2개의 이벤트와 내용 (when, summary, description, owner, attendee)이 일치하는 지 비교");
		
		Event newevent1 = eventDao.getEvent(event_id1);
		Event newevent2 = eventDao.getEvent(event_id2);
		
		System.out.println("event1 Attendee equals newevent1: " + event1.getAttendee().equals(newevent1.getAttendee()));
		System.out.println("event1 Summary equals newevent1: " + event1.getSummary().equals(newevent1.getSummary()));
		System.out.println("event1 Description equals newevent1: " + event1.getDescription().equals(newevent1.getDescription()));
		System.out.println("event1 Owner equals newevent1: " + event1.getOwner().equals(newevent1.getOwner()));
		System.out.println("event1 When compareTo newevent1: " + event1.getWhen().compareTo(newevent1.getWhen()));
		
		System.out.println("event2 Attendee equals newevent2: " + event2.getAttendee().equals(newevent2.getAttendee()));
		System.out.println("event2 Summary equals newevent2: " + event2.getSummary().equals(newevent2.getSummary()));
		System.out.println("event2 Description equals newevent2: " + event2.getDescription().equals(newevent2.getDescription()));
		System.out.println("event2 Owner equals newevent2: " + event2.getOwner().equals(newevent2.getOwner()));
		System.out.println("event2 When compareTo newevent2: " + event2.getWhen().compareTo(newevent2.getWhen()));
		//8. 5개의 Event 모두 출력 (owner와 attendee는 해당 사용자의 이메일과 이름을 출력)
		System.out.println("8. 5개의 Event 모두 출력 (owner와 attendee는 해당 사용자의 이메일과 이름을 출력)");
		List<Event> all_events = eventDao.getEvents();
		
		for(int i = 0; i < all_events.size(); i++){
			System.out.println((i+1) + "번째 Event");
			System.out.println("id : " + all_events.get(i).getId());
			System.out.println("when : " + all_events.get(i).getWhen().getTime());
			System.out.println("summary : " + all_events.get(i).getSummary());
			System.out.println("description : " + all_events.get(i).getDescription());
			System.out.println("owner's name : " + all_events.get(i).getOwner().getName());
			System.out.println("owner's email : " + all_events.get(i).getOwner().getEmail());
			System.out.println("attendee's name : " + all_events.get(i).getAttendee().getName());
			System.out.println("attendee's email : " + all_events.get(i).getAttendee().getEmail());
		}
	}
}
