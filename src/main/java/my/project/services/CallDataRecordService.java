package my.project.services;

import jakarta.annotation.PostConstruct;
import my.project.entity.CallDataRecord;
import my.project.entity.Subscriber;
import my.project.repositories.CallDataRecordRepository;
import my.project.repositories.SubscriberRepository;
import my.project.util.IncorrectPhoneNumberException;
import my.project.util.NoDataToReceiveException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Сервис для работы с записями данных о вызовах.
 * <p>
 * Этот сервис отвечает за создание, хранение и генерацию отчетов о вызовах
 * для абонентов. Он также инициализирует данные абонентов и генерирует
 * случайные номера телефонов.
 * </p>
 */
@Service
@Transactional
public class CallDataRecordService {

	private final Random random = new Random();

	private static final int NUMBER_OF_CALLER = 21;
	private static final int MAX_CALLS_PER_DAY = 21;

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	private final SubscriberRepository subscriberRepository;
	private final CallDataRecordRepository callDataRecordRepository;

	/**
	 * Конструктор класса CallDataRecordService.
	 *
	 * @param subscriberRepository     Репозиторий для работы с абонентами.
	 * @param callDataRecordRepository Репозиторий для работы с записями данных о вызовах.
	 */
	public CallDataRecordService(SubscriberRepository subscriberRepository,
								 CallDataRecordRepository callDataRecordRepository) {
		this.subscriberRepository = subscriberRepository;
		this.callDataRecordRepository = callDataRecordRepository;
	}

	/**
	 * Инициализация данных при создании сервиса.
	 * <p>
	 * Удаляет все предыдущие записи данных о вызовах и генерирует новые записи.
	 * </p>
	 */
	@PostConstruct
	private void generate() {
		callDataRecordRepository.deleteAll();
		generateCallDataRecords();
	}

	/**
	 * Получает все записи данных вызовов (CDR) из репозитория.
	 * <p>
	 * Этот метод извлекает все записи данных вызовов из {@code callDataRecordRepository}.
	 * Если записи отсутствуют, выбрасывается исключение {@code NoDataToReceiveException}.
	 *
	 * @return Список объектов {@code CallDataRecord}, содержащий все записи данных вызовов.
	 * @throws NoDataToReceiveException если записи данных вызовов не найдены.
	 */
	public List<CallDataRecord> getAllCallDataRecords() {
		List<CallDataRecord> callDataRecords = callDataRecordRepository.findAll();
		if (callDataRecords.isEmpty()) {
			throw new NoDataToReceiveException();
		}
		return callDataRecords;
	}


	/**
	 * Генерирует отчет о вызовах для указанного номера телефона за заданный период.
	 *
	 * @param msisdn    Номер телефона абонента (должен содержать 11 цифр).
	 * @param startDate Дата начала периода в формате "yyyy-MM-dd".
	 * @param endDate   Дата окончания периода в формате "yyyy-MM-dd".
	 * @return UUID файла отчета.
	 * @throws IOException                   Если произошла ошибка при записи файла.
	 * @throws IncorrectPhoneNumberException Если номер телефона не соответствует формату.
	 * @throws NoDataToReceiveException      Если нет данных для генерации отчета.
	 */
	public String generateReport(String msisdn, String startDate, String endDate) throws IOException {
		LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00", FORMATTER);
		LocalDateTime end = LocalDateTime.parse(endDate + "T00:00:00", FORMATTER);
		if (msisdn.length() != 11) {
			throw new IncorrectPhoneNumberException();
		}
		List<CallDataRecord> records = callDataRecordRepository.findByMsisdnAndDateRange(msisdn, start, end);
		if (records.isEmpty()) {
			throw new NoDataToReceiveException();
		}
		String uuid = UUID.randomUUID().toString();
		String fileName = String.format("%s_%s.csv", msisdn, uuid);
		String directoryPath = "src/main/java/my/project/reports";
		String filePath = Paths.get(directoryPath, fileName).toString();

		File directory = new File(directoryPath);
		if (!directory.exists()) {
			directory.mkdir();
		}
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			for (CallDataRecord record : records) {
				writer.write(String.join(",",
						record.getCallType(),
						record.getPhoneOne(),
						record.getPhoneTwo(),
						record.getStartTime().toString(),
						record.getEndTime().toString()));
				writer.newLine();
				writer.newLine();
			}
		}

