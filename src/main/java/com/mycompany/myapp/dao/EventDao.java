package com.mycompany.myapp.dao;

import java.util.List;

import com.mycompany.myapp.domain.Event;

public interface EventDao {

    Event getEvent(int eventId);

    int createEvent(Event event);

    List<Event> findForUser(int userId);

    List<Event> getEvents();
}