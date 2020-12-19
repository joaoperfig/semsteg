package semsteg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ReplacementStatistics {
	
static String readFile(String filename) {
		
		String everything = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e1) {
			System.out.print("WARNING: Could not find file: ");
			System.out.println(filename);
			return "";
		}
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    everything = sb.toString();
		} catch (Exception e) {
			System.out.print("WARNING: Something went wring with file: ");
			System.out.println(filename);
		}finally {
		    try {
				br.close();
			} catch (IOException e) {
				System.out.print("WARNING: Could not close file: ");
				System.out.println(filename);
			}
		}
		
		return everything;
		
	}
	
	static void writeFile(String filename, String content) {
		try {
			File myObj = new File(filename);
			if (myObj.createNewFile()) {
					System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("File already exists.");
			}
	    } catch (IOException e) {
	    	System.out.println("An error occurred.");
	    	e.printStackTrace();
	    }
		
		try (FileWriter myWriter = new FileWriter(filename)) {
		      myWriter.write(content);
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: java semsteg.ReplacementStatistics infile outfile");
			return;
		}
		
		String intext = readFile(args[0]);	
		System.out.println("Read input file of length ");
		System.out.println(intext.length());
		String outfile = args[1];
		
		Parser parser = new Parser();
		ParsedText parsed = parser.parse(intext);
		Replacer replacer = new WordnetReplacer();
		
		StringBuilder result = new StringBuilder();
		
		for (TextPart part : parsed.parts) {
			if (part instanceof Word) {
				result.append(replacer.replacements((Word)part).size());
				result.append(",");
			}
		}
		
		writeFile(outfile, result.toString());	
		System.out.println("Done!");
	}


}
