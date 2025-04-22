package KADAN.interview.demo.service.impl;

import KADAN.interview.demo.enumType.SearchTarget;
import KADAN.interview.demo.repository.MaskRepository;
import KADAN.interview.demo.repository.PharmacyRepository;
import KADAN.interview.demo.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
	private final PharmacyRepository pharmacyRepository;
	private final MaskRepository maskRepository;

	@Override
	public List<Map<String, String>> searchAll(String word, SearchTarget type) {
		if (word == null || word.isBlank()) {
			throw new IllegalArgumentException("Search keyword must not be empty");
		}

		final String keyword = "+" + word + "*";

		if (SearchTarget.PHARMACY.equals(type)) {
			return pharmacyRepository
					.searchByName(keyword)
					.stream()
					.map(
							data -> Map.of(
									"type", "PHARMACY",
									"name", data.getName()
							))
					.toList();
		} else {
			return maskRepository
					.searchByName(keyword)
					.stream()
					.map(
							data -> Map.of(
									"type", "MASK",
									"name", data.getName()
							))
					.toList();
		}
	}
}
