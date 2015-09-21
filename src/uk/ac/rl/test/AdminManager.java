package uk.ac.rl.test;

import org.icatproject.Datafile;
import org.icatproject.DatafileFormat;
import org.icatproject.Dataset;
import org.icatproject.DatasetType;
import org.icatproject.Facility;
import org.icatproject.ICAT;
import org.icatproject.Investigation;
import org.icatproject.InvestigationType;
import org.icatproject.Sample;
import org.icatproject.SampleType;

import uk.ac.rl.test.model.SchemaType;
import uk.ac.rl.test.model.User;

public class AdminManager extends AbstractTest {
	
	public AdminManager(SchemaType schemaType, ICAT icat, String sessionId, User user) {
		super(schemaType, icat, sessionId, user);
	}
	
	public void generateSampleData() {
		log.info("Generating sample data...");
		ObjectGenerator.generate();
		ObjectGenerator.sampleFacility.setId(createObject(ObjectGenerator.sampleFacility));
		ObjectGenerator.sampleInvestigationType.setId(createObject(ObjectGenerator.sampleInvestigationType));
		ObjectGenerator.sampleInvestigation.setId(createObject(ObjectGenerator.sampleInvestigation));
		ObjectGenerator.sampleDatasetType.setId(createObject(ObjectGenerator.sampleDatasetType));
		ObjectGenerator.sampleSampleType.setId(createObject(ObjectGenerator.sampleSampleType));
		ObjectGenerator.sampleSample.setId(createObject(ObjectGenerator.sampleSample));
		ObjectGenerator.sampleDataset.setId(createObject(ObjectGenerator.sampleDataset));
		ObjectGenerator.sampleDatafileFormat.setId(createObject(ObjectGenerator.sampleDatafileFormat));
		ObjectGenerator.sampleDatafile.setId(createObject(ObjectGenerator.sampleDatafile));
		log.info("Sample data created.");
	}
	
	public void deleteSampleData() {
		log.info("Deleting sample data...");
		if(ObjectGenerator.sampleInvestigationType != null) 
			clearDatabase(ObjectGenerator.sampleInvestigationType.getId(), InvestigationType.class);
		if(ObjectGenerator.sampleInvestigation != null)
			clearDatabase(ObjectGenerator.sampleInvestigation.getId(), Investigation.class);
		if(ObjectGenerator.sampleDatasetType != null)
			clearDatabase(ObjectGenerator.sampleDatasetType.getId(), DatasetType.class);
		if(ObjectGenerator.sampleDataset != null)
			clearDatabase(ObjectGenerator.sampleDataset.getId(), Dataset.class);
		if(ObjectGenerator.sampleDatafileFormat != null)
			clearDatabase(ObjectGenerator.sampleDatafileFormat.getId(), DatafileFormat.class);
		if(ObjectGenerator.sampleDatafile != null)
			clearDatabase(ObjectGenerator.sampleDatafile.getId(), Datafile.class);
		if(ObjectGenerator.sampleSample != null)
			clearDatabase(ObjectGenerator.sampleSample.getId(), Sample.class);
		if(ObjectGenerator.sampleSampleType != null)
			clearDatabase(ObjectGenerator.sampleSampleType.getId(), SampleType.class);
		if(ObjectGenerator.sampleFacility != null)
			clearDatabase(ObjectGenerator.sampleFacility.getId(), Facility.class);
		log.info("Sample data deleted.");
	}
}
