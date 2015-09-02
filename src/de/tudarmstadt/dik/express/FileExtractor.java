/**
 * 
 */
package de.tudarmstadt.dik.express;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dinesh
 *
 */
public class FileExtractor {

	public /*ArrayList<String>*/String refactorReferenceName(String reference) {
		String refactoredName = "";
		String temp[] = reference.split(Constants.REFERENCE_FINDER);
		if(temp.length!= 0 && temp.length>1) {
			String temp2[] = temp[1].split(Constants.ISO_REFACTOR);
			if(temp2.length!=0 && temp2.length > 1) {
				refactoredName = temp2[0];
			}
		}
		return refactoredName;
	}
	
	
	@SuppressWarnings("resource")
	public HashMap<String, ArrayList<String>> getMasterSlave(File[] fileList) throws IOException {
		
		PrintWriter logger = new PrintWriter(Constants.LOGGER_PATH, "UTF-8");
		HashMap<String, ArrayList<String>> master_slaves= new HashMap<String, ArrayList<String>>();
		
		for(File file : fileList) {
			String filename = file.getName().split(Constants.ISO_REFACTOR)[0];
			
			 FileReader fr;
			 fr = new FileReader (file);
		     BufferedReader br = new BufferedReader (fr);
		     String line;
		     ArrayList<String> references = new ArrayList<String>();
		     while ((line = br.readLine()) != null) {
		    	 String[] arr = line.split(Constants.REFERENCE_FINDER); 
		    	 if(arr.length!=0) {
		    		 if(Constants.TRUE_CASE==arr[0].trim().compareTo(Constants.MASTER_WORD)) {
		    			 logger.println("Match :: "+arr[0]);
		    			 if(arr.length>3) {
		    				 arr = line.split(Constants.ISO_FINDER);
		    				 String refactoredReference = "";
		    				 if(arr.length!=0)
		    					 refactoredReference = refactorReferenceName(arr[1].trim());
		    				 	 if(!refactoredReference.equals("")) {			    					
			    					 if(!filename.equals(refactoredReference)) {
			    						 System.out.println("Slave - reference :: "+refactoredReference);
			    						 references.add(refactoredReference);
			    					 }// refactor name and file name check			    				     
		    				 	 }// if refactor name is empty check
		    			 }// line checked if it has enough objects after reference has been found
		    		 }//check for REFERENCE word
		    	 }// line is checked for null after split based on spaces		    	  
		     }// file loop		     
		     references = deDuplicator(references);
		     if(!references.isEmpty()) {
		    	 System.out.println("Master - filename :: "+filename);
		    	 //System.out.println(references.size());
		    	 master_slaves.put(filename, references);
		     }
		    
        }//File list loop					
		return master_slaves;
		
	}
	
	/*
	 * Removing duplicate records by using Set interface
	 */
	public ArrayList<String> deDuplicator(ArrayList<String> duplicatedList) {
		ArrayList<String> unduplicatedList = new ArrayList<String>();
		Set<String> exDuplicator = new HashSet<String>();
		exDuplicator.addAll(duplicatedList);
		unduplicatedList.addAll(exDuplicator);
		return unduplicatedList ;
		
	}
}
