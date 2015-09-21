package uk.ac.rl.test;

import java.util.ArrayList;
import java.util.List;

import org.icatproject.Datafile;
import org.icatproject.DatafileFormat;
import org.icatproject.DatafileParameter;
import org.icatproject.Dataset;
import org.icatproject.ICAT;
import org.icatproject.Investigation;
import org.icatproject.ParameterType;

import uk.ac.rl.test.model.ReportRow;
import uk.ac.rl.test.model.SchemaType;
import uk.ac.rl.test.model.User;

public class CreateTest extends AbstractTest {

	public CreateTest(SchemaType schemaType, ICAT icat, String sessionId, User user) {
		super(schemaType, icat, sessionId, user);
	}

	private void createInvestigations(Long amount) {
		List<Investigation> investigations = new ArrayList<Investigation>();
		for (int i = 1; i <= amount; i++) {
			investigations.add(ObjectGenerator.getInvestigation(i, ObjectGenerator.sampleFacility, ObjectGenerator.sampleInvestigationType));
		}
		List<Long> ids = createObjects(investigations);
		// clearTable(Investigation.class.getSimpleName());
		clearDatabase(ids, Investigation.class);
	}

	private void createDatasetsForInvestigation(Long amount) {
		List<Dataset> datasets = new ArrayList<Dataset>();
		for (int i = 1; i <= amount; i++) {
			datasets.add(ObjectGenerator.getDataset(i, ObjectGenerator.sampleInvestigation, ObjectGenerator.sampleDatasetType, ObjectGenerator.sampleSample));
		}
		List<Long> ids = createObjects(datasets);
		// clearTable(Dataset.class.getSimpleName());
		clearDatabase(ids, Dataset.class);
	}

	private void createDatafilesForDataset(Long amount) {
		List<Datafile> datafiles = new ArrayList<Datafile>();
		DatafileFormat sampleDatafileFormat = getObject(DatafileFormat.class.getSimpleName(), (long) 1);
		for (int i = 1; i <= amount; i++) {
			datafiles.add(ObjectGenerator.getDatafile(i, ObjectGenerator.sampleDataset, sampleDatafileFormat));
		}
		List<Long> ids = createObjects(datafiles);
		// clearTable(Datafile.class.getSimpleName());
		clearDatabase(ids, Datafile.class);
	}

	private void createDatafilesParametersForDatafile(Long amount) {
		List<DatafileParameter> datafileParameters = new ArrayList<DatafileParameter>();
		String query = String.format("SELECT parameterType FROM ParameterType parameterType "
				+ "WHERE (parameterType.applicableToDatafile = true) LIMIT 0, %d", amount);
		List<Object> parameterTypes = runQuery(query);
		log.info(parameterTypes.size());
		for (int i = 0; i < amount; i++) {
			datafileParameters.add(ObjectGenerator.getDatafileParameter(i, ObjectGenerator.sampleDatafile,
					(ParameterType) parameterTypes.get(i)));
		}
		List<Long> ids = createObjects(datafileParameters);
		// clearTable(DatafileParameter.class.getSimpleName());
		// clearTable(ParameterType.class.getSimpleName());
		clearDatabase(ids, DatafileParameter.class);
	}

	public List<ReportRow> run() {
		switch (schemaType) {
			case ISIS:
				createInvestigations((long) 10);
				createDatasetsForInvestigation((long) 500);
				createDatafilesForDataset((long) 500);
				createDatafilesParametersForDatafile((long) 500);
				break;
			case DLS:
				createInvestigations((long) 10);
				//createDatasetsForInvestigation((long) 40);
				createDatafilesForDataset((long) 40);
				break;
		}
		return results;
	}
}