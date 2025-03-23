package my.project;

import my.project.dto.UsageDataReportDTO;
import my.project.entity.CallDataRecord;
import my.project.repositories.CallDataRecordRepository;
import my.project.services.UsageDataReportService;
import my.project.util.IncorrectPhoneNumberException;
import my.project.util.NoDataToReceiveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsageDataReportServiceTest {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	@Mock
	private CallDataRecordRepository callDataRecordRepository;

	@InjectMocks
	private UsageDataReportService usageDataReportService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGetUsageDataReportByMsisdnAndMonth_Success() {
		String msisdn = "71234567890";
		String month = "2023-01";

		CallDataRecord record1 = new CallDataRecord();
		record1.setCallType("01");
		record1.setPhoneOne(msisdn);
		record1.setPhoneTwo("71234567891");
		record1.setStartTime(LocalDateTime.parse("2023-01-10T10:00:00", FORMATTER));
		record1.setEndTime(LocalDateTime.parse("2023-01-10T10:05:00", FORMATTER));

		CallDataRecord record2 = new CallDataRecord();
		record2.setCallType("02");
		record2.setPhoneOne("71234567891");
		record2.setPhoneTwo(msisdn);
		record2.setStartTime(LocalDateTime.parse("2023-01-15T11:00:00", FORMATTER));
		record2.setEndTime(LocalDateTime.parse("2023-01-15T11:10:00", FORMATTER));

		when(callDataRecordRepository.findByMsisdnAndDateRange(msisdn,
				LocalDateTime.parse("2023-01-01T00:00:00", FORMATTER),
				LocalDateTime.parse("2023-02-01T00:00:00", FORMATTER).minusSeconds(1)))
				.thenReturn(List.of(record1, record2));

		UsageDataReportDTO report = usageDataReportService.getUsageDataReportByMsisdnAndMonth(msisdn, month);

		assertNotNull(report);
		assertEquals(msisdn, report.getMsisdn());
	}

	@Test
	public void testGetUsageDataReportByMsisdnAndMonth_IncorrectPhoneNumber() {
		String msisdn = "123456789";
		String month = "2023-01";

		assertThrows(IncorrectPhoneNumberException.class, () -> usageDataReportService.getUsageDataReportByMsisdnAndMonth(msisdn, month));
	}

	@Test
	public void testGetUsageDataReportByMsisdnAndMonth_NoDataToReceive() {
		String msisdn = "71234567890";
		String month = "2023-01";

		when(callDataRecordRepository.findByMsisdnAndDateRange(msisdn,
				LocalDateTime.parse("2023-01-01T00:00:00", FORMATTER),
				LocalDateTime.parse("2023-02-01T00:00:00", FORMATTER).minusSeconds(1)))
				.thenReturn(Collections.emptyList());

		assertThrows(NoDataToReceiveException.class, () -> usageDataReportService.getUsageDataReportByMsisdnAndMonth(msisdn, month));
	}

	@Test
	public void testGetUsageDataReportByMsisdn_Success() {
		String month = "2023-01";

		CallDataRecord record1 = new CallDataRecord();
		record1.setCallType("01");
		record1.setPhoneOne("71234567890");
		record1.setPhoneTwo("71234567891");
		record1.setStartTime(LocalDateTime.parse("2023-01-10T10:00:00", FORMATTER));
		record1.setEndTime(LocalDateTime.parse("2023-01-10T10:05:00", FORMATTER));

		CallDataRecord record2 = new CallDataRecord();
		record2.setCallType("02");
		record2.setPhoneOne("71234567891");
		record2.setPhoneTwo("71234567890");
		record2.setStartTime(LocalDateTime.parse("2023-01-15T11:00:00", FORMATTER));
		record2.setEndTime(LocalDateTime.parse("2023-01-15T11:10:00", FORMATTER));

		when(callDataRecordRepository.findAllByDateRange(
				LocalDateTime.parse("2023-01-01T00:00:00", FORMATTER),
				LocalDateTime.parse("2023-02-01T00:00:00", FORMATTER).minusSeconds(1)))
				.thenReturn(List.of(record1, record2));

		List<UsageDataReportDTO> reports = usageDataReportService.getUsageDataReportByMsisdn(month);

		assertNotNull(reports);
		assertEquals(2, reports.size());
	}

	@Test
	public void testGetUsageDataReportByMsisdn_NoDataToReceive() {
		String month = "2023-01";

		when(callDataRecordRepository.findAllByDateRange(
				LocalDateTime.parse("2023-01-01T00:00:00", FORMATTER),
				LocalDateTime.parse("2023-02-01T00:00:00", FORMATTER).minusSeconds(1)))
				.thenReturn(Collections.emptyList());

		assertThrows(NoDataToReceiveException.class, () -> usageDataReportService.getUsageDataReportByMsisdn(month));
	}
}

