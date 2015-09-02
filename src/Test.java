import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/* Commented them as they are never used.
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
*/

/**
 * Hello UKP candidate.
 * The following program should read an arbitrary number of files from a directory and store all their tokens in a member variable.
 * The program may contain some bugs interfering with the desired functionality.
 * Your tasks are:
 * 1. Understand the program and correct all errors that lead to erroneous behaviour. Comment your changes and shortly explain the reasons.
 * 2. The program also contains some questionable programming constructs (e.g. disregarding Java naming conventions, etc.).
 *    Try to find as many as you can, and correct them. Comment your changes and shortly explain the reasons.
 * 3. Add the missing JavaDocs at a level of detail that you consider as appropriate.
 * 4. Write a <b>single</b> method that <b>returns</b> the number of items in tokenMap, the average length (as double value) of the elements in tokenMap after calling applyFilters(), and the number of tokens starting with "a" (case sensitive).
 *    Output this information.
 *
 * @author zesch
 *
 */

/**
 * This class reads all files from a directory and store all their tokens in a member variable
 *
 */
public class Test {

    public final static String CHARSET = "ISO-8859-1";

    public File inputDir;

    // Never used, so commented it
    // public double nrofFiles;

    public int minimumCharacters;
    public int maximumCharacters;

    // Created a new instance of HashMap to avoid null pointer exception
    HashMap<String, Integer> tokenMap = new HashMap<String, Integer>();

    
	public Test(File pInputDir, int pMinChars, int pMaxChars) {

        inputDir = pInputDir;
        minimumCharacters = pMinChars;
        maximumCharacters = pMaxChars;

        //Invalid parameters such as Minchars/MaxChars less than 0 or MaxChars < Minchars can still be passed
       // if ((pMinChars == 0) || (pMaxChars == 0)) {
        if ((pMinChars <= 0) || (pMaxChars <= 0) || (pMaxChars < pMinChars)) {
            throw new RuntimeException("Configuration parameters have not been correctly initialized.");
        }
    }
    /**
     * This method runs all the private functions present in the class and creates the output
     */
    public void run() {
        readFiles();
        applyFilters();
        outputTokens();
        outputTokenMapDetails();
    }

    /**
     * This method reads files from a directory one by one and adds tokens found to tokenMap
     */
	private void readFiles() {
		
		//changed files as directory because it is about checking whether any files exist inside the folder
        //File[] files = inputDir.listFiles();
		File[] directory = inputDir.listFiles();
        // Changed the if condition because if the folder is empty the previous condition was not satisfying the check
        // if (files == null) 
        if (directory.length == 0) {
            System.err.println("Filelist is empty. Directory: " + inputDir.getAbsolutePath());
            System.exit(1);
        }

        for (int i = 0; i < directory.length; i++) {
            File file = new File(directory[i].getAbsoluteFile().toString());

            if (file.length() == 0) {
                System.out.println("Skipping emtpy file " + file.getAbsolutePath());
                continue;
            }

            //System.out.println(getFileTokens(file));

            // Assignment operator overwrites the tokenMap resulting in erroneous behavior(tokens of only last file preserved)
           //tokenMap = getFileTokens(file);
            tokenMap.putAll(getFileTokens(file));
           // tokenMap.put(CHARSET, getFileTokens(file[i]));
          // System.out.println("tokenMap output" +tokenMap);
            

        }
    }

    /**
     * This function gets the tokens present in the file passed as input
     * @param  infile  File in which tokens are to be found
     * @return         HashMap of tokens and the corresponding count
     */
    private HashMap<String, Integer> getFileTokens(File infile) {

        HashMap<String, Integer> fileTokens = new HashMap<String, Integer>();

        BufferedReader in;
        String line;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(infile), CHARSET));
            while ((line = in.readLine()) != null) {
            	// Changed lineParts to wordParts as it separates the words using space as the regular expression
                //String lineParts[] = line.split(" ");
            	String wordParts[] = line.split(" ");
            	for(String part : wordParts) {
                	//System.out.println("This is where you are"+part);
                	System.out.println(fileTokens.equals(part));
                    if (fileTokens.equals(part)) {
                    	System.out.println("You have reached here1");
                        fileTokens.put(part, fileTokens.get(part) + 1);
                        System.out.println("You have reached here0");
                    }
                    else {
                        	fileTokens.put(part, 1);
                        	
                    }
                }
            }
            in.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        
        
        return fileTokens;
    }

    /**
     * This method removes all the tokens which do not comply with min/max chars length
     */
    private void applyFilters() {
        try {
            for (String token : tokenMap.keySet()) {
                if (token.length() < minimumCharacters || token.length() > maximumCharacters) {
                    tokenMap.remove(token);
                }
            }
        }
        catch(Exception e) {}
    }
    /**
     * This method prints the tokenMap line by line
     */
    private void outputTokens() {
        String output = "";
        for (String token : tokenMap.keySet()) {
        	//System.out.println(tokenMap.keySet());
        	//System.out.println(token);
            output += token + "\n";
            
        }
        //System.out.println(output);
    }

    /**
     * This method prints the average token size and the total no of tokens found.
     */
    private void outputTokenMapDetails() {
        Integer size   = 0;
        Double  avglen = 0.0;

        for (String token : tokenMap.keySet()) {
            avglen += token.length();
        }
        size   = tokenMap.size();
        avglen = avglen / size ;
        System.out.println("Number of items in tokenMap : " + size + " and average length of the elements : " + avglen );
    }


    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Wrong number of parameters: java <application> <indir> <minChars> <maxChars>");
            System.exit(1);
        }

        File inputDir = new File(args[0]);
        int minChars = new Integer(args[1]);
        int maxChars = new Integer(args[2]);

        Test pst = new Test(inputDir, minChars, maxChars);
        pst.run();
    }
}