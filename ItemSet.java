package apriori;

import java.util.HashSet;
import java.util.Set;

public class ItemSet {
    private Set<String> items;
    
    public ItemSet() {
        items = new HashSet<>();
    }

    public ItemSet(Set<String> items) {
        this.items = items;
    }

    public void addItem(String item) {
        items.add(item);
    }

    public Set<String> getItems() {
        return items;
    }

    public ItemSet union(ItemSet other) {
        Set<String> newSet = new HashSet<>(items);
        newSet.addAll(other.items);
        return new ItemSet(newSet);
    }

    public boolean containsAll(ItemSet other) {
        return items.containsAll(other.getItems());
    }

    @Override
    public String toString() {
        return items.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ItemSet itemSet = (ItemSet) obj;
        return items.equals(itemSet.items);
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }
}
