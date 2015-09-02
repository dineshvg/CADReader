/**
 * 
 */
package de.tudarmstadt.dik.express;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * @author dinesh
 *
 */
public class OriginalEntities {
	
	static boolean remover = false;
	
	public static void updateEntityInFile(HashMap<String, ArrayList<String>> master_slaves, File[] fileList, ArrayList<String> entityListFromMaster) 
			throws IOException {
		File output_file = new File("express_long.exp");
		BufferedWriter writer = new BufferedWriter(new FileWriter(output_file));		
		Object[] masterList = master_slaves.keySet().toArray();
		// #Obtained slave file name
		for(int cnt=0; cnt<masterList.length;cnt++) {
			String reference_exp = (String) masterList[cnt];			
			ArrayList<String> original_exp_list = master_slaves.get(reference_exp);
			for(int cnt_2 = 0; cnt_2<original_exp_list.size(); cnt_2++) {
				String original_exp = original_exp_list.get(cnt_2);
				//System.out.println("Original "+original_exp);
				// #Obtain slave file
				for(File file : fileList) {
					String[] fileName = file.getName().split(Constants.ISO_REFACTOR);
					if(reference_exp.equals(fileName[0])) {
						System.out.println("Writing Master file "+ fileName[0]+ " into long file");
						writeShortDataToFile(file,output_file,writer,original_exp);
					}
					else if(original_exp.equals(fileName[0])) {
						System.out.println("Slave file for "+ reference_exp + " is "+fileName[0]);
						searchForEntitiesOnFile(file,entityListFromMaster,writer,output_file);
					}
				}
			}
		}writer.close();
	}

	private static void writeShortDataToFile(File file, File output_file,
			BufferedWriter writer, String original_exp) throws IOException {
		
		BufferedReader read = new BufferedReader(new FileReader(file));
		//BufferedReader tempRead = read;
		//Get the line numbers we need to ignore
		List lineNumbers = IgnoreLines.lineNumbersToIgnore(file,original_exp);
		int lineNbr = 0;
		String line;
		//int end_cnt = 0;
		//int rem_flag = -1;
		while ((line = read.readLine()) != null) {
			lineNbr++;
			if(!lineNumbers.contains(Integer.valueOf(lineNbr))) {
				writer.write(line);writer.newLine();
			}
			
			/*
			if(rem_flag==-1) {
				end_cnt = removeReferencesForRealData(tempRead,line,original_exp);
				//System.err.println(line+"  END COUNT "+ end_cnt);
			}						
			if(end_cnt == 0) {
				writer.write(line);writer.newLine();
			} else if(end_cnt > 0){	
				for (int i = 0; i<end_cnt; i++) {					
				}
				//while((line = read.readLine())!=null || end_cnt!=0) {
					end_cnt--;
					if(end_cnt==0) {
						rem_flag = -1;
					} else {
						rem_flag = 1;
					}
				//}
					
				
			}
		*/} read.close();
	}

	/*private static int removeReferencesForRealData(BufferedReader tempRead, String line, 
			String original_exp) throws IOException {
		
		if(line.contains(Constants.ENTITY_REFERENCE_FINDER) && line.contains(original_exp)) {						
			int end_cnt = 0;
			end_cnt = findReferenceEnd(tempRead);
			//remover = true;	
			return end_cnt;
		} else if(line.contains(Constants.ENTITY_END)){				
			//remover = false;
			return 0;
		} else {
			return 0;
		}
	}*/

	/*private static int findReferenceEnd(BufferedReader tempRead) throws IOException {
		// Finds the end of the reference for the REF_FROM -- ISO XXX on the file
		int end_cnt = 0;
		while(!tempRead.readLine().contains(Constants.ENTITY_END)) {
			//System.err.println("temp read lines "+ tempRead.readLine());
			end_cnt ++;
		}
		System.err.println("END_CNT "+end_cnt);
		return end_cnt;		
	}*/

	private static void searchForEntitiesOnFile(File file,
			ArrayList<String> entityListFromMaster, BufferedWriter writer,File output_file) throws IOException {		
		for(String entity_name : entityListFromMaster) {
			scanFile(entity_name,file,writer,output_file);
		}
	}

	@SuppressWarnings("resource")
	private static void scanFile(String entity_name, File file, BufferedWriter writer,File output_file) throws IOException {
		BufferedReader in = new BufferedReader (new FileReader (file));
		 String line; int lineNbr = 0;
		 while ((line = in.readLine()) != null) {	
			 lineNbr++;
			 if(line.contains(Constants.ENTITY_SEARCH_START+entity_name)) {
				 writer.write(line); writer.newLine();
				 storeEntityValues(file,lineNbr,writer,output_file);
			 }
		 }
	}

	private static void storeEntityValues(File file, int lineNbr,BufferedWriter writer, File output_file) throws IOException {
		
		String tempLine = (String) FileUtils.readLines(file).get(lineNbr-1);
		while(!tempLine.isEmpty()) {			
			if(tempLine.contains(Constants.ENTITY_SEARCH_END)) {
				//System.out.println(tempLine);				
				//writer.write(tempLine);
				writer.newLine();
				writer.newLine();				
				return;
			} else {				
				//System.out.println(tempLine);
				tempLine = (String) FileUtils.readLines(file).get(lineNbr);
				//writer = new BufferedWriter(new FileWriter(output_file));
				writer.write(tempLine);
				writer.newLine();
			}
			lineNbr++;
		}
	}
}
