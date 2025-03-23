package my.project.controllers;

import my.project.dto.UsageDataReportDTO;
import my.project.services.UsageDataReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * Контроллер для обработки запросов, связанных с отчетами об использовании данных (UDR).
 * Этот контроллер предоставляет API для получения отчетов об использовании данных на основе информации об абонентах.
 *
 * <p>Контроллер обрабатывает следующие запросы:</p>
 * <ul>
 *   <li><code>GET /udr/getByMsisdn</code> - Получает отчет о потреблении данных для указанного абонента (MSISDN).</li>
 *   <li><code>GET /udr/getAllByMonth</code> - Получает все отчеты о потреблении данных за указанный месяц.</li>
 * </ul>
 */
@Controller
@RequestMapping("/udr")
public class UsageDataReportController {

	private final UsageDataReportService usageDataReportService;

	/**
	 * Конструктор контроллера UsageDataReportController с указанным сервисом.
	 *
	 * @param usageDataReportService сервис для работы с отчетами об использовании данных
	 */
	@Autowired
	public UsageDataReportController(UsageDataReportService usageDataReportService) {
		this.usageDataReportService = usageDataReportService;
	}

	/**
	 * Получает отчет об использовании данных для указанного абонента (MSISDN).
	 *
	 * <p>Этот метод обрабатывает GET-запрос к <code>/udr/getByMsisdn</code>.
	 * Он извлекает отчет об использовании данных на основе предоставленного MSISDN и необязательного параметра месяца.</p>
	 *
	 * @param msisdn номер абонента, для которого запрашивается отчет об использовании данных
	 * @param month необязательный параметр, указывающий месяц (YYYY-MM), за который генерируется отчет; если не указан, то за весь тарифицируемый период
	 * @return ResponseEntity, содержащий UsageDataReportDTO (UDR) для указанного MSISDN
	 */
	@GetMapping("/getByMsisdn")
	public ResponseEntity<UsageDataReportDTO> getUsageDataReportByMsisdn(@RequestParam("msisdn") String msisdn,
																		 @RequestParam(required = false) String month) {
		var response = usageDataReportService.getUsageDataReportByMsisdnAndMonth(msisdn, month);
		return ResponseEntity.ok(response);
	}

	/**
	 * Получает все отчеты об использовании данных за указанный месяц.
	 *
	 * <p>Этот метод обрабатывает GET-запрос к <code>/udr/getAllByMonth</code>.
	 * Он извлекает все отчеты об использовании данных на основе предоставленного параметра месяца.</p>
	 *
	 * @param month месяц (YYYY-MM), за который запрашиваются все отчеты о потреблении данных
	 * @return ResponseEntity, содержащий список UsageDataReportDTO для указанного месяца
	 */
	@GetMapping("/getAllByMonth")
	public ResponseEntity<List<UsageDataReportDTO>> getAllUsageDataReportByMonth(@RequestParam("month") String month) {
		var response = usageDataReportService.getUsageDataReportByMsisdn(month);
		return ResponseEntity.ok(response);
	}
}

