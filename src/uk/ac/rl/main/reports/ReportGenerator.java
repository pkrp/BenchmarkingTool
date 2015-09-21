package uk.ac.rl.main.reports;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperXlsExporterBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabMeasureBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabRowGroupBuilder;
import net.sf.dynamicreports.report.builder.crosstab.Crosstabs;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.log4j.Logger;

import uk.ac.rl.test.model.ReportRow;
import uk.ac.rl.test.model.SchemaType;

public class ReportGenerator {

	public static Logger log = Logger.getLogger(ReportGenerator.class);
	
	private final String REPORTS_PATH = "C:/Users/vjn76682/eclipse/workspace/OracleTest/data/clustered";

	private ArrayList<ReportRow> data;
	private String filepath;
	private Date currentDate;
	private JasperReportBuilder reportBuilder;

	public void generate(SchemaType schemaType, ArrayList<ReportRow> data) {
		this.data = data;
		this.currentDate = new Date();
		this.reportBuilder = DynamicReports.report();
		this.filepath = String.format("%s/%s/report_%s", REPORTS_PATH, schemaType.toString(),
				new SimpleDateFormat("yyyyMMddHHmm").format(currentDate));
		build();
	}

	private JasperXlsExporterBuilder getXlsExporter() {
		return DynamicReports.export.xlsExporter(String.format("%s.xls", this.filepath)).setDetectCellType(true)
				.setIgnorePageMargins(true).setWhitePageBackground(false).setRemoveEmptySpaceBetweenColumns(true);
	}

	private void build() {

		log.info("Generating reports...");

		CrosstabRowGroupBuilder<String> rowGroup = Crosstabs.rowGroup("query", String.class);
		CrosstabColumnGroupBuilder<String> columnUserGroup = Crosstabs.columnGroup("user", String.class);
		CrosstabMeasureBuilder<Long> sizeVariable = Crosstabs.measure("Size", "size", Long.class, Calculation.SUM);
		CrosstabMeasureBuilder<Object> timeVariable = Crosstabs.measure("Time", "time", Double.class, Calculation.SUM).setPattern("###.####");
		sizeVariable.setStyle(StyleGenerator.getSizeVariableStyle());
		timeVariable.setStyle(StyleGenerator.getTimeVariableStyle());
		rowGroup.setHeaderWidth(410);
		
		CrosstabBuilder crosstab = Crosstabs.crosstab()
				.setCellWidth(80)
				.rowGroups(rowGroup)
				.columnGroups(columnUserGroup).measures(sizeVariable, timeVariable);
		TextColumnBuilder<String> typeColumn = col.column("Type", "type", type.stringType()).setWidth(0)
				.setStyle(StyleGenerator.getBoldStyle(stl.style()));

		try {
			reportBuilder
					.setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
					.summary(crosstab)
					.groupBy(typeColumn)
					.title(cmp
							.horizontalList()
							.newRow(20)
							.add(cmp.text(
									String.format("Performance report - %s", new SimpleDateFormat(
											"dd.MM.yyy, HH:mm").format(currentDate))).setStyle(
									StyleGenerator.getTitleStyle())).newRow(25))
					.setDataSource(new JRBeanCollectionDataSource(data))
					.setColumnTitleStyle(StyleGenerator.getColumnTitleStyle()).highlightDetailEvenRows()
					.toPdf(new FileOutputStream(String.format("%s.pdf", this.filepath))).toXls(getXlsExporter()).show();
		} catch (DRException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		log.info(String
				.format("Reports generated! \nPDF path: %s.pdf \nXLS path: %s.xls", this.filepath, this.filepath));
	}
}
