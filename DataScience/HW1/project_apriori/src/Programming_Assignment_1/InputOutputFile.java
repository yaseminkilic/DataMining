package Programming_Assignment_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
class InputOutputFile {
	
	private String outputFile = "";
	List<Set<String>> readFile(String[] args){
		this.outputFile = args[2];
		
        List<Set<String>> itemsetList = new ArrayList<>();
        BufferedReader bufferedReader = null;
		String line;
		try {
			bufferedReader = new BufferedReader(new FileReader(args[1]) );
			while( (line = bufferedReader.readLine()) != null ){
				String splitedArr[] = line.split("\t");
				itemsetList.add(new HashSet<>(Arrays.asList(splitedArr)));
        	}
		}catch (Exception e){
			e.printStackTrace();
        }finally {
	            try {
	                if (bufferedReader != null) {
	                    bufferedReader.close();
	                }
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	    }
		return itemsetList;
	}
	
	boolean writeOutputFile(Itemset<String> data){
		FileWriter writer = null;
        BufferedWriter output = null;
		try {
	    	File f2 = new File( outputFile );
			if(f2.exists())  f2.delete();
			
			writer = new FileWriter(f2);
	        output = new BufferedWriter(writer);
	        for (Set<String> itemset : data.getItemsets()) { 
	        	if(data.getItem1(itemset) != null){
		        	output.write(format(data.getItem1(itemset)) + "\t" + format(data.getItem2(itemset)) + "\t" + String.format("%.2f", data.getSupport(itemset)) + "\t" + String.format("%.2f", data.getConfidence(itemset))); 
		        	output.newLine();
	        	}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (output != null) output.close();
				if (writer != null) writer.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return true;
	}
	
	private String format(List<String> list){
		String[] arr = list.toString().split("\\[|\\]");
		String str = ("{" + arr[1] + "}");
		return str;
	}
}
