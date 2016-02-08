package de.tudresden.slr.model.taxonomy.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class ModelComparer extends EcoreUtil.EqualityHelper {
	private static final long serialVersionUID = 1770395065328356008L;

	/**
	 * String based comparison of two objects
	 * @author Jorge Manrubia
	 * http://jorgemanrubia.com/2008/07/06/comparing-emf-models/
	 */
	private class EObjectComparator implements Comparator<EObject> {
		public int compare(EObject object1, EObject object2) {
			String targetString1 = extractComparisonString(object1);
			String targetString2 = extractComparisonString(object2);

			return targetString1.compareTo(targetString2);
		}

		private String extractComparisonString(EObject object) {
			return object.toString().replaceAll(
					object.getClass().getName(), "").replaceAll(
							Integer.toHexString(object.hashCode()), "");
		}
	}

	
	/**
	 * Compares to objects and their descendants recursively.
	 * Returns true if the objects and their descendants are structurally equal.
	 * Order of descendants is not taken into account.
	 */
	public boolean equals(EObject obj1, EObject obj2){
		if(obj1 == null || obj2 == null){
			return false;
		}
		Comparator<EObject> comparator = new EObjectComparator();
		if(comparator.compare(obj1, obj2) != 0 || obj1.eContents().size() != obj2.eContents().size()){
			return false;
		}

		List<EObject> sortedList1 = new ArrayList<EObject>(obj1.eContents());
		List<EObject> sortedList2 = new ArrayList<EObject>(obj2.eContents());
		Collections.sort(sortedList1, comparator);
		Collections.sort(sortedList2, comparator);
		
		for(int i = 0; i < sortedList1.size(); i++){
			if(!equals(sortedList1.get(i), sortedList2.get(i))){
				return false;
			}
		}
		
		return true;
	}
}