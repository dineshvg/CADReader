/**
 * 
 */
package de.tudarmstadt.dik.express;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

/**
 * @author dinesh
 *
 */
public class ReferencedEntities {

	public static ArrayList<String> getReferencedEntities(HashMap<String, ArrayList<String>> master_slaves, 
			File[] fileList) throws IOException {			
			
		ArrayList<String> listOfEntities = new ArrayList<String>();
		Object[] filenameList = master_slaves.keySet().toArray();
		
		for(int i=0; i<filenameList.length; i++) {
			String fileNameString = (String) filenameList[i];
			
			for(File file : fileList) {
				String[] fileName = file.getName().split(Constants.ISO_REFACTOR);
				if(fileNameString.equals(fileName[0])) {
					System.out.println("Master found");					
					listOfEntities = searchForEntityReferences(file,master_slaves.get(fileNameString));
				}
			}			
		}
		return listOfEntities;
	}

	private static ArrayList<String> searchForEntityReferences(File file, ArrayList<String> arrayList) throws IOException {
		ArrayList<String> entityList = new  ArrayList<String>();
	     BufferedReader in = new BufferedReader (new FileReader (file));
	     String line;
	     int lineNbr = 0;
	     int[] startNbr = {0,0};	     
	     for(int i = 0; i<arrayList.size(); i++) {
	    	 int j = 0;
	    	 String slave = arrayList.get(i);
	    	 while ((line = in.readLine()) != null) {	
	    		 lineNbr ++;
		    	 if(line.contains(Constants.ENTITY_REFERENCE_FINDER)){
		    		 if(line.contains(slave)) {
		    			 startNbr[j] = lineNbr;	
		    			 System.out.println("Entities capture started");
		    			 String capturedEntities = captureEntities(file,startNbr[j]);
		    			 ArrayList<String> obtainedEntities =  disconnectEntitiy(capturedEntities);
		    			 entityList.addAll(obtainedEntities);
		    			 j++;		    			 		    	    			 
		    		 }		    		 
		    	 }
		     } in.close();
	     }
		return entityList;	 
	}

	private static ArrayList<String> disconnectEntitiy(String capturedEntities) {
		String[] rawBrokenEntity = capturedEntities.split(Constants.ENTITY_SEPERATOR);
		ArrayList<String> entity = new ArrayList<String>();
		for(int i=0; i<rawBrokenEntity.length; i++) {
			if(!rawBrokenEntity[i].equals("") && rawBrokenEntity[i].contains(Constants.ENTITY_START)) {				
				String rawEntity_temp = rawBrokenEntity[i].substring(3, rawBrokenEntity[i].lastIndexOf(","));
				//System.out.println("-->"+rawEntity_temp);
				if(rawEntity_temp.contains(Constants.ENTITY_WORD_AS)) {
					String[] rawEntityArray = rawEntity_temp.split(Constants.ENTITY_WORD_AS);
					String rawEntity = rawEntityArray[0].trim();
					//System.out.println(rawEntity);
					entity.add(rawEntity);
				} else {
					String[] rawEntityArray = rawEntity_temp.split(Constants.ENTITY_COMMA_SEPERATOR);
					String rawEntity = rawEntityArray[0].trim();
					//System.out.println(rawEntity);
					entity.add(rawEntity);	
				}			
			} else if(rawBrokenEntity[i].contains(Constants.ENTITY_WORD_AS)) {
				String[] rawEntityArray = rawBrokenEntity[i].split(Constants.ENTITY_WORD_AS);
				String rawEntity = rawEntityArray[0].trim();
				//System.out.println(rawEntity);
				entity.add(rawEntity);
			} else {
				String[] rawEntityArray = rawBrokenEntity[i].split(Constants.ENTITY_COMMA_SEPERATOR);
				String rawEntity = rawEntityArray[0].trim();
				//System.out.println(rawEntity);
				entity.add(rawEntity);
			}
		}
		return entity;
	}

	private static String captureEntities(File file, int i) throws IOException {
		String capturedLine = "";
		String tempLine = capturedLine+(String) FileUtils.readLines(file).get(i);
		while(!tempLine.equals("")) {
			i++;
			if(tempLine.contains(Constants.ENTITY_END)) {
				capturedLine = capturedLine +"-bazinga-"+ tempLine;
				return capturedLine;
			} else {
				capturedLine = capturedLine +Constants.ENTITY_SEPERATOR+ tempLine;
				tempLine = (String) FileUtils.readLines(file).get(i);
			}	
		}
		return "";	
	}	
}
