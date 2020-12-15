package semsteg;

import java.util.ArrayList;

public class Parser {
	
	public Parser() {
		
	}
	
	public ParsedText parse(String text){
		ParsedText parsed = new ParsedText ();
		StringBuilder partbuild = new StringBuilder();
		boolean isword = false;
		for (char ch : text.toCharArray()) {
			if (Character.isLetter(ch)) {
				if (isword) partbuild.append(ch);
				else {
					isword = true;
					parsed.addPart(new TextPart(partbuild.toString()));
					partbuild = new StringBuilder();
					partbuild.append(ch);
				}
			} else { // is not a letter
				if (isword) {
					isword = false;
					parsed.addPart(new Word(partbuild.toString()));
					partbuild = new StringBuilder();
					partbuild.append(ch);
				} else partbuild.append(ch);
			}
		}
		if (isword) parsed.addPart(new TextPart(partbuild.toString()));
		else parsed.addPart(new TextPart(partbuild.toString()));
		
		return parsed;
	}
	
	
}
