package semsteg;

import java.util.ArrayList;
import java.util.List;

public class Embeder {

	public HashingFunction hashFunction;
	public Replacer replacer;
	public String alphabet;
	public int sectionSize;
	
	public Embeder (Replacer rep, String alpha, int size, HashingFunction hash) {
		hashFunction = hash;
		replacer = rep;
		alphabet = alpha;
		sectionSize = size;
	}
	
	public ParsedText embed (ParsedText cover, String hide) {
		// Split cover into fixed size sections
		List<List<TextPart>> sections = new ArrayList<List<TextPart>>();
		int currentLen = 0;
		List<TextPart> currentSection = new ArrayList<TextPart>();
		for (TextPart part : cover.parts) {
			currentSection.add(part);
			if (part instanceof Word) {
				currentLen ++;
				if (currentLen >= sectionSize) {
					sections.add(currentSection);
					currentLen = 0;
					currentSection = new ArrayList<TextPart>();
				}
			}
		}
		if (currentLen > 0) {
			sections.add(currentSection);
		}
		// Check if cover is big enough
		if (sections.size() < hide.length()) {
			System.out.println("WARNING: This cover message is too short, only a portion of the hidden message will be embeded.");
			hide = hide.substring(0, sections.size());
		}
		
		ParsedText result = new ParsedText();
		
		for (int i=0; i<hide.length(); i++) {
			List<TextPart> coverSection = sections.get(i);
			char hideChar = hide.charAt(i);
			boolean found = false;
			
			List<List<TextPart>> sectionReplacements = replacer.sectionReplacements(coverSection);
			
			System.out.print("Got ");
			System.out.print(sectionReplacements.size());
			System.out.print(" chances to find: ");
			System.out.println(hideChar);
			
			for(int j=0; j<sectionReplacements.size(); j++) {
				StringBuilder repBuild = new StringBuilder();
				for (int k=0; k<sectionReplacements.get(j).size(); k++) {
					repBuild.append(sectionReplacements.get(j).get(k));
				}
				
				char thisChar = hashFunction.hash(repBuild.toString());
				
				//System.out.print(repBuild.toString());
				//System.out.print(" ");
				//System.out.println(hashFunction.hash(repBuild.toString()));
				
				if (thisChar == hideChar) {
					found = true;
					for (int k=0; k<sectionReplacements.get(j).size(); k++) {
						result.addPart(sectionReplacements.get(j).get(k));
					}
					break;
				}
			}
			
			if (!found) {
				System.out.println("WARNING: A section did not have a replacement that allowed for the embedding of the hidden character.");
				return result;
			}
			
		}
		
		return result;
	}

}
