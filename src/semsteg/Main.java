package semsteg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	
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
		if (args.length != 5) {
			System.out.println("Usage: java semsteg.Main coverfile plaintextfile alphabetfile sectionSize outfile");
			return;
		}
		
		String covertext = readFile(args[0]);	
		System.out.println("Read covertext of length ");
		System.out.println(covertext.length());
		String plainText = readFile(args[1]);
		System.out.println("Read plaintext of length ");
		System.out.println(plainText.length());
		String alphabet = readFile(args[2]);
		System.out.println("Read alphabet of length ");
		System.out.println(alphabet.length());
		int sectionSize = Integer.parseInt(args[3]);
		String outfile = args[4];
		
		Parser parser = new Parser();
		ParsedText parsed = parser.parse(covertext);
		Replacer replacer = new WordnetReplacer();
		HashingFunction hf = new HashCodeHasher(alphabet);
		Embeder embed = new Embeder(replacer, alphabet, sectionSize, hf);
		
		ParsedText result = embed.embed(parsed, plainText);
		
		writeFile(outfile, result.getText());	

	}

}
