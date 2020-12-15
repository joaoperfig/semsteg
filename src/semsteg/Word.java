package semsteg;

public class Word extends TextPart {
	
	public String lowercase;
	public String uppercase;
	public boolean originallyUpper;
	
	public Word(String content) { // assumes word has at most one uppercase at the start
		super(content);
		lowercase = content.toLowerCase();
		uppercase = content.substring(0, 1).toUpperCase() + content.substring(1).toLowerCase();
		originallyUpper = content.substring(0, 1).equals(content.substring(0, 1).toUpperCase());
	}

}
