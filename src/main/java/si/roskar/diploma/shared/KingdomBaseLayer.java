package si.roskar.diploma.shared;

public class KingdomBaseLayer extends KingdomLayer{
	
	private int dbKey;

	public KingdomBaseLayer(){
		super();
		this.dbKey = 487;
	}

	public KingdomBaseLayer(int id, String name, String style, boolean visibility, String format, String projection, float opacity){
		super(id, name, style, visibility, format, projection, opacity);
	}

	public int getDbKey(){
		return dbKey;
	}

	public void setDbKey(int dbKey){
		this.dbKey = dbKey;
	}
}
