package si.roskar.diploma.shared;

import java.util.Comparator;

public class LayerZIndexComparator implements Comparator<KingdomLayer>{

	@Override
	public int compare(KingdomLayer o1, KingdomLayer o2){
		return ((Integer)o1.getZIndex()).compareTo((Integer)o2.getZIndex());
	}
}
