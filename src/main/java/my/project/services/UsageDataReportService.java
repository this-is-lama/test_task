package my.project.services;

import my.project.entity.CallDataRecord;
import my.project.dto.IncomingCallDTO;
import my.project.dto.OutcomingCallDTO;
import my.project.dto.UsageDataReportDTO;
import my.project.repositories.CallDataRecordRepository;
import my.project.util.IncorrectPhoneNumberException;
import my.project.util.NoDataToReceiveException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Сервис для генерации отчетов об использовании данных по абонентам.
 * <p>
 * Этот сервис предоставляет методы для получения отчетов о входящих и исходящих вызовах
 * для заданного номера телефона (MSISDN) за указанный месяц или за все время.
 * </p>
 */
@Service
@Transactional(readOnly = true)
public class UsageDataReportService {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	private final CallDataRecordRepository callDataRecordRepository;

	/**
	 * Конструктор сервиса.
	 *
	 * @param callDataRecordRepository Репозиторий для работы с записями данных о вызовах.
	 */
	public UsageDataReportService(CallDataRecordRepository callDataRecordRepository) {
		this.callDataRecordRepository = callDataRecordRepository;
	}

	/**
	 * Получает отчет об использовании данных по заданному номеру телефона и месяцу.
	 *
	 * @param msisdn Номер телефона абонента (должен содержать 11 цифр).
	 * @param month  Месяц в формате "yyyy-MM" для отчета. Если null или пустой,
	 *               возвращает все записи для указанного номера.
	 * @return Отчет об использовании данных (входящие и исходящие вызовы).
	 * @throws IncorrectPhoneNumberException Если номер телефона имеет неверную длину.
	 * @throws NoDataToReceiveException Если нет данных для предоставления.
	 */
	public UsageDataReportDTO getUsageDataReportByMsisdnAndMonth(String msisdn, String month) {
		if (msisdn.length() != 11) {
			throw new IncorrectPhoneNumberException();
		}
		LocalDateTime start;
		LocalDateTime end;
		List<CallDataRecord> records;
		if (month == null || month.isEmpty()) {
			records = callDataRecordRepository.findByMsisdn(msisdn);
		} else {
			start = LocalDateTime.parse(month + "-01T00:00:00", FORMATTER);
			end = start.plusMonths(1).minusSeconds(1);
			records = callDataRecordRepository.findByMsisdnAndDateRange(msisdn, start, end);
		}
		if (records.isEmpty()) {
			throw new NoDataToReceiveException();
		}
		LocalTime incomingTime = LocalTime.of(0, 0, 0);
		LocalTime outcomingTime = LocalTime.of(0, 0, 0);
		for (CallDataRecord record : records) {
			Duration duration = Duration.between(record.getStartTime(), record.getEndTime());
			if (record.getCallType().equals("01")) {
				if (record.getPhoneOne().equals(msisdn)) {
					outcomingTime = outcomingTime.plus(duration);
				}
				if (record.getPhoneTwo().equals(msisdn)) {
					incomingTime = incomingTime.plus(duration);
				}
			}
			if (record.getCallType().equals("02")) {
				if (record.getPhoneOne().equals(msisdn)) {
					incomingTime = incomingTime.plus(duration);
				}
				if (record.getPhoneTwo().equals(msisdn)) {
					outcomingTime = outcomingTime.plus(duration);
				}
			}
		}
		return new UsageDataReportDTO(
				msisdn,
				new IncomingCallDTO(incomingTime),
				new OutcomingCallDTO(outcomingTime)
		);
	}

	/**
	 * Получает отчет об использовании данных по всем нашим абонентам за запрошенный месяц
	 *
	 * @param month Месяц в формате "yyyy-MM" для отчета.
	 * @return Список отчетов об использовании данных для всех абонентов за указанный месяц.
	 * @throws NoDataToReceiveException Если нет данных для предоставления.
	 */
	public List<UsageDataReportDTO> getUsageDataReportByMsisdn(String month) {
		LocalDateTime start = LocalDateTime.parse(month + "-01T00:00:00", FORMATTER);
		LocalDateTime end = start.plusMonths(1).minusSeconds(1);
		List<CallDataRecord> records = callDataRecordRepository.findAllByDateRange(start, end);
		Map<String, UsageDataReportDTO> map = new HashMap<>();
		for (var record : records) {
			String phoneOne = record.getPhoneOne();
			String phoneTwo = record.getPhoneTwo();
			Duration duration = Duration.between(record.getStartTime(), record.getEndTime());
			String type = record.getCallType();
			calculateTime(map, type, phoneOne, duration);
			type = type.equals("01") ? "02" : "01";
			calculateTime(map, type, phoneTwo, duration);
		}
		if (map.isEmpty()) {
			throw new NoDataToReceiveException();
		}
		return map.values().stream().toList();
	}

	/**
	 * Вычисляет время входящих и исходящих вызовов и обновляет отчет.
	 *
	 * @param map    Ассоциативный массив, содержащий отчеты об использовании данных по номерам телефонов.
	 * @param type   Тип вызова ("01" для исходящего, "02" для входящего).
	 * @param msisdn Номер телефона абонента.
	 * @param duration Продолжительность вызова.
	 */
	private void calculateTime(Map<String, UsageDataReportDTO> map, String type,String msisdn, Duration duration) {
		UsageDataReportDTO report;
		if (map.containsKey(msisdn)) {
			report = map.get(msisdn);
		} else {
			report = new UsageDataReportDTO(msisdn, new IncomingCallDTO(), new OutcomingCallDTO());
		}
		if (type.equals("01")) {
			report.plusOutcomingTime(duration);
		}
		if (type.equals("02")) {
			report.plusIncomingTime(duration);
		}
		map.put(msisdn, report);
	}

}
