package KADAN.interview.demo.service;

import org.springframework.http.ResponseEntity;

public interface DataLoaderService {
	ResponseEntity<Object> loadData();
}
