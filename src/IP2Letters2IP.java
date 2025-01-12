import java.net.InetAddress;
import java.net.UnknownHostException;

public class IP2Letters2IP {
	public static String ipToLetters(String ipAddress) {
		if (ipAddress == null || ipAddress.trim().isEmpty()) {
			throw new IllegalArgumentException("כתובת IP לא יכולה להיות ריקה");
		}

		// הסרת רווחים מיותרים
		ipAddress = ipAddress.trim();

		// בדיקת תקינות בסיסית של כתובת ה-IP
		String[] parts = ipAddress.split("\\.");
		if (parts.length != 4) {
			throw new IllegalArgumentException("כתובת IP חייבת להכיל 4 מספרים מופרדים בנקודות");
		}

		StringBuilder result = new StringBuilder();

		// עיבוד כל חלק בכתובת
		for (String part : parts) {
			try {
				int number = Integer.parseInt(part);
				if (number < 0 || number > 255) {
					throw new IllegalArgumentException("כל מספר בכתובת IP חייב להיות בין 0 ל-255");
				}

				// המרת המספר לשלוש ספרות עם אפסים מובילים
				String paddedNumber = String.format("%03d", number);

				// המרת כל ספרה לאות
				for (char digit : paddedNumber.toCharArray()) {
					// המרת הספרה לאות ('0' -> 'A', '1' -> 'B', וכו')
					char letter = (char) ('A' + (digit - '0'));
					result.append(letter);
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("כתובת IP חייבת להכיל רק מספרים ונקודות");
			}
		}

		return result.toString();
	}

	public static String lettersToIP(String letters) {
		// בדיקות תקינות
		if (letters == null || letters.trim().isEmpty()) {
			throw new IllegalArgumentException("המחרוזת לא יכולה להיות ריקה");
		}

		letters = letters.trim().toUpperCase();

		// בדיקה שאורך המחרוזת מתחלק ב-12 (4 מקטעים של 3 אותיות)
		if (letters.length() != 12) {
			throw new IllegalArgumentException("אורך המחרוזת חייב להיות 12 (4 מקטעים של 3 אותיות כל אחד)");
		}

		// בדיקה שכל התווים הם אותיות A-Z
		if (!letters.matches("[A-Z]+")) {
			throw new IllegalArgumentException("המחרוזת יכולה להכיל רק אותיות A-Z");
		}

		StringBuilder result = new StringBuilder();

		// עיבוד כל מקטע של 3 אותיות
		for (int i = 0; i < letters.length(); i += 3) {
			if (i > 0) {
				result.append(".");
			}

			// המרת 3 אותיות למספר
			int number = 0;
			for (int j = 0; j < 3; j++) {
				char letter = letters.charAt(i + j);
				int digit = letter - 'A'; // המרת האות למספר (A->0, B->1, etc.)

				// בדיקה שהספרה תקינה (0-9)
				if (digit < 0 || digit > 9) {
					throw new IllegalArgumentException("האות " + letter + " אינה מייצגת ספרה חוקית (רק A-J מותר)");
				}

				number = number * 10 + digit;
			}

			// בדיקה שהמספר בטווח החוקי של כתובת IP
			if (number > 255) {
				throw new IllegalArgumentException("המספר המתקבל (" + number + ") גדול מ-255");
			}

			result.append(number);
		}

		return result.toString();
	}

	// get my IP
	public static String getMyIP() {
		byte[] ip = null;

		// Invitation.missionNumber =
		// Integer.parseInt(JOptionPane.showInputDialog("Enter Number of Mission:"));

		try {
			ip = InetAddress.getLocalHost().getAddress();

		} catch (UnknownHostException e) {

			e.printStackTrace();
		}

		return new String(ip);
	}

	public static void main(String[] args) {
		try {
			// בדיקת המרה דו-כיוונית
			String[] testIPs = { "192.168.1.1", "10.0.0.1", "255.255.255.255", "0.0.0.0" };

			for (String originalIP : testIPs) {
				// המרה לאותיות
				String letters = ipToLetters(originalIP);
				// המרה בחזרה ל-IP
				String convertedBack = lettersToIP(letters);

				System.out.println("IP מקורי: " + originalIP);
				System.out.println("אותיות: " + letters);
				System.out.println("IP לאחר המרה חזרה: " + convertedBack);
				System.out.println("ההמרה תקינה: " + originalIP.equals(convertedBack));
				System.out.println();
			}

			// בדיקת מקרי קצה ושגיאות
			String[] invalidInputs = { "ABCDEFGHIJKL", // תקין
					"ABC", // קצר מדי
					"ABCDEFGHIJKLM", // ארוך מדי
					"ABC.DEF.GHI", // תווים לא חוקיים
					"ABCDEFKLMNOP" // מכיל אותיות מעבר ל-J
			};

			for (String input : invalidInputs) {
				try {
					String result = lettersToIP(input);
					System.out.println("קלט: " + input);
					System.out.println("פלט: " + result);
				} catch (IllegalArgumentException e) {
					System.out.println("קלט: " + input);
					System.out.println("שגיאה: " + e.getMessage());
				}
				System.out.println();
			}

		} catch (IllegalArgumentException e) {
			System.err.println("שגיאה: " + e.getMessage());
		}

	}

}
