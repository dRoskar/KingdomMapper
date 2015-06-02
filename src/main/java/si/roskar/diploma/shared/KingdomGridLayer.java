package si.roskar.diploma.shared;


public class KingdomGridLayer extends KingdomLayer{
	private MapSize mapSize = null;
	private int dbKey;

	public KingdomGridLayer(){
		super();
	}
	
	public KingdomGridLayer(int id, String name, String style, boolean visibility, String format, String projection, float opacity, MapSize size){
		super(id, name, style, visibility, format, projection, opacity);
		setMapSize(size);
	}
	
	public MapSize getMapSize(){
		return mapSize;
	}

	public void setMapSize(MapSize size){
		this.mapSize = size;
		
		switch(size){
			case TOWN_MAP:
				this.dbKey = 11;
				break;
			case COUNTRY_MAP:
				this.dbKey = 11;
				break;
			case CONTINENT_MAP:
				this.dbKey = 11;
				break;
		}
	}
	
	public int getDBKey(){
		return dbKey;
	}
}
