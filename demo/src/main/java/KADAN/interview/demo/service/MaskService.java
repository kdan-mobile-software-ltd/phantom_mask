package KADAN.interview.demo.service;

import KADAN.interview.demo.converter.PurchaseRequest;
import KADAN.interview.demo.converter.dto.TransactionDto;
import KADAN.interview.demo.converter.dto.TransactionSummaryDto;

import java.util.Date;

public interface MaskService {
	TransactionSummaryDto getMaskTransactionSummary(Date startDate, Date endDate);

	TransactionDto processPurchase(PurchaseRequest request);
}
