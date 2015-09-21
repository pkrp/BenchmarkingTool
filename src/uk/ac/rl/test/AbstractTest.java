package uk.ac.rl.test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.icatproject.AccessType;
import org.icatproject.EntityBaseBean;
import org.icatproject.ICAT;
import org.icatproject.IcatException_Exception;

import uk.ac.rl.test.model.ReportRow;
import uk.ac.rl.test.model.QueryType;
import uk.ac.rl.test.model.SchemaType;
import uk.ac.rl.test.model.User;

public abstract class AbstractTest {

	public static final Logger log = Logger.getLogger(AbstractTest.class);

	protected SchemaType schemaType;
	protected final ICAT icat;
	protected final String sessionId;
	protected List<ReportRow> results;
	protected User user;

	public AbstractTest(SchemaType schemaType, ICAT icat, String sessionId, User user) {
		this.schemaType = schemaType;
		this.icat = icat;
		this.sessionId = sessionId;
		this.results = new ArrayList<ReportRow>();
		this.user = user;
	}

	public List<ReportRow> run() {
		return results;
	}

	protected List<Object> runQuery(String query) {
		try {
			return icat.search(sessionId, query);
		} catch (IcatException_Exception e) {
			log.error(String.format("IcatException while running query: %s", query));
			e.printStackTrace();
			return null;
		}
	}

	protected List<Object> searchForObjects(String query) {
		List<Object> result;
		String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		Instant start = Instant.now();
		result = runQuery(query);
		Instant end = Instant.now();
		double duration = Duration.between(start, end).toMillis() / 1000.0;
		log.info(result.size());
		log.info(String.format("%s: %4.3f", methodName, duration));
		results.add(new ReportRow(QueryType.SELECT, getTitle(methodName), query, (long) result.size(), duration, user
				.loginToString().toUpperCase()));
		return result;
	}

	protected <E extends EntityBaseBean> Long createObject(E object) {
		Long id = null;
		try {
			id = icat.create(sessionId, object);
		} catch (IcatException_Exception e) {
			log.error(String.format("IcatException while creating %s", object.getClass().getSimpleName()));
			e.printStackTrace();
			// afterAll();
			return null;
		}
		return id;
	}

