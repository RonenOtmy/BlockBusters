import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

public class ReadDataFromExcel {

	public static String[] getSheetNames(String fileName) throws IOException {
		File file = new File(fileName);
		try (FileInputStream fis = new FileInputStream(file)) {
			Workbook workbook = null;

			workbook = new HSSFWorkbook(fis);

			// sheets names
			List<String> sheetNames = new ArrayList<>();
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				sheetNames.add(workbook.getSheetName(i));
			}

			workbook.close();
			return sheetNames.toArray(new String[0]);
		}
	}

	public static Question[][] ReadDataFromExcel(String filename, int sheetIndex) {

		ArrayList<Question> questionArr = new ArrayList<Question>();

		File file = new File(filename);
		try {
			// Create a file input stream to read Excel workbook and worksheet
			FileInputStream xlfile = new FileInputStream(file);
			HSSFWorkbook xlwb = new HSSFWorkbook(xlfile);
			HSSFSheet xlSheet = xlwb.getSheetAt(sheetIndex);

			// Get the number of rows and columns
			int numRows = xlSheet.getLastRowNum() + 1;
			int numCols = xlSheet.getRow(0).getLastCellNum();

			// Create double array data table - rows x cols
			// We will return this data table

			// For each row, create a HSSFRow, then iterate through the "columns"
			// For each "column" create an HSSFCell to grab the value at the specified cell
			// (i,j)
			// i=1 -> to skip first row with columns names
			for (int i = 1; i < numRows; i++) {
				HSSFRow xlRow = xlSheet.getRow(i);
				int currentAnswerIndex = 0;

				for (int j = 0; j < numCols; j++) {
					HSSFCell xlCell = xlRow.getCell(j);

					if (j == 0)
						questionArr.add(new Question(xlCell.toString()));
					else
						questionArr.get(questionArr.size() - 1).setAnswerByIndex(xlCell.toString(),
								currentAnswerIndex++);

				}

				// shuffle answers of question
				questionArr.get(questionArr.size() - 1).shuffleAnswers();

			}
		} catch (IOException e) {
			System.out.println("ERROR FILE HANDLING " + e.toString());
		}

		int size = (int) (Math.sqrt(questionArr.size()));
		int questionArrIndex = 0;
		Question[][] array = new Question[size][size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				array[i][j] = questionArr.get(questionArrIndex++);
			}
		}

		return array;
	}

	public static Question[][] Read2DQuestionArrayFromExcel(String filename, int sheetIndex) {

		Question[][] qArr = null;

		File file = new File(filename);
		try {
			// Create a file input stream to read Excel workbook and worksheet
			FileInputStream xlfile = new FileInputStream(file);
			HSSFWorkbook xlwb = new HSSFWorkbook(xlfile);
			HSSFSheet xlSheet = xlwb.getSheetAt(sheetIndex);

			// Get the number of rows and columns
			int numRows = xlSheet.getLastRowNum() + 1;
			int numCols = xlSheet.getRow(0).getLastCellNum();

			int size = (int) Math.sqrt(numRows);
			qArr = new Question[size][size];

			// Create double array data table - rows x cols
			// We will return this data table

			// For each row, create a HSSFRow, then iterate through the "columns"
			// For each "column" create an HSSFCell to grab the value at the specified cell
			// (i,j)
			// i=1 -> to skip first row with columns names

			System.out.println("size: " + size);

			
			// ignore first column (columns names)
			for (int i = 1; i < numRows; i++) {
				HSSFRow xlRow = xlSheet.getRow(i);
				int currentAnswerIndex = 0;


				qArr[(i-1) / size][(i-1) % size] = new Question(xlRow.getCell(0).toString());
				qArr[(i-1) / size][(i-1) % size].setAnswerByIndex(xlRow.getCell(1).toString(), currentAnswerIndex++);
				qArr[(i-1) / size][(i-1) % size].setAnswerByIndex(xlRow.getCell(2).toString(), currentAnswerIndex++);
				qArr[(i-1) / size][(i-1) % size].setAnswerByIndex(xlRow.getCell(3).toString(), currentAnswerIndex++);
				qArr[(i-1) / size][(i-1) % size].setAnswerByIndex(xlRow.getCell(4).toString(), currentAnswerIndex++);

				// shuffle answers of question
				qArr[(i-1) / size][(i-1) % size].shuffleAnswers();

			}
		} catch (IOException e) {
			System.out.println("ERROR FILE HANDLING " + e.toString());
		}

		return qArr;
	}

	public static void main(String args[]) {

		// display sheets names
		try {
			String[] namesArr = getSheetNames("questions data//blockbustersgame.xls");
			System.out.println(Arrays.toString(namesArr));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// display sheet data
		Question[][] questionArr = Read2DQuestionArrayFromExcel("questions data//blockbustersgame.xls", 0);

		for (int i = 0; i < questionArr.length; i++) {
			System.out.println();
			for (int j = 0; j < questionArr[i].length; j++)
				System.out.println(questionArr[i][j]);

		}

	}

}