/**
 * @author dinesh
 */
package de.tudarmstadt.dik.express;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Parser {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SdaiException 
	 */
	public static void main(String argv[]) throws IOException {
		
		
		/*
		 * Source : http://stackoverflow.com/questions/4852531
		 *          /find-files-in-a-folder-using-java
		 */
		
		File[] fileList = getFileList(Constants.DIR_PATH);/*"//home/dinesh/Semantic_Web_HiWi/express_file_dir"*/
		
		System.out.println("Getting express files for building....");
		
		FileExtractor extractor = new FileExtractor();
		HashMap<String, ArrayList<String>> master_slaves = extractor.getMasterSlave(fileList);				
		ArrayList<String> entityListFromMaster = ReferencedEntities.getReferencedEntities(master_slaves,fileList);
		OriginalEntities.updateEntityInFile(master_slaves,fileList,entityListFromMaster);
	}

	/**
	 * @desc Searches for *.exp files in the directory and returns those files as a list.
	 * @param dirPath
	 * @return fileList
	 * @throws Null pointer exception if no file is found
	 */
	   public static File[] getFileList(String dirPath) throws NullPointerException {
           File dir = new File(dirPath);   

           File[] fileList = dir.listFiles(new FilenameFilter() {
               public boolean accept(File dir, String name) {
                   return name.endsWith(".exp");
               }
           });
           
           return fileList;
       }

}