	protected List<Long> createObjects(List<? extends EntityBaseBean> objects) {
		List<Long> ids = new ArrayList<Long>();
		try {
			if (icat.isAccessAllowed(sessionId, objects.get(0), AccessType.CREATE)) {
				String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
				Instant start = Instant.now();
				try {
					ids = icat.createMany(sessionId, (List<EntityBaseBean>) objects);
				} catch (IcatException_Exception e) {
					log.error(String.format("IcatException while creating %d %s", objects.size(), objects.getClass()
							.getSimpleName()));
					e.printStackTrace();
					// afterAll();
					return null;
				}
				Instant end = Instant.now();
				double duration = Duration.between(start, end).toMillis() / 1000.0;
				log.info(String.format("%s: %4.3f", methodName, duration));
				results.add(new ReportRow(QueryType.CREATE, getTitle(methodName), "INSERT", (long) objects.size(),
						duration, user.loginToString().toUpperCase()));
			}
		} catch (IcatException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ids;
	}

	protected <E extends EntityBaseBean> void deleteObjects(List<E> objects) throws IcatException_Exception {
		icat.deleteMany(sessionId, (List<EntityBaseBean>) objects);
	}

	protected <E extends EntityBaseBean> void deleteObject(E object) throws IcatException_Exception {
		icat.delete(sessionId, object);
	}

	protected <E extends EntityBaseBean> E getObject(String className, Long id) {
		try {
			return (E) icat.get(sessionId, className, id);
		} catch (IcatException_Exception e) {
			log.error(String.format("IcatException while getting %s", "id: %s", className, id));
			e.printStackTrace();
			return null;
		}
	}

	protected <E extends EntityBaseBean> void clearDatabase(List<Long> ids, Class<E> clazz) {
		List<E> objects = new ArrayList<E>();
		for (Long id : ids) {
			try {
				E object = clazz.newInstance();
				object.setId(id);
				objects.add(object);
			} catch (InstantiationException | IllegalAccessException e) {
				log.error(String.format("Error creating instance of class: %s", clazz.getSimpleName()));
				e.printStackTrace();
			}
		}
		try {
			deleteObjects(objects);
		} catch (IcatException_Exception e) {
			log.error(String.format("IcatException while deleting %d %s", objects.size(), clazz.getSimpleName()));
			e.printStackTrace();
			return;
		}
		log.info(String.format("Deleted %d objects of class %s", objects.size(), clazz.getSimpleName()));
	}

	protected <E extends EntityBaseBean> void clearDatabase(Long id, Class<E> clazz) {
		try {
			E object = clazz.newInstance();
			object.setId(id);
			deleteObject(object);
		} catch (InstantiationException | IllegalAccessException e) {
			log.error(String.format("Error creating instance of class: %s", clazz.getSimpleName()));
			e.printStackTrace();
		} catch (IcatException_Exception e) {
			log.error(String.format("IcatException while deleting %s, id: %s", clazz.getSimpleName(), id));
			e.printStackTrace();
			return;
		}
		log.info(String.format("Deleted object of class %s", clazz.getSimpleName()));
	}

	protected void clearTable(String tableName) {
		try {
			String query = String.format("SELECT elem FROM %s elem where elem.createId = '%s/%s'", tableName,
					user.getAuthType(), user.getLogin());
			List<Object> objects = icat.search(sessionId, query);
			if (objects.size() > 0) {
				icat.deleteMany(sessionId, (List<EntityBaseBean>) (Object) objects);
				log.info(String.format("Deleted %d %s objects", objects.size(), tableName));
			}
		} catch (IcatException_Exception e) {
			log.error("IcatException while deleting");
			e.printStackTrace();
		}
	}

	protected void clearAllDatabase() {
		List<String> tables = Arrays.asList("Facility", "Application", "DataCollection", "DataCollectionDatafile",
				"DataCollectionDataset", "Datafile", "DatafileFormat", "Dataset", "DatasetType", "FacilityCycle",
				"Grouping", "Instrument", "InstrumentScientist", "Investigation", "InvestigationGroup",
				"InvestigationInstrument", "InvestigationType", "InvestigationUser", "Job", "Keyword", "Log",
				"ParameterType", "PermissibleStringValue", "Publication", "PublicStep", "RelatedDatafile", "Rule",
				"Sample", "SampleType", "Shift", "Study", "StudyInvestigation", "User", "UserGroup");
		try {
			for (String tableName : tables) {
				String query = String.format("SELECT elem FROM %s elem where elem.createId = '%s/%s'", tableName,
						user.getAuthType(), user.getLogin());
				List<Object> objects = icat.search(sessionId, query);
				if (objects.size() > 0) {
					icat.deleteMany(sessionId, (List<EntityBaseBean>) (Object) objects);
					log.info(String.format("Deleted %d %s objects", objects.size(), tableName));
				}
			}
		} catch (IcatException_Exception e) {
			log.error("IcatException while deleting");
			e.printStackTrace();
		}
	}

	protected void clearDatabase() {
		List<String> tables = Arrays.asList("Facility", "DataCollection", "DataCollectionDatafile",
				"DataCollectionDataset", "DatafileFormat", "Dataset", "Instrument", "Investigation",
				"InvestigationGroup", "InvestigationInstrument", "InvestigationType", "Job", "Log", "ParameterType",
				"Sample", "SampleType", "Datafile");
		try {
			for (String tableName : tables) {
				String query = String.format("SELECT elem FROM %s elem where elem.createId = '%s/%s'", tableName,
						user.getAuthType(), user.getLogin());
				List<Object> objects = icat.search(sessionId, query);
				if (objects.size() > 0) {
					icat.deleteMany(sessionId, (List<EntityBaseBean>) (Object) objects);
					log.info(String.format("Deleted %d %s objects", objects.size(), tableName));
				}
			}
		} catch (IcatException_Exception e) {
			log.error("IcatException while deleting");
			e.printStackTrace();
		}
	}

	private String getTitle(String camelString) {
		return camelString.replaceAll("select|create", "")
				.replaceAll("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])", " ").toLowerCase();
	}
}
