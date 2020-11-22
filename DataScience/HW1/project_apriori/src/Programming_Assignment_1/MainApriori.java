package Programming_Assignment_1;

import java.util.List;
import java.util.Set;

public class MainApriori {

    public static void main(String[] args) {
        if (args.length < 3){
            System.out.println("The program must be execute with three arguments: minimum support, input file name, output file name.\n"
            		+ "You entered missing arguments!!!");
            System.exit(0);
        }

        AprioriAlgorithm<String> generator = new AprioriAlgorithm<>();
        InputOutputFile file = new InputOutputFile();
        List<Set<String>> itemsetList = file.readFile(args);
        Itemset<String> data = generator.applyApriori(itemsetList, Double.valueOf(args[0])/100);
        if(!file.writeOutputFile(data)){
        	 System.out.println("Output file wasnt written!!!");
        }
    }
}