package KADAN.interview.demo.service.impl;

import KADAN.interview.demo.converter.dto.MaskDto;
import KADAN.interview.demo.converter.dto.PharmacyDto;
import KADAN.interview.demo.repository.MaskRepository;
import KADAN.interview.demo.repository.PharmacyRepository;
import KADAN.interview.demo.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
	private final PharmacyRepository pharmacyRepository;
	private final MaskRepository maskRepository;

	@Override
	public List<?> searchAll(String word, Boolean type) {
		final String keyword = "+" + word + "*";
		// type 未輸入預設為藥局，否則查詢口罩
		if (type == null || type) {
			return pharmacyRepository
					.searchByName(keyword)
					.stream()
					.map(
							data -> {
								PharmacyDto dto = new PharmacyDto();
								dto.setName(data.getName());
								dto.setCashBalance(data.getCashBalance());
								return dto;
							}
					)
					.toList();
		} else {
			return maskRepository
					.searchByName(keyword)
					.stream()
					.map(
							data -> {
								MaskDto dto = new MaskDto();
								dto.setName(data.getName());
								dto.setPrice(data.getPrice());
								dto.setPackSize(data.getPackSize());
								return dto;
							}
					)
					.toList();
		}
	}
}
