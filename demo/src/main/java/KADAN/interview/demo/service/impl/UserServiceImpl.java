package KADAN.interview.demo.service.impl;

import KADAN.interview.demo.converter.dto.UserDto;
import KADAN.interview.demo.repository.TransactionHistoryRepository;
import KADAN.interview.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final TransactionHistoryRepository transactionHistoryRepository;

	@Override
	public List<UserDto> getTopUsers(Date startDate, Date endDate, int top) {
		PageRequest page = PageRequest.of(0, top);
		return transactionHistoryRepository.findTopUsersByTransactionBetween(startDate, endDate, page);
	}
}
