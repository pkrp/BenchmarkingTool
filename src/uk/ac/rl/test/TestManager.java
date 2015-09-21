package uk.ac.rl.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.icatproject.EntityBaseBean;
import org.icatproject.ICAT;
import org.icatproject.ICATService;
import org.icatproject.IcatException_Exception;
import org.icatproject.Login.Credentials;
import org.icatproject.Login.Credentials.Entry;

import uk.ac.rl.main.reports.ReportGenerator;
import uk.ac.rl.test.model.ReportRow;
import uk.ac.rl.test.model.SchemaType;
import uk.ac.rl.test.model.User;


public class TestManager {
	
	public static Logger log = Logger.getLogger(TestManager.class);
	
	private String adminSessionId;
	private SchemaType schemaType;
	public static ICAT icat;
	public static  String sessionId;
	private ArrayList<ReportRow> results = new ArrayList<ReportRow>();
	private User user;
	private AdminManager adminManager;

	public TestManager(String serverUrl, SchemaType schemaType) {
		this.schemaType = schemaType;
		icat = getIcat(serverUrl);
	}

	private String login(String authType, String login, String password) {
		Credentials credentials = new Credentials(); 
		List<Entry> entries = credentials.getEntry(); 
		Entry entry = new Entry(); 
		entry.setKey("username"); 
		entry.setValue(login); 
		entries.add(entry); 
		entry = new Entry(); 
		entry.setKey("password"); 
		entry.setValue(password);
		entries.add(entry); 
		try {
			String sessionId = icat.login(authType, credentials);
			log.info(String.format("%s/%s logged in", authType, login));
			return sessionId;
		} catch (IcatException_Exception e) {
			e.printStackTrace();
			return null;
		}  
	}
	
	public void logout(String sessionId) {
		try {
			icat.logout(sessionId);
		} catch (IcatException_Exception e) {
			e.printStackTrace();
		}
	}
	
	public ICAT getIcat(String serverUrl) {
		URL icatUrl;
		try {
			icatUrl = new URL(new URL(serverUrl), "/ICATService/ICAT?wsdl");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} 
		QName qName = new QName("http://icatproject.org", "ICATService"); 
		return new ICATService(icatUrl, qName).getICATPort();
	}
	
	public void afterAll() {
		new ReportGenerator().generate(schemaType, results);
		try {
			List<Object> logs = icat.search(adminSessionId, "SELECT log FROM Log log where log.createId = 'simple/admin'");
			icat.deleteMany(sessionId, (List<EntityBaseBean>)(Object) logs);
			log.info(String.format("Deleted %d logs", logs.size()));
			adminManager.deleteSampleData();
			logout(adminSessionId);
			log.info("simple/admin logged out");
		} catch (IcatException_Exception e) {
			e.printStackTrace();
		}
	}
	
	public void beforeAll(User admin) {
		adminSessionId = login("simple", "admin", "admin");
		adminManager = new AdminManager(schemaType, icat, adminSessionId, admin);
		adminManager.generateSampleData();
		logout(adminSessionId);
	}
	
	public void clearUserLogs() {
		try {
			List<Object> logs = icat.search(sessionId, String.format("SELECT log FROM Log log where log.createId = '%s/%s'", user.getAuthType(), user.getLogin()));
			icat.deleteMany(sessionId, (List<EntityBaseBean>)(Object) logs);
			log.info(String.format("Deleted %d logs", logs.size()));			
		} catch (IcatException_Exception e) {
			e.printStackTrace();
		}
	}
	
	public void startUser(User user) {
		this.user = user;
		sessionId = login(user.getAuthType(), user.getLogin(), user.getPassword());
		if (sessionId != null) {
			results.addAll(new SearchTest(schemaType, icat, sessionId, user).run());
			//results.addAll(new CreateTest(schemaType, icat, sessionId, user).run());
			clearUserLogs();
			logout(sessionId);
			log.info(String.format("%s/%s logged out", user.getAuthType(), user.getLogin()));
		}
	}
}
