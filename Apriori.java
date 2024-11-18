package apriori;

import java.util.*;

public class Apriori {
    private List<ItemSet> transactions;
    private double minSupport;
    private double minConfidence;

    public Apriori(List<ItemSet> transactions, double minSupport, double minConfidence) {
        this.transactions = transactions;
        this.minSupport = minSupport;
        this.minConfidence = minConfidence;
    }

    public List<ItemSet> findFrequentItemsets() {
        List<ItemSet> frequentItemsets = new ArrayList<>();
        List<ItemSet> candidateItemsets = generateInitialCandidates();

        while (!candidateItemsets.isEmpty()) {
            List<ItemSet> validItemsets = new ArrayList<>();
            for (ItemSet candidate : candidateItemsets) {
                double supportValue = support(candidate);
                if (supportValue >= minSupport) {
                    frequentItemsets.add(candidate);
                    validItemsets.add(candidate);
                }
                System.out.println("Candidate: " + candidate + ", Support: " + supportValue);
            }
            System.out.println("Valid Itemsets: " + validItemsets);
            candidateItemsets = generateCandidates(validItemsets);
            System.out.println("New Candidates: " + candidateItemsets);
        }

        return frequentItemsets;
    }

    public List<String> generateAssociationRules() {
        List<String> rules = new ArrayList<>();
        List<ItemSet> frequentItemsets = findFrequentItemsets();

        for (ItemSet itemSet : frequentItemsets) {
            if (itemSet.getItems().size() > 1) {
                List<ItemSet> subsets = generateSubsets(itemSet);
                for (ItemSet subset : subsets) {
                    ItemSet remaining = itemSet.union(subset);
                    remaining.getItems().removeAll(subset.getItems());

                    if (!remaining.getItems().isEmpty()) {
                        double confidenceValue = confidence(subset, itemSet);
                        if (confidenceValue >= minConfidence) {
                            rules.add(subset + " => " + remaining + " (Confidence: " + confidenceValue + ")");
                        }
                    }
                }
            }
        }

        return rules;
    }

    private List<ItemSet> generateInitialCandidates() {
        Set<String> items = new HashSet<>();
        for (ItemSet transaction : transactions) {
            items.addAll(transaction.getItems());
        }

        List<ItemSet> initialCandidates = new ArrayList<>();
        for (String item : items) {
            ItemSet itemSet = new ItemSet();
            itemSet.addItem(item);
            initialCandidates.add(itemSet);
        }

        System.out.println("Initial Candidates: " + initialCandidates);
        return initialCandidates;
    }

    private List<ItemSet> generateCandidates(List<ItemSet> frequentItemsets) {
        List<ItemSet> candidates = new ArrayList<>();
        if (frequentItemsets.isEmpty()) {
            return candidates;
        }
        int size = frequentItemsets.get(0).getItems().size() + 1;

        for (int i = 0; i < frequentItemsets.size(); i++) {
            for (int j = i + 1; j < frequentItemsets.size(); j++) {
                ItemSet set1 = frequentItemsets.get(i);
                ItemSet set2 = frequentItemsets.get(j);

                Set<String> unionSet = new HashSet<>(set1.getItems());
                unionSet.addAll(set2.getItems());

                if (unionSet.size() == size) {
                    ItemSet candidate = new ItemSet(unionSet);
                    if (!candidates.contains(candidate)) {
                        candidates.add(candidate);
                    }
                }
            }
        }

        return candidates;
    }

    private double support(ItemSet itemSet) {
        int count = 0;
        for (ItemSet transaction : transactions) {
            if (transaction.containsAll(itemSet)) {
                count++;
            }
        }
        return (double) count / transactions.size();
    }

    private double confidence(ItemSet subset, ItemSet itemSet) {
        int subsetCount = 0;
        int itemSetCount = 0;

        for (ItemSet transaction : transactions) {
            if (transaction.containsAll(subset)) {
                subsetCount++;
            }
            if (transaction.containsAll(itemSet)) {
                itemSetCount++;
            }
        }

        return itemSetCount == 0 ? 0 : (double) itemSetCount / subsetCount;
    }

    private List<ItemSet> generateSubsets(ItemSet itemSet) {
        List<ItemSet> subsets = new ArrayList<>();
        List<String> itemsList = new ArrayList<>(itemSet.getItems());
        int n = itemsList.size();

        // Generate all non-empty subsets
        for (int i = 1; i < (1 << n); i++) {
            ItemSet subset = new ItemSet();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) > 0) {
                    subset.addItem(itemsList.get(j));
                }
            }
            subsets.add(subset);
        }

        return subsets;
    }
}
