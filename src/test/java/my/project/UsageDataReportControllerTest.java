package my.project;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Collections;
import java.util.List;

import my.project.controllers.UsageDataReportController;
import my.project.dto.UsageDataReportDTO;
import my.project.services.UsageDataReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class UsageDataReportControllerTest {


	private MockMvc mockMvc;

	@Mock
	private UsageDataReportService usageDataReportService;

	@InjectMocks
	private UsageDataReportController usageDataReportController;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(usageDataReportController).build();
	}

	@Test
	public void testGetUsageDataReportByMsisdn() throws Exception {
		String msisdn = "1234567890";
		String month = "2023-10";
		UsageDataReportDTO dto = new UsageDataReportDTO();
		when(usageDataReportService.getUsageDataReportByMsisdnAndMonth(msisdn, month)).thenReturn(dto);

		mockMvc.perform(get("/udr/getByMsisdn")
						.param("msisdn", msisdn)
						.param("month", month)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").exists());
	}

	@Test
	public void testGetAllUsageDataReportByMonth() throws Exception {
		String month = "2023-10";
		List<UsageDataReportDTO> dtoList = Collections.singletonList(new UsageDataReportDTO());
		when(usageDataReportService.getUsageDataReportByMsisdn(month)).thenReturn(dtoList);

		mockMvc.perform(get("/udr/getAllByMonth")
						.param("month", month)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());
	}
}

