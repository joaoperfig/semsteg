package semsteg;

import java.util.ArrayList;
import java.util.List;

public class Extractor {
	
	public HashingFunction hashFunction;
	public String alphabet;
	public int sectionSize;
	
	public Extractor (String alpha, int size, HashingFunction hash) {
		hashFunction = hash;
		alphabet = alpha;
		sectionSize = size;
	}
	
	public String extract (ParsedText stego) {
		// Split stego into fixed size sections
		List<List<TextPart>> sections = new ArrayList<List<TextPart>>();
		int currentLen = 0;
		List<TextPart> currentSection = new ArrayList<TextPart>();
		for (TextPart part : stego.parts) {
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
		if (currentSection.size() > 0) {
			sections.add(currentSection);
		}
		StringBuilder resBuild = new StringBuilder();
		for (List<TextPart> section : sections) {
			StringBuilder repBuild = new StringBuilder();
			for (int k=0; k<section.size(); k++) {
				repBuild.append(section.get(k));
			}
			char thisChar = hashFunction.hash(repBuild.toString());
			resBuild.append(thisChar);
		}
		return resBuild.toString();		
	}

}
