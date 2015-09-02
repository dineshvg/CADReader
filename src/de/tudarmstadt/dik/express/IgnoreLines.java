/**
 * 
 */
package de.tudarmstadt.dik.express;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dinesh
 *
 */
public class IgnoreLines {

	public static List lineNumbersToIgnore(File file, String original_exp) throws IOException {
		
		BufferedReader read = new BufferedReader(new FileReader(file));
		String line;
		ArrayList<Integer> ignoreLine = new ArrayList<Integer>();
		int lineNbr = 0;
		while((line = read.readLine()) != null) {
			lineNbr++;
			if(line.contains(Constants.ENTITY_REFERENCE_FINDER) && line.contains(original_exp)) {
				ignoreLine.add(Integer.valueOf(lineNbr));
			} else if (lineNbr!=0 && 
					ignoreLine.contains(Integer.valueOf(lineNbr-1)) && 
					!line.contains(Constants.ENTITY_END)) {
				ignoreLine.add(Integer.valueOf(lineNbr));
			}
		}
		
		return ignoreLine;
		
		
	}

}
