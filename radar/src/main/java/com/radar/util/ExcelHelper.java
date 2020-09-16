package com.radar.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.radar.model.Blip;

public class ExcelHelper {
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	static String[] HEADERs = { "Id", "Title", "Description", "Published" };
	static String SHEET = "Build your Technology Radar";

	public static boolean hasExcelFormat(MultipartFile file) {
		if (!TYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}

	public static List<Blip> excelToBlips(InputStream is) {
		try {
			Workbook workbook = new XSSFWorkbook(is);

			Sheet sheet = workbook.getSheet(SHEET);
			Iterator<Row> rows = sheet.iterator();

			List<Blip> blips = new ArrayList<Blip>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();
				Blip blip = new Blip();
				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				} else {
					blip.setId(Long.parseLong((rowNumber)+""));
					rowNumber++;
				}
				
				Iterator<Cell> cellsInRow = currentRow.iterator();
				int cellIdx = 0;
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();

					switch (cellIdx) {
						case 0:
							blip.setName(currentCell.getStringCellValue());
							break;

						case 1:
							blip.setRing(currentCell.getStringCellValue());
							break;

						case 2:
							blip.setQuadrant(currentCell.getStringCellValue());
							break;

						case 3:
							blip.setIsNew(currentCell.getBooleanCellValue());
							break;
	          
						case 4:
							blip.setDescription(currentCell.getStringCellValue());
							break;

						default:
							break;
					}
					cellIdx++;
				}
				if(!(blip.getName().equals("") || blip.getName()==null)){
					blips.add(blip);
				}
			}
			workbook.close();
			return blips;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}
	}
}
