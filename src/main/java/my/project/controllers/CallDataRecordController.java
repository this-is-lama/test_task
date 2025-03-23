package my.project.controllers;

import my.project.entity.CallDataRecord;
import my.project.services.CallDataRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Контроллер для обработки запросов, связанных с записями вызовов (CDR).
 * Этот контроллер предоставляет API для генерации записей вызовов и отчетов по ним.
 *
 * <p>Контроллер обрабатывает следующие запросы:</p>
 * <ul>
 *   <li><code>POST /cdr/generateRecord</code> - Генерация записей вызовов.</li>
 *   <li><code>POST /cdr/generateReport</code> - Генерация отчета по записям вызовов для указанного абонента.</li>
 * </ul>
 */
@RestController
@RequestMapping("/cdr")
public class CallDataRecordController {

	private final CallDataRecordService callDataRecordService;

	/**
	 * Конструктор для инициализации контроллера.
	 *
	 * @param callDataRecordService сервис для работы с записями вызовов
	 */
	public CallDataRecordController(CallDataRecordService callDataRecordService) {
		this.callDataRecordService = callDataRecordService;
	}

	/**
	 * Обрабатывает HTTP GET запрос для получения всех записей данных вызовов.
	 * <p>
	 * Этот метод извлекает все записи данных вызовов с помощью сервиса {@code callDataRecordService}.
	 * Возвращает список записей в формате JSON с кодом ответа 200 (OK).
	 *
	 * @return {@code ResponseEntity<List<CallDataRecord>>} объект, содержащий список всех записей данных вызовов
	 *         и статус ответа 200 (OK).
	 */
	@GetMapping("/all")
	public ResponseEntity<List<CallDataRecord>> getAll() {
		List<CallDataRecord> response = callDataRecordService.getAllCallDataRecords();
		return ResponseEntity.ok(response);
	}


	/**
	 * Генерирует записи вызовов.
	 *
	 * <p>Этот метод обрабатывает POST-запрос на <code>/cdr/generateRecord</code>.
	 * В результате выполнения метода вызывается сервис для генерации записей вызовов.</p>
	 *
	 * @return статус ответа HTTP 200 (OK) при успешной генерации записей
	 */
	@PostMapping("/generateRecord")
	public ResponseEntity<HttpStatus> generateCallDataRecord() {
		callDataRecordService.generateCallDataRecords();
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Генерирует отчет по записям вызовов для указанного абонента за указанный период.
	 *
	 * <p>Этот метод обрабатывает POST-запрос на <code>/cdr/generateReport</code>.
	 * Отчет генерируется на основе переданных параметров: номера абонента,
	 * начало периода и конец периода.</p>
	 *
	 * @param msisdn номер абонента, для которого генерируется отчет
	 * @param startTime время начала периода, за который генерируется отчет
	 * @param endTime время окончания периода, за который генерируется отчет
	 * @return сообщение об успешной генерации отчета и его уникальный идентификатор
	 * @throws IOException если произошла ошибка при генерации отчета
	 */
	@PostMapping("/generateReport")
	public ResponseEntity<String> generateReport(@RequestParam("msisdn") String msisdn,
												 @RequestParam("start") String startTime,
												 @RequestParam("end") String endTime) throws IOException {
		String uuid = callDataRecordService.generateReport(msisdn, startTime, endTime);
		return ResponseEntity.ok("Отчет был успешно сгенерирован. UUID:" + uuid);
	}
}


