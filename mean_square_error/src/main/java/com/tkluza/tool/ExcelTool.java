package com.tkluza.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import com.tkluza.image.model.ImageQualityResult;

public class ExcelTool {

	private HSSFWorkbook workbook;
	private HSSFSheet excelSheet;
	private File excelFile;
	private FileInputStream inputStreamFile;

	public ExcelTool() {
	}

	public void init() {
		openExcelFile();
	}

	public void writeResult(ImageQualityResult result) {
		if (workbook == null || excelSheet == null) {
			System.out.println("Excel page is not opened");
			return;
		}
		writeResultToRow(result, excelSheet.getLastRowNum() + 1);
		close();
	}

	public void writeResultList(List<ImageQualityResult> resultList) {
		if (workbook == null || excelSheet == null) {
			System.out.println("Excel page is not opened");
			return;
		}
		int currentRow = excelSheet.getLastRowNum() + 1;
		for (int i = 0; i < resultList.size(); i++) {
			writeResultToRow(resultList.get(i), currentRow + i);
		}
		close();
	}

	private void writeResultToRow(ImageQualityResult result, int rowNumber) {
		try {
			Row row = excelSheet.createRow(rowNumber);
			row.createCell(0).setCellValue(result.getReferenceModel().toString());
			row.createCell(1).setCellValue(result.getTestModel().toString());
			row.createCell(2).setCellValue(result.getAlgorithm().toString());
			row.createCell(3).setCellValue(result.getAlgorithmResult());
			row.createCell(4).setCellValue(result.getScenario().toString());
		} catch (Exception e) {

		}
	}

	public void openExcelFile() {
		try {
			excelFile = new File(Constraint.EXCEL_FILE_SOURCE_PATH);
			inputStreamFile = new FileInputStream(excelFile);
			workbook = new HSSFWorkbook(inputStreamFile);
			excelSheet = workbook.getSheetAt(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			FileOutputStream outputStream = new FileOutputStream(excelFile);
			workbook.write(outputStream);
			inputStreamFile.close();
			outputStream.close();
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
