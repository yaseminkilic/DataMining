package Programming_Assignment_1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
class AprioriAlgorithm<I> {

	private Map<Set<I>, Double> supports, confidences;
	private Map<Set<I>, Integer> supportsMap;  // Support count is the number of times an itemset.
	private Map<Set<I>, List<I>> item_set1, item_set2;
	AprioriAlgorithm(){
		supportsMap = new HashMap<>();
		supports = new HashMap<>(); 
		confidences = new HashMap<>();
		item_set1 = new HashMap<>();
		item_set2 = new HashMap<>();
	}

	
    /**
     * Computes the frequent itemset that is a member of the set of candidate 1-itemsets C1.
     * Scans all of the transactions to count the number of occurrences of each item
     * Set of frequent 1-itemsets that consists of the candidate 1-itemsets satisfying minimum support.
     * @param itemsL 		List of transactions.
     * @param supportM 		Map of support count for each item.
     * @param minS 		    minimum support
     * @return List of 1-itemsets.
     */
    private List<Set<I>> findItemsets(List<Set<I>> itemsetL, Map<Set<I>, Integer> supportL, double minS) {
        Map<I, Integer> m = new HashMap<>();
        for (Set<I> set : itemsetL) {
            for (I item : set) {
                Set<I> tmp = new HashSet<>(1);
                tmp.add(item);
                supportL.put(tmp, supportL.getOrDefault(tmp, 0) + 1);
                m.put(item, m.getOrDefault(item, 0) + 1);
            }
        }
        List<Set<I>> freqList = new ArrayList<>();
        for (Map.Entry<I, Integer> e : m.entrySet()) {
            if ( e.getValue() * 1.0 / m.size() >= minS) {
                Set<I> set = new HashSet<>(1);
                set.add(e.getKey());
                freqList.add(set);
            }
        }
        return freqList;
    }

