import java.net.InetAddress;
import java.net.UnknownHostException;

public class IP2Letters2IP {
	public static String ipToLetters(String ipAddress) {
		if (ipAddress == null || ipAddress.trim().isEmpty()) {
			throw new IllegalArgumentException("����� IP �� ����� ����� ����");
		}

		// ���� ������ �������
		ipAddress = ipAddress.trim();

		// ����� ������ ������ �� ����� �-IP
		String[] parts = ipAddress.split("\\.");
		if (parts.length != 4) {
			throw new IllegalArgumentException("����� IP ����� ����� 4 ������ ������� �������");
		}

		StringBuilder result = new StringBuilder();

		// ����� �� ��� ������
		for (String part : parts) {
			try {
				int number = Integer.parseInt(part);
				if (number < 0 || number > 255) {
					throw new IllegalArgumentException("�� ���� ������ IP ���� ����� ��� 0 �-255");
				}

				// ���� ����� ����� ����� �� ����� �������
				String paddedNumber = String.format("%03d", number);

				// ���� �� ���� ����
				for (char digit : paddedNumber.toCharArray()) {
					// ���� ����� ���� ('0' -> 'A', '1' -> 'B', ���')
					char letter = (char) ('A' + (digit - '0'));
					result.append(letter);
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("����� IP ����� ����� �� ������ �������");
			}
		}

		return result.toString();
	}

	public static String lettersToIP(String letters) {
		// ������ ������
		if (letters == null || letters.trim().isEmpty()) {
			throw new IllegalArgumentException("������� �� ����� ����� ����");
		}

		letters = letters.trim().toUpperCase();

		// ����� ����� ������� ����� �-12 (4 ������ �� 3 ������)
		if (letters.length() != 12) {
			throw new IllegalArgumentException("���� ������� ���� ����� 12 (4 ������ �� 3 ������ �� ���)");
		}

		// ����� ��� ������ �� ������ A-Z
		if (!letters.matches("[A-Z]+")) {
			throw new IllegalArgumentException("������� ����� ����� �� ������ A-Z");
		}

		StringBuilder result = new StringBuilder();

		// ����� �� ���� �� 3 ������
		for (int i = 0; i < letters.length(); i += 3) {
			if (i > 0) {
				result.append(".");
			}

			// ���� 3 ������ �����
			int number = 0;
			for (int j = 0; j < 3; j++) {
				char letter = letters.charAt(i + j);
				int digit = letter - 'A'; // ���� ���� ����� (A->0, B->1, etc.)

				// ����� ������ ����� (0-9)
				if (digit < 0 || digit > 9) {
					throw new IllegalArgumentException("���� " + letter + " ���� ������ ���� ����� (�� A-J ����)");
				}

				number = number * 10 + digit;
			}

			// ����� ������ ����� ����� �� ����� IP
			if (number > 255) {
				throw new IllegalArgumentException("����� ������ (" + number + ") ���� �-255");
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
			// ����� ���� ��-�������
			String[] testIPs = { "192.168.1.1", "10.0.0.1", "255.255.255.255", "0.0.0.0" };

			for (String originalIP : testIPs) {
				// ���� �������
				String letters = ipToLetters(originalIP);
				// ���� ����� �-IP
				String convertedBack = lettersToIP(letters);

				System.out.println("IP �����: " + originalIP);
				System.out.println("������: " + letters);
				System.out.println("IP ���� ���� ����: " + convertedBack);
				System.out.println("����� �����: " + originalIP.equals(convertedBack));
				System.out.println();
			}

			// ����� ���� ��� �������
			String[] invalidInputs = { "ABCDEFGHIJKL", // ����
					"ABC", // ��� ���
					"ABCDEFGHIJKLM", // ���� ���
					"ABC.DEF.GHI", // ����� �� ������
					"ABCDEFKLMNOP" // ���� ������ ���� �-J
			};

			for (String input : invalidInputs) {
				try {
					String result = lettersToIP(input);
					System.out.println("���: " + input);
					System.out.println("���: " + result);
				} catch (IllegalArgumentException e) {
					System.out.println("���: " + input);
					System.out.println("�����: " + e.getMessage());
				}
				System.out.println();
			}

		} catch (IllegalArgumentException e) {
			System.err.println("�����: " + e.getMessage());
		}

	}

}
