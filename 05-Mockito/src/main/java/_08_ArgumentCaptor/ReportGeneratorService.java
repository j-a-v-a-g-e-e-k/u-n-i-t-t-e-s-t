package _08_ArgumentCaptor;

import java.util.Date;

public class ReportGeneratorService {
	
	private IReportGenerator reportGenerator;
	
	public void generateReport(Date startDate, Date endDate, byte[] content) {
		ReportEntity report = new ReportEntity();
		report.setContent(content);
		report.setStartDate(startDate);
		report.setEndDate(endDate);
		reportGenerator.generateReport(report);
	}

}
