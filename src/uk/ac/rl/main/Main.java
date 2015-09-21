package uk.ac.rl.main;

import org.apache.log4j.PropertyConfigurator;

import uk.ac.rl.main.config.SSL;
import uk.ac.rl.test.TestManager;
import uk.ac.rl.test.model.SchemaType;
import uk.ac.rl.test.model.User;

public class Main {
	
	public static final String SERVER_URL = "http://vm165.nubes.stfc.ac.uk:8080/";

	public static void main(String[] args) {
		String log4jConfPath = "./log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		SSL.disableCertificateValidation();
		User admin = new User("admin", "admin", "simple");
		User user = new User("user", "user", "simple");
		User facilityAdmin = new User("fa", "fa", "simple");
		User instrumentScientist = new User("is", "is", "simple");
		
		TestManager testManager = new TestManager(SERVER_URL, SchemaType.ISIS);
		testManager.beforeAll(admin);
		testManager.startUser(admin);
		testManager.startUser(user);
		//testManager.start(facilityAdmin);
		//testManager.start(instrumentScientist);
		testManager.afterAll();
	}
}
