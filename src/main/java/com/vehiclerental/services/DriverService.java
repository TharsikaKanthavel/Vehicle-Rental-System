package com.vehiclerental.services;

import com.vehiclerental.models.User;
import java.util.List;

public interface DriverService {
    List<User> listDrivers(); // filter users who have DRIVER role
}
