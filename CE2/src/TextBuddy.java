/*
 * This program will modify the text file upon providing commands
 * Benedict Chong Wen Yong
 * A0110679A
 * C01
 * 
 */

import java.util.*;
import java.io.*;

public class TextBuddy {

	static File file_object = null;
	static final String MESSAGE_DELETE_ERROR = "Unable to delete";
	static final String MESSAGE_NOT_FOUND = " not found";
	static Scanner sc = new Scanner(System.in);
	static String fileName;

	public static void main(String[] args) throws IOException {

		Stack<String> record = new Stack<String>();
		Stack<String> extra = new Stack<String>();

		fileName = getFileName(args);
		createOrModifyFile(record);
		printWelcomeMsg();
		commandInput(record, extra);
		saveData(record, file_object);

	}

	private static void commandInput(Stack<String> record, Stack<String> extra)
			throws FileNotFoundException {
		String command;

		do {
			command = requestInput();

			if (command.equals("display")) {
				displayContent(record, extra);

			} else if (command.equals("clear")) {
				deleteAll(record);

			} else if (command.equals("add")) {
				addTask(record);

			} else if (command.equals("delete")) {
				deleteTask(record, extra);

			} else if (command.equals("sort")) {
				sortAlphabetically(record, extra);

			} else if (command.equals("search")) {
				searchContent(record, extra);

			}

		} while (!command.equals("exit"));

	}

	private static void searchContent(Stack<String> record, Stack<String> extra) {
		String search = sc.next();
		while (!record.empty()) {
			extra.push(record.pop());
		}
		int displayCounter = 1;
		int searchCount = 0;
		while (!extra.empty()) {
			String contents = extra.pop();
			int isFound = contents.indexOf(search);
			if (isFound != -1) {
				System.out.println(displayCounter + ". " + contents);
				searchCount++;
			}
			record.push(contents);
			displayCounter++;
		}
		if (searchCount == 0) {
			System.out.println("'" + search + "'" + MESSAGE_NOT_FOUND);
		}
	}

	private static void sortAlphabetically(Stack<String> record,
			Stack<String> extra) {
		for (int i = 0; i < record.size(); i++) {
			while (!record.empty()) {
				extra.push(record.pop());
			}
			String case1 = extra.pop();
			String case2 = extra.pop();
			String baseCase;

			// Initial check
			baseCase = initialSort(record, case1, case2);
			// Remaining values
			baseCase = remainingSort(record, extra, baseCase);
			// Push final value
			record.push(baseCase);

		}

		printFileContents(record, extra);
	}

	public static String initialSort(Stack<String> record, String case1,
			String case2) {
		String baseCase;
		int weight = case1.compareTo(case2);
		if (weight < 0) {
			record.push(case1);
			baseCase = case2;
		} else {
			record.push(case2);
			baseCase = case1;
		}
		return baseCase;
	}

	private static String remainingSort(Stack<String> record,
			Stack<String> extra, String baseCase) {
		while (!extra.empty()) {
			String case3 = extra.pop();
			int weight = baseCase.compareTo(case3);
			if (weight < 0) {
				record.push(baseCase);
				baseCase = case3;
			} else {
				record.push(case3);
			}
		}
		return baseCase;
	}

	private static void deleteTask(Stack<String> record, Stack<String> extra)
			throws FileNotFoundException {
		int deleteNo = sc.nextInt();
		if (record.empty() || record.size() < deleteNo) {
			System.out.println(MESSAGE_DELETE_ERROR);
		} else {
			while (deleteNo != record.size()) {
				extra.push(record.pop());
			}
			System.out.println("deleted from " + fileName + ": \""
					+ record.pop() + "\"");
			while (!extra.empty()) {
				record.push(extra.pop());
			}
		}

		saveData(record, file_object);
	}

	private static void addTask(Stack<String> record)
			throws FileNotFoundException {
		String information = sc.nextLine();
		record.push(information.trim());
		System.out.println("added to " + fileName + ": \"" + information.trim()
				+ "\"");
		saveData(record, file_object);
	}

	private static void deleteAll(Stack<String> record)
			throws FileNotFoundException {
		while (!record.empty()) {
			record.pop();
		}
		System.out.println("all content deleted from " + fileName);
		saveData(record, file_object);
	}

	private static void displayContent(Stack<String> record, Stack<String> extra) {

		if (record.empty()) {
			printEmptyFile(fileName);
		} else {
			printFileContents(record, extra);
		}
	}

	private static void printEmptyFile(String fileName) {
		System.out.println(fileName + " is empty");
	}

	private static void printFileContents(Stack<String> record,
			Stack<String> extra) {
		int displayCounter = 1;
		String display;
		while (!record.empty()) {
			extra.push(record.pop());
		}
		while (!extra.empty()) {
			display = extra.pop();
			System.out.println(displayCounter + ". " + display);
			record.push(display);
			displayCounter++;
		}

	}

	private static String requestInput() {
		String command;
		System.out.print("command: ");
		command = sc.next();
		return command;
	}

	private static void printWelcomeMsg() {
		System.out.println("Welcome to TextBuddy. " + fileName
				+ " is ready for use");
	}

	private static void createOrModifyFile(Stack<String> record)
			throws IOException {
		if (!file_object.exists()) {
			file_object.createNewFile();
			System.out.print(fileName + " has been created \n");
		} else if (file_object.exists()) {
			sc.nextLine();
			System.out.print("Would you like to edit file? (Y/N)");
			editOrExit(sc, record);

		}
	}

	private static void editOrExit(Scanner sc, Stack<String> record)
			throws IOException {
		String choice = sc.nextLine();
		if (choice.equalsIgnoreCase("N")) {
			System.exit(0);
		} else if (choice.equalsIgnoreCase("Y")) {
			String[] previous = oldData(file_object);
			for (int i = 0; i < previous.length; i++) {
				record.push(previous[i]);
			}
		}
	}

	public static String getFileName(String[] args) {
		String fileName;
		// If no arguments, get arguments, for regression testing
		if (args.length < 1) {
			fileName = sc.next();
			file_object = new File(fileName);
		} else {
			fileName = args[0];
			file_object = new File(fileName);
		}
		return fileName;
	}

	private static void saveData(Stack<String> stk, File file_object)
			throws FileNotFoundException {
		PrintWriter writer2 = new PrintWriter(file_object);
		String[] strArr = new String[stk.size()];
		stk.toArray(strArr);
		for (int i = 0; i < strArr.length; i++) {
			writer2.println(strArr[i]);
		}
		writer2.close();

	}

	private static String[] oldData(File file_object) throws IOException {
		FileReader fr = new FileReader(file_object);
		BufferedReader textReader = new BufferedReader(fr);
		int numberOfLines = countLines(file_object);
		String[] textData = new String[numberOfLines];
		for (int i = 0; i < numberOfLines; i++) {
			textData[i] = textReader.readLine();
		}
		textReader.close();
		return textData;
	}

	private static int countLines(File file_object) throws IOException {
		FileReader file_to_read = new FileReader(file_object);
		BufferedReader bf = new BufferedReader(file_to_read);
		String aLine;
		int numberOfLines = 0;
		while ((aLine = bf.readLine()) != null) {
			numberOfLines++;
		}
		bf.close();
		return numberOfLines;

	}
}