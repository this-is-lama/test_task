package my.project;

import my.project.entity.CallDataRecord;
import my.project.repositories.CallDataRecordRepository;
import my.project.repositories.SubscriberRepository;
import my.project.services.CallDataRecordService;
import my.project.util.IncorrectPhoneNumberException;
import my.project.util.NoDataToReceiveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CallDataRecordServiceTest {

	@Mock
	public SubscriberRepository subscriberRepository;

	@Mock
	private CallDataRecordRepository callDataRecordRepository;

	@InjectMocks
	private CallDataRecordService callDataRecordService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGenerateReport_Success() throws IOException {
		String msisdn = "71234567890";
		String startDate = "2023-01-01";
		String endDate = "2023-12-31";

		CallDataRecord record = new CallDataRecord();
		record.setCallType("incoming");
		record.setPhoneOne(msisdn);
		record.setPhoneTwo("71234567891");
		record.setStartTime(LocalDateTime.now());
		record.setEndTime(LocalDateTime.now().plusMinutes(5));

		when(callDataRecordRepository.findByMsisdnAndDateRange(msisdn,
				LocalDateTime.parse(startDate + "T00:00:00"),
				LocalDateTime.parse(endDate + "T00:00:00"))).thenReturn(List.of(record));

		String uuid = callDataRecordService.generateReport(msisdn, startDate, endDate);

		assertNotNull(uuid);

		String expectedFileName = String.format("%s_%s.csv", msisdn, uuid);
		String filePath = "src/main/java/my/project/reports/" + expectedFileName;
		Path path = Paths.get(filePath);
		assertTrue(Files.exists(path));

		Files.delete(path);
	}

	@Test
	public void testGenerateReport_IncorrectPhoneNumber() {
		String msisdn = "12345";
		String startDate = "2023-01-01";
		String endDate = "2023-12-31";

		assertThrows(IncorrectPhoneNumberException.class, () -> callDataRecordService.generateReport(msisdn, startDate, endDate));
	}

	@Test
	public void testGenerateReport_NoDataToReceive() {
		String msisdn = "71234567890";
		String startDate = "2023-01-01";
		String endDate = "2023-12-31";

		when(callDataRecordRepository.findByMsisdnAndDateRange(msisdn,
				LocalDateTime.parse(startDate + "T00:00:00"),
				LocalDateTime.parse(endDate + "T00:00:00"))).thenReturn(Collections.emptyList());

		assertThrows(NoDataToReceiveException.class, () -> callDataRecordService.generateReport(msisdn, startDate, endDate));
	}
}

