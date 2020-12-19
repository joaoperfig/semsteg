package semsteg;

import java.util.List;

public interface Replacer {
	
	public List<Word> replacements (Word original);
	
	public List<List<TextPart>> sectionReplacements (List<TextPart> section);
	
	public List<TextPart> setSectionReplace (List<TextPart> section);
	
	public List<TextPart> getNext();
	
	public int getTotalReplacements();
}