		return uuid;
	}

	/**
	 * Инициализирует список абонентов со случайными номерами телефонов.
	 *
	 * @return Список сохраненных абонентов.
	 */
	private List<Subscriber> initializeSubscribers() {
		List<Subscriber> subscribers = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_CALLER; i++) {
			StringBuilder phoneNumber = new StringBuilder("7");
			for (int j = 0; j < 10; j++) {
				phoneNumber.append(random.nextInt(10));
			}
			subscribers.add(new Subscriber(phoneNumber.toString()));
		}
		return subscriberRepository.saveAll(subscribers);
	}

	/**
	 * Генерирует записи данных о вызовах для абонентов.
	 * <p>
	 * Метод создает случайные записи о вызовах на основе существующих абонентов.
	 * Если абоненты отсутствуют, они инициализируются. Записи о вызовах создаются
	 * на основе случайных значений для времени начала и окончания вызова в пределах
	 * заданного периода.
	 * </p>
	 */
	public void generateCallDataRecords() {
		List<Subscriber> subscribers = subscriberRepository.count() == 0 ?
				initializeSubscribers() : subscriberRepository.findAll();
		Optional<CallDataRecord> optionalCDR = callDataRecordRepository.findFirstByOrderByEndTimeDesc();
		LocalDate startDateTime;
		if (optionalCDR.isPresent()) {
			startDateTime = optionalCDR.get().getEndTime().toLocalDate();
		} else {
			startDateTime = generateRandomDate();
		}
		int numberOfMonthToGenerate = 12;
		LocalDate endDateTime = startDateTime.plusMonths(numberOfMonthToGenerate);

		for (LocalDate currentDate = startDateTime; currentDate.isBefore(endDateTime); currentDate = currentDate.plusDays(1)) {
			int callsToday = random.nextInt(1, MAX_CALLS_PER_DAY);
			for (int i = 0; i < callsToday; i++) {

				String caller = getRandomSubscriber(subscribers);
				String receiver = getRandomSubscriber(subscribers);
				while (caller.equals(receiver)) {
					receiver = getRandomSubscriber(subscribers);
				}

				LocalTime callStartTime = LocalTime.of(
						random.nextInt(0, 24),
						random.nextInt(0, 60),
						random.nextInt(0, 60));
				LocalTime callEndTime = callStartTime.plusSeconds(random.nextInt(30, 600));

				LocalDate endDateOfCall = callEndTime.isBefore(callStartTime)
						? currentDate = currentDate.plusDays(1)
						: currentDate;

				CallDataRecord record = new CallDataRecord(
						random.nextBoolean() ? "01" : "02",
						caller,
						receiver,
						LocalDateTime.of(currentDate, callStartTime),
						LocalDateTime.of(endDateOfCall, callEndTime)
				);
				callDataRecordRepository.save(record);
			}
		}
	}

	/**
	 * Генерирует случайную дату в диапазоне с 1980 по 2025 год.
	 *
	 * @return Случайно сгенерированная дата.
	 */
	public LocalDate generateRandomDate() {
		int year = random.nextInt(1980, 2025);
		int month = random.nextInt(1, 13);
		int day;

		switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				day = random.nextInt(1, 32);
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				day = random.nextInt(1, 31);
				break;
			case 2:
				if (isLeapYear(year)) {
					day = random.nextInt(1, 30);
				} else {
					day = random.nextInt(1, 29);
				}
				break;
			default:
				day = random.nextInt(1, 29);
		}

		return LocalDate.of(year, month, day);
	}

	/**
	 * Проверяет, является ли указанный год високосным.
	 *
	 * @param year Год для проверки.
	 * @return true, если год високосный; false в противном случае.
	 */
	private boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}

	/**
	 * Получает случайный номер телефона абонента из списка абонентов.
	 *
	 * @param subscribers Список абонентов.
	 * @return Случайный номер телефона абонента.
	 */
	private String getRandomSubscriber(List<Subscriber> subscribers) {
		return subscribers.get(random.nextInt(subscribers.size())).getMsisdn();
	}
}

