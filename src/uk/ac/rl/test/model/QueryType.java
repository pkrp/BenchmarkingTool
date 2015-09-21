package uk.ac.rl.test.model;

public enum QueryType {
	CREATE("CREATE"), 
	SELECT("SELECT");
	
	private final String type;
	
	private QueryType(final String type) {
		this.type = type;
	}
	
	@Override
    public String toString() {
        return type;
    }
}
