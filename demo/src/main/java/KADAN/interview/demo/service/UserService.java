package KADAN.interview.demo.service;

import KADAN.interview.demo.converter.dto.UserDto;

import java.util.Date;
import java.util.List;

public interface UserService {
	List<UserDto> getTopUsers(Date startDate, Date endDate, int top);
}
