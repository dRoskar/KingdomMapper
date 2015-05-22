package si.roskar.diploma.shared;


public class KingdomGridLayer extends KingdomLayer{
	private MapSize size = null;
	private int dbKey;

	public KingdomGridLayer(){
		super();
	}
	
	public KingdomGridLayer(int id, String name, String style, boolean visibility, String format, String projection, float opacity, MapSize size){
		super(id, name, style, visibility, format, projection, opacity);
		setSize(size);
	}
	
	public MapSize getSize(){
		return size;
	}

	public void setSize(MapSize size){
		this.size = size;
		
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
