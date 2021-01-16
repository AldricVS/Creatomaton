package test.manual;

public class FileTest {

	public static void main(String[] args) {
		System.out.println(System.getenv("LocalAppData")); // C:\Users\...\AppData\Local
		System.out.println(System.getProperty("user.home")); // C:\Users\...
	}

}
