package uk.ac.rl.test;

import java.util.Random;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.icatproject.Datafile;
import org.icatproject.DatafileFormat;
import org.icatproject.DatafileParameter;
import org.icatproject.Dataset;
import org.icatproject.DatasetType;
import org.icatproject.Facility;
import org.icatproject.Investigation;
import org.icatproject.InvestigationType;
import org.icatproject.ParameterType;
import org.icatproject.Sample;
import org.icatproject.SampleType;

public class ObjectGenerator {
	private final static int SAMPLE_OBJECT_NUMBER = 0;

	public static Facility sampleFacility;
	public static InvestigationType sampleInvestigationType;
	public static Investigation sampleInvestigation;
	public static DatasetType sampleDatasetType;
	public static Sample sampleSample;
	public static SampleType sampleSampleType;
	public static Dataset sampleDataset;
	public static DatafileFormat sampleDatafileFormat;
	public static Datafile sampleDatafile;
	
	private static XMLGregorianCalendar getSampleDate() {
		XMLGregorianCalendar sampleDate = null;
		try {
			sampleDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(2015, 8, 01, 12, 1, 1, 0, 0);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		return sampleDate;
	}
	
	public static void generate() {
		sampleFacility = ObjectGenerator.getFacility();
		sampleInvestigationType = ObjectGenerator.getInvestigationType(sampleFacility);
		sampleInvestigation = ObjectGenerator.getInvestigation(SAMPLE_OBJECT_NUMBER, sampleFacility,
				sampleInvestigationType);
		sampleDatasetType = ObjectGenerator.getDatasetType(sampleFacility);
		sampleSampleType = ObjectGenerator.getSampleType(sampleFacility);
		sampleSample = ObjectGenerator.getSample(sampleInvestigation, sampleSampleType);
		sampleDataset = ObjectGenerator.getDataset(SAMPLE_OBJECT_NUMBER, sampleInvestigation, sampleDatasetType,
				sampleSample);
		sampleDatafileFormat = ObjectGenerator.getDatafileFormat(sampleFacility);
		sampleDatafile = ObjectGenerator.getDatafile(SAMPLE_OBJECT_NUMBER, sampleDataset, sampleDatafileFormat);
	}
	
	public static Facility getFacility() {
		Facility facility = new Facility();
		facility.setName("some facility name ");
		facility.setFullName("full facility name");
		facility.setDescription("description of sample facility");
		facility.setDaysUntilRelease(90);
		facility.setUrl("www.somefacilityurl.ac.uk");
		return facility;
	}
	
	public static DatafileFormat getDatafileFormat(Facility facility) {
		DatafileFormat datafileFormat = new DatafileFormat();
		datafileFormat.setFacility(facility);
		datafileFormat.setName("sample datafile format name");
		datafileFormat.setDescription("sample description of sample format name");
		datafileFormat.setVersion("some version of sample datafile format");
		datafileFormat.setType("some type of sample datafile format");
		return datafileFormat;
	}
	
	public static Sample getSample(Investigation investigation, SampleType sampleType) {
		Sample sample = new Sample();
		sample.setName("Sample sample name");
		sample.setInvestigation(investigation);
		sample.setType(sampleType);
		return sample;
	}
	
	public static SampleType getSampleType(Facility facility) {
		SampleType sampleType = new SampleType();
		sampleType.setFacility(facility);
		sampleType.setName("Sample sample type name");
		sampleType.setMolecularFormula("some molecular formula for sample sample type");
		sampleType.setSafetyInformation("sample safety information for sample sample type");
		return sampleType;
	}
	
	public static DatasetType getDatasetType(Facility facility) {
		DatasetType datasetType = new DatasetType();
		datasetType.setFacility(facility);
		datasetType.setName("sample dataset type");
		datasetType.setDescription("sample description of dataset type");
		return datasetType;
	}
	
	public static InvestigationType getInvestigationType(Facility facility) {
		InvestigationType investigationType = new InvestigationType();
		investigationType.setFacility(facility);
		investigationType.setName("sample investigation type");
		investigationType.setDescription("sample description of investigation type");
		return investigationType;
	}

/*	public static ParameterType getParameterType(int parameterTypeNumber, Datafile datafile, Facility facility, boolean applicableToDataCollection,
			boolean applicableToInvestigation, boolean applicableToSample, boolean applicableToDataset,
			boolean applicableToDatafile, ParameterValueType parameterValueType) {
		ParameterType parameterType = new ParameterType();
		parameterType.setName(String.format("parameterType%d", parameterTypeNumber));
		parameterType.setApplicableToDataCollection(applicableToDataCollection);
		parameterType.setApplicableToDatafile(applicableToDatafile);
		parameterType.setApplicableToDataset(applicableToDataset);
		parameterType.setApplicableToInvestigation(applicableToInvestigation);
		parameterType.setApplicableToSample(applicableToSample);
		parameterType.setDescription(String.format("some description for parameter type %d", parameterTypeNumber));
		parameterType.setFacility(facility);
		parameterType.setUnits("some units value");
		parameterType.setUnitsFullName("full name of some units name");
		parameterType.setValueType(parameterValueType);
		return parameterType;
	}*/

	public static Investigation getInvestigation(int investigationNumber, Facility facility,
			InvestigationType investigationType) {
		XMLGregorianCalendar sampleDate = getSampleDate();
		Investigation investigation = new Investigation();
		investigation.setFacility(facility);
		investigation.setDoi("some doi");
		investigation.setName(String.format("investigation %d", investigationNumber));
		investigation.setType(investigationType);
		investigation.setVisitId("visit id");
		investigation.setSummary(String.format("summary of investigation %d", investigationNumber));
		investigation.setTitle(String.format("tittle of investigation %d", investigationNumber));
		investigation.setStartDate(sampleDate);
		investigation.setReleaseDate(sampleDate);
		investigation.setEndDate(sampleDate);
		return investigation;
	}

	public static Dataset getDataset(int datasetNumber, Investigation investigation, DatasetType datasetType, Sample sample) {
		XMLGregorianCalendar sampleDate = getSampleDate();
		Dataset dataset = new Dataset();
		dataset.setName(String.format("dataset %d", datasetNumber));
		dataset.setComplete(false);
		dataset.setDescription(String.format("description of dataset %d", datasetNumber));
		//dataset.setDoi("some doi");
		dataset.setLocation(String.format("location of dataset %d", datasetNumber));
		dataset.setInvestigation(investigation);
		dataset.setType(datasetType);
		dataset.setEndDate(sampleDate);
		dataset.setStartDate(sampleDate);
		dataset.setSample(sample);
		return dataset;
	}

	public static Datafile getDatafile(int datafileNumber, Dataset dataset, DatafileFormat datafileFormat) {
		XMLGregorianCalendar sampleDate = getSampleDate();
		Datafile datafile = new Datafile();
		datafile.setName("datafile " + datafileNumber);
		datafile.setDataset(dataset);
		datafile.setChecksum("checksum");
		datafile.setDatafileCreateTime(sampleDate);
		datafile.setDatafileModTime(sampleDate);
		datafile.setDescription(String.format("description of datafile %d", datafileNumber));
		//datafile.setDoi("some doi");
		datafile.setFileSize(new Random().nextLong());
		datafile.setDatafileFormat(datafileFormat);
		datafile.setLocation(String.format("location of datafile %d", datafileNumber));
		return datafile;
	}

	public static DatafileParameter getDatafileParameter(int datafileParameterNumber, Datafile datafile,
			ParameterType parameterType) {
		XMLGregorianCalendar sampleDate = getSampleDate();
		DatafileParameter datafileParameter = new DatafileParameter();
		datafileParameter.setDatafile(datafile);
		datafileParameter.setDateTimeValue(sampleDate);
		datafileParameter.setNumericValue(new Random().nextDouble());
		datafileParameter.setStringValue(String.format("some string value for datafile parameter %d",
				datafileParameterNumber));
		datafileParameter.setType(parameterType);
		return datafileParameter;
	}
}
