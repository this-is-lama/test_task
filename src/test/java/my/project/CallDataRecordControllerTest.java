package my.project;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import my.project.controllers.CallDataRecordController;
import my.project.services.CallDataRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class CallDataRecordControllerTest {


	private MockMvc mockMvc;

	@Mock
	private CallDataRecordService callDataRecordService;

	@InjectMocks
	private CallDataRecordController callDataRecordController;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(callDataRecordController).build();
	}

	@Test
	public void testGenerateCallDataRecord() throws Exception {
		doNothing().when(callDataRecordService).generateCallDataRecords();

		mockMvc.perform(post("/cdr/generateRecord"))
				.andExpect(status().isOk());

		verify(callDataRecordService, times(1)).generateCallDataRecords();
	}

	@Test
	public void testGenerateReport() throws Exception {
		String msisdn = "1234567890";
		String startTime = "2023-01-01T00:00:00";
		String endTime = "2023-01-02T00:00:00";
		String expectedUuid = "some-uuid";

		when(callDataRecordService.generateReport(msisdn, startTime, endTime)).thenReturn(expectedUuid);

		mockMvc.perform(post("/cdr/generateReport")
						.param("msisdn", msisdn)
						.param("start", startTime)
						.param("end", endTime))
				.andExpect(status().isOk());

		verify(callDataRecordService, times(1)).generateReport(msisdn, startTime, endTime);
	}
}

