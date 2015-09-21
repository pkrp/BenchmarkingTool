package uk.ac.rl.test.model;

public enum SchemaType {
	DLS("DLS"), 
	ISIS("ISIS");
	
	private final String type;
	
	private SchemaType(final String type) {
		this.type = type;
	}
	
	@Override
    public String toString() {
        return type;
    }
}
