package apriori;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<ItemSet> transactions = new ArrayList<>();
        
        // Path to your transactions file
        String filePath = "transactions.txt";
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                // Trim, remove quotes, and split the line
                line = line.trim().replaceAll("\"", "").replace("Transaction: ", "");
                String[] items = line.split(",\\s*");

                // Debugging output
                System.out.println("Cleaned Transaction: " + Arrays.toString(items));

                transactions.add(new ItemSet(new HashSet<>(Arrays.asList(items))));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        double minSupport = 0.1;  // Lower the minimum support threshold
        double minConfidence = 0.5;  // Minimum confidence threshold
        Apriori apriori = new Apriori(transactions, minSupport, minConfidence);
        List<ItemSet> frequentItemsets = apriori.findFrequentItemsets();

        System.out.println("Frequent itemsets:");
        for (ItemSet itemSet : frequentItemsets) {
            System.out.println(itemSet);
        }

        List<String> associationRules = apriori.generateAssociationRules();

        System.out.println("Association Rules:");
        for (String rule : associationRules) {
            System.out.println(rule);
        }
    }
}
