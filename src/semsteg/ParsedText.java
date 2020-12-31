package semsteg;

import java.util.ArrayList;
import java.util.List;

public class ParsedText {
	public List<TextPart> parts;
	public int wordNum;
	
	public ParsedText() {
		parts = new ArrayList<TextPart>();
	}
	
	public String getText() {
		StringBuilder build = new StringBuilder();
		for (TextPart part : parts) {
			build.append(part.value);
		}
		return build.toString();
	}
	
	public void addPart(TextPart part) {
		parts.add(part);
	}
	
	public int len() {
		return parts.size();
	}
	
	public int wordCount() {
		int count = 0;
		for (TextPart part : parts) {
			if (part instanceof Word) count++; 
		}
		return count;
	}
	
	public ArrayList<Word> words (){
		ArrayList<Word> res = new ArrayList<Word>();
		for (TextPart part : parts) {
			if (part instanceof Word) res.add((Word)part);
		}
		return res;
	}
	
	public String latexHighlight(String color, Replacer rep) {
		StringBuilder build = new StringBuilder();
		for (TextPart part : parts) {
			if (part instanceof Word) {
				Word word = (Word) part;
				if (rep.replacements(word).size()>1) { // \textcolor{red}{b} 
					build.append("\\textcolor{");
					build.append(color);
					build.append("}{");
					build.append(part.value);
					build.append("}");
					
				} else {
					build.append(part.value);
				}
			} else {
				build.append(part.value);
			}
		}
		return build.toString();
	}
}
