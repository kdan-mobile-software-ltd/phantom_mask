package KADAN.interview.demo.service.impl;

import KADAN.interview.demo.converter.vo.PharmacyVo;
import KADAN.interview.demo.converter.vo.UsersVo;
import KADAN.interview.demo.entity.*;
import KADAN.interview.demo.repository.*;
import KADAN.interview.demo.service.DataLoaderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Time;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Service
@RequiredArgsConstructor
public class DataLoaderServiceImpl implements DataLoaderService {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private final PharmacyRepository pharmacyRepository;
	private final MaskRepository maskRepository;
	private final PharmacyMaskInventoryRepository inventoryRepository;
	private final OpeningTimeRepository openingTimeRepository;
	private final UsersRepository usersRepository;
	private final TransactionHistoryRepository historyRepository;

	// 本地測試用 implements CommandLineRunner
	//	@Transactional
//	@Override
//	public void run(String... args) {
//		try {
//			clearAllTables();
//			loadPharmacies();
//			loadUsers();
//			System.out.println("✅ InitData executed after SpringBoot startup!");
//		} catch (Exception e) {
//			System.out.println("===> InitData Failed:" + e.getMessage());
//			e.printStackTrace();
//		}
//	}

	@Transactional
	@Override
	public ResponseEntity<Object> loadData() {
		try {
			clearAllTables();
			loadPharmacies();
			loadUsers();
			return ResponseEntity.ok("InitData Success!");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("InitData Failed: " + e.getMessage());
		}
	}

	private void clearAllTables() {
		LOGGER.info("===== Clearing all tables =====");
		openingTimeRepository.deleteAll();
		historyRepository.deleteAll();
		maskRepository.deleteAll();
		usersRepository.deleteAll();
		pharmacyRepository.deleteAll();
		LOGGER.info("===== Finished Clear all tables =====");
	}

	private void loadPharmacies() throws IOException {
		LOGGER.info("===== Start Loading pharmacies =====");
		File pharmaciesFile = ResourceUtils.getFile("classpath:data/pharmacies.json");
		JSONArray pharmacies = new JSONArray(new String(Files.readAllBytes(pharmaciesFile.toPath())));
		for (Object e : pharmacies) {
			JSONObject obj = new JSONObject(e.toString());
			PharmacyVo vo = new PharmacyVo(obj);
			Pharmacy pharmacy = new Pharmacy();
			pharmacy.setName(vo.getName());
			pharmacy.setCashBalance(vo.getCashBalance());
			Pharmacy savedPharmacy = pharmacyRepository.save(pharmacy);

			vo.getMasks().forEach(
					maskVo -> {
						String name = maskVo.getName();
						Mask mask = new Mask();
						mask.setName(name);
						mask.setPrice(maskVo.getPrice());
						maskRepository.save(mask);

						PharmacyMaskInventory inventory = new PharmacyMaskInventory();
						inventory.setPharmacy(savedPharmacy);
						inventory.setMask(mask);
						inventory.setQuantity(extractPackSize(name));
						inventoryRepository.save(inventory);
					});

			vo.getOpeningHours()
					.forEach(
							timeVo -> {
								OpeningTime oh = new OpeningTime();
								oh.setPharmacy(savedPharmacy);
								oh.setWeekDay(timeVo.getWeekDay());
								oh.setStartTime(Time.valueOf(timeVo.getStartTime()));
								oh.setEndTime(Time.valueOf(timeVo.getEndTime()));
								openingTimeRepository.save(oh);
							});
		}
	}

	private void loadUsers() throws IOException {
		LOGGER.info("===== Start Loading users =====");
		File usersFile = ResourceUtils.getFile("classpath:data/users.json");
		JSONArray users = new JSONArray(new String(Files.readAllBytes(usersFile.toPath())));
		for (Object e : users) {
			JSONObject obj = new JSONObject(e.toString());
			UsersVo vo = new UsersVo(obj);
			Users user = new Users();
			user.setName(vo.getName());
			user.setCashBalance(vo.getCashBalance());
			Users savedUsers = usersRepository.save(user);

			vo.getHistories()
					.forEach(
							historyVo -> {
								String pharmacyName = historyVo.getPharmacyName();
								Pharmacy pharmacy = pharmacyRepository.findByName(pharmacyName)
										.orElseThrow(
												() -> new EntityNotFoundException("Pharmacy not found: " + pharmacyName));

								TransactionHistory history = new TransactionHistory();
								history.setUsers(savedUsers);
								history.setPharmacy(pharmacy);
								history.setMaskName(historyVo.getMaskName());
								history.setTransactionAmount(historyVo.getTransactionAmount());
								history.setTransactionDate(historyVo.getTransactionDate());
								historyRepository.save(history);
							});
		}
	}

	private int extractPackSize(String name) {
		Pattern pattern = Pattern.compile("\\((\\d+) per pack\\)");
		Matcher matcher = pattern.matcher(name);
		if (matcher.find()) {
			return Integer.parseInt(matcher.group(1));
		} else {
			throw new IllegalArgumentException("Cannot extract pack size from mask name: " + name);
		}
	}
}

