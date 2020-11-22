package Programming_Assignment_1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
class Itemset<I> {

    private final List<Set<I>> itemsetList;
    private final Map<Set<I>, Integer> supportMap;
    private final Map<Set<I>, Double> confidenceMap;
	private Map<Set<I>, List<I>> item1 = new HashMap<>();
	private Map<Set<I>, List<I>> item2 = new HashMap<>();
    private final double minS;
    private final int size;
    Itemset(List<Set<I>> items, Map<Set<I>, List<I>> i1, Map<Set<I>, List<I>> i2, Map<Set<I>, Integer> supportMap, Map<Set<I>, Double> confidenceMap, double min, int size) {
        this.itemsetList = items;
        this.supportMap = supportMap;
        this.confidenceMap = confidenceMap;
        this.minS = min;
        this.item1 = i1;
        this.item2 = i2;
        this.size = size;
    }

    public List<Set<I>> getItemsets() { return itemsetList; }
    public Map<Set<I>, Integer> getSupportMap() { return supportMap; }
    public Map<Set<I>, Double> getConfidenceMap() { return confidenceMap; }
    public double getMinSupp() { return minS; }
    public int getSize() { return size; }
	public List<I> getItem1(Set<I> itemset) { return item1.get(itemset); }
	public List<I> getItem2(Set<I> itemset) { return item2.get(itemset); }
    public double getSupport(Set<I> itemset) { return 1.0 * supportMap.get(itemset) / size * 100; }
    public double getConfidence(Set<I> itemset) {  return !confidenceMap.containsKey(itemset) ? 0.0 : confidenceMap.get(itemset); }
}