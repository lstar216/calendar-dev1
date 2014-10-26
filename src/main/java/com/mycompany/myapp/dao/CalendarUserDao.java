package com.mycompany.myapp.dao;

import java.util.List;

import com.mycompany.myapp.domain.CalendarUser;

public interface CalendarUserDao {
    CalendarUser getUser(int id);

    CalendarUser findUserByEmail(String email);

    List<CalendarUser> findUsersByEmail(String partialEmail);

    int createUser(CalendarUser user);
}
