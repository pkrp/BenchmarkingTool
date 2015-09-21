package uk.ac.rl.test;

import java.util.List;

import org.icatproject.ICAT;

import uk.ac.rl.test.model.ReportRow;
import uk.ac.rl.test.model.SchemaType;
import uk.ac.rl.test.model.User;

public class SearchTest extends AbstractTest {

	public SearchTest(SchemaType schemaType, ICAT icat, String sessionId, User user) {
		super(schemaType, icat, sessionId, user);
	}

	private void selectUsers() {
		String query = "SELECT user FROM User user ORDER BY user.name";
		searchForObjects(query);
	}

	/*ALEX*/
	private void selectInvestigationWithParameterTypesByInstrumentName(String name) {
		String query = String.format("SELECT inv FROM Investigation inv "
				+ "JOIN inv.investigationInstruments invInstr "
				+ "JOIN invInstr.instrument instr "
				+ "JOIN inv.datasets ds " 
				+ "JOIN ds.datafiles df "
				+ "JOIN df.parameters param " 
				+ "JOIN param.type type "
				+ "WHERE instr.name = '%s'", name);
		searchForObjects(query);
	}
	
	private void selectInvestigationByInstrumentName(String name) {
		String query = String.format("SELECT inv FROM Investigation inv "
				+ "JOIN inv.investigationInstruments invInstr "
				+ "JOIN invInstr.instrument instr "
				+ "WHERE instr.name = '%s'", name);
		searchForObjects(query);
	}
	
	/*WAYNE*/
	private void selectDatafilesByDatasetIdWithLimit(String id) {
		String query = String.format("SELECT datafile FROM Datafile datafile "
				+ "WHERE (datafile.dataset.id = '%s') "
				+ "ORDER BY datafile.name ASC LIMIT 0, 50", id);
		searchForObjects(query);
	}
	
	/*WAYNE*/
	private void selectDatafilesByDatasetId(String id) {
		String query = String.format("SELECT datafile FROM Datafile datafile "
				+ "WHERE (datafile.dataset.id = '%s') "
				+ "ORDER BY datafile.name", id);
		log.info(query);
		searchForObjects(query);
	}
	
	/*WAYNE*/
	private void selectDatafilesWithInvestigationsByDatasetIdWithLimit(String id) {
		String query = String.format("SELECT datafile FROM Datafile datafile "
				+ "WHERE (datafile.dataset.id = '%s') "
				+ "ORDER BY datafile.name "
				+ "INCLUDE datafile.dataset.investigation inv, inv.facility LIMIT 0, 50", id);
		searchForObjects(query);
	}
	
	private void selectDatafilesByInvestigationId(String id) {
		String query = String.format("SELECT datafile FROM Datafile datafile "
				+ "WHERE (datafile.dataset.investigation.id = '%s')", id);
		searchForObjects(query);
	}
	
	private void selectDatafilesSizeByInvestigationId(String id) {
		String query = String.format("SELECT sum(datafile.fileSize) FROM Datafile datafile "
				+ "WHERE (datafile.dataset.investigation.id = '%s')", id);
		searchForObjects(query);
	}
	
	private void selectDatafileParametersForDataset(String id) {
		String query = String.format("SELECT datafileParameter FROM DatafileParameter datafileParameter "
				+ "WHERE datafileParameter.datafile.dataset.id = %s ", id);
		searchForObjects(query);
	}
	
	private void selectDatafileByTypeNameAndInstrumentName(String typeName, String numericValue, String instrumentName) {
		String query = String.format("SELECT df FROM Datafile df "
				+ "JOIN df.parameters dp "
				+ "JOIN dp.type type "
				+ "JOIN df.dataset ds "
				+ "JOIN ds.investigation inv "
				+ "JOIN inv.investigationInstruments invInstr "
				+ "JOIN invInstr.instrument instr "
				+ "WHERE type.name = '%s' AND dp.numericValue = %s AND instr.name = '%s'", 
				typeName, numericValue, instrumentName);
		searchForObjects(query);
	}
	
	private void selectDatafileByDatafileName(String datafileName) {
		String query = String.format("SELECT df FROM Datafile df "
				+ "JOIN df.parameters dp "
				+ "JOIN dp.type type "
				+ "JOIN df.dataset ds "
				+ "JOIN ds.investigation inv "
				+ "JOIN inv.investigationInstruments invInstr "
				+ "JOIN invInstr.instrument instr "
				+ "WHERE df.name LIKE '%s%%'", datafileName);
		searchForObjects(query);
	}
	
	/*GROUP BY DOES NOT WORK!!!*/
	private void selectInvestigationsOrderedByDatafilesAmount() {
		String query = "SELECT inv FROM Investigation inv "
				+ "JOIN inv.datasets ds "
				+ "JOIN ds.datafiles df "
				+ "GROUP BY inv.id "
				+ "ORDER BY COUNT(df.id) DESC";
		searchForObjects(query);
	}

	public List<ReportRow> run() {
		switch (schemaType) {
			case ISIS:
				selectUsers();
				selectInvestigationWithParameterTypesByInstrumentName("EMU");
				selectDatafilesByDatasetIdWithLimit("24078846"); 
				selectDatafilesByDatasetId("24078846"); 
				selectDatafilesWithInvestigationsByDatasetIdWithLimit("24078846"); 
				selectDatafilesByInvestigationId("24080054"); 
				selectDatafilesSizeByInvestigationId("24080054"); 
				selectDatafileParametersForDataset("24080046"); 
				selectDatafileByTypeNameAndInstrumentName("run_number", "426", "LOQ");
				selectDatafileByDatafileName("LOQ12345");
				break;
			case DLS:
				selectUsers();
				selectInvestigationByInstrumentName("i03");
				selectDatafilesByDatasetIdWithLimit("878042539");
				selectDatafilesByDatasetId("697671818");
				selectDatafilesWithInvestigationsByDatasetIdWithLimit("747856539");
				selectDatafilesByInvestigationId("576504259"); 
				selectDatafilesSizeByInvestigationId("576504259");
				break;
		}		
		return results;
	}
}
