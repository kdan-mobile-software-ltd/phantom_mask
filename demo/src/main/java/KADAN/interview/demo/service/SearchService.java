package KADAN.interview.demo.service;

import KADAN.interview.demo.enumType.SearchTarget;

import java.util.List;
import java.util.Map;

public interface SearchService {
	List<Map<String, String>> searchAll(String keyword, SearchTarget type);
}