    /**
     * Sort the itemsets
     * Generates candidates
     * @param m				The number of times each itemset.
     * @param itemset		List of source itemsets
     * @return List of candidates
     */
    @SuppressWarnings("unchecked")
	private List<Set<I>> apriori_generation(Map<Set<I>, Integer> m, List<Set<I>> itemset) {
        List<List<I>> list = new ArrayList<>(itemset.size());
        for (Set<I> set : itemset) {
            List<I> tmp = new ArrayList<>(set);
            Collections.<I>sort(tmp, COMPARE);
            list.add(tmp);
        }
        int size = list.size();
        List<Set<I>> generationList = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            for (int j = i + 1; j < size; ++j) {
                Set<I> candidate = mergeItems(m, list.get(i), list.get(j));
                if (candidate != null) {
                    generationList.add(candidate);
                	item_set1.put(candidate, list.get(i));
                	item_set2.put(candidate, list.get(j));
                }
            }
        }
        return generationList;
    }

    /**
     * Find the next itemset.
     * @param item1 	[item_set]
     * @param item2	[associative_item_set]
     * @return merged itemset
     */
    private Set<I> mergeItems(Map<Set<I>, Integer> map, List<I> item1, List<I> item2) {
        int length = item1.size();
        for (int i = 0; i < length - 1; ++i) {if (!item1.get(i).equals(item2.get(i))) { return null; }}
        if (item1.get(length - 1).equals(item2.get(length - 1))) { return null;}
        
        Set<I> mergedSet = new HashSet<>(length + 1);
        for (int i = 0; i < length - 1; ++i) {
            mergedSet.add(item1.get(i));
        }
        mergedSet.add(item1.get(length - 1)); 
        mergedSet.add(item2.get(length - 1));
        return mergedSet;
    }
    
    /**
     * Computes the itemsets because all subsets of a frequent itemset must also be frequent
     * @param candidates 	list of candidates
     * @param transaction   a transaction
     * @return list of itemsets
     */
    private List<Set<I>> findSubset(List<Set<I>> candidates, Set<I> transaction) {
        List<Set<I>> subsets = new ArrayList<>(candidates.size());
        for (Set<I> candidate : candidates) {
            if (transaction.containsAll(candidate)) {
                subsets.add(candidate);
            }
        }
        return subsets;
    }

    /**
     * Compounds all frequent candidates into a list.
     * @param candidatesL 	list of candidates.
     * @param supportsM 	map holding each itemset to its support count.
     * @param size    		# of transactions.
     * @return list of itemset
     */
    private List<Set<I>> getNext(List<Set<I>> candidatesL, Map<Set<I>, Integer> supportsM, double minS, int size) {
        List<Set<I>> nItemset = new ArrayList<>(candidatesL.size());
        for (Set<I> itemset : candidatesL) {
            if (supportsM.containsKey(itemset)) {
                int supportCount = supportsM.get(itemset);
                double support = 1.0 * supportCount / size * 100;
                if (support >= minS) {
                    nItemset.add(itemset);
                    this.supports.put(itemset, support);
                    double a = supports.getOrDefault(item_set1.get(itemset), minS*100);
                    double confidence = support/a;
                    this.confidences.put(itemset, confidence);
                }
            }
        }
        return nItemset;
    }
    
    /**
     * Find frequent itemsets using an iterative level-wise approach based on candidate generation.
     * Generates the frequent itemset data.
     * @param transactions 	list of transactions
     * @param minS 			minimum support count threshold
     * @return Object
     */
    public Itemset<I> applyApriori(List<Set<I>> transactions, double minS) {
        if (transactions.isEmpty()) {
        	System.err.println("Itemset list is empty.\nCheck your input!!!");
        	return null;
        }
        // Get the frequent of the set of candidate 1-itemsets, C1, then map each list of frequent k-itemsets.
        List<Set<I>> freqItemList = findItemsets(transactions, supportsMap, minS);
        Map<Integer, List<Set<I>>> kItemsetMap = new HashMap<>();
        kItemsetMap.put(1, freqItemList);

        double support = 0, confidence = 0;
        int k = 2;
        do { /* the cardinality of itemsets processed at each iteration of the following loop */
            // First generate the candidates.
            List<Set<I>> candidateList = apriori_generation(supportsMap, kItemsetMap.get(k - 1));
            for (Set<I> transaction : transactions) { /* scan this database of transactions for counts */
                List<Set<I>> candidateList2 = findSubset(candidateList, transaction); /* get the subsets of t that are candidates */
                for (Set<I> candidate : candidateList2) { /* increase # of candidates */
                    supportsMap.put(candidate, supportsMap.getOrDefault(candidate, 0) + 1);
                    support = 1.0 * supportsMap.get(candidate) / transactions.size() * 100;
                    if (support >= minS) {
                        this.supports.put(candidate, support);
                        confidence = support / supports.getOrDefault(item_set1.get(candidate), minS*100);
                        confidences.put(candidate, confidence);
                    }
                }
            }
            /* increase the count of candidate if its bigger than minimum support */
            kItemsetMap.put(k, getNext(candidateList, supportsMap, minS, transactions.size()));
            
        } while (!kItemsetMap.get(k++).isEmpty());
        /* return the object to obtain the output */ 
        return new Itemset<>(extract(kItemsetMap), item_set1, item_set2, supportsMap, confidences, minS, transactions.size());
    }

    private List<Set<I>> extract(Map<Integer, List<Set<I>>> m) {
        List<Set<I>> ret = new ArrayList<>();
        for (List<Set<I>> items : m.values()) {
            ret.addAll(items);
        }
        return ret;
    }
    
    @SuppressWarnings("rawtypes")
	private static final Comparator COMPARE = new Comparator() {
        @SuppressWarnings("unchecked")
		@Override
        public int compare(Object o1, Object o2) { return ((Comparable) o1).compareTo(o2); }
    };
}