package semsteg;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WordnetReplacer implements Replacer {
	public static String dataFile = "E:\\gits\\semsteg\\wordnet_parser\\parsed.txt";
	
	private Map<String, ArrayList<String>> replacements;
	
	private List<Integer> current;
	private List<List<TextPart>> wordReplacements;
	private int currentcount;
	private int totalReplacements;
	private int sectionSize;
	List<Integer> numReplacements;
	
	public WordnetReplacer() {
		try {
			replacements = new HashMap<String, ArrayList<String>>();
			File file = new File(dataFile); 
			BufferedReader br = null;
			br = new BufferedReader(new FileReader(file));
			String line; 
			while ((line = br.readLine()) != null) {
				//System.out.println(st); 
				if (line.length() < 2) continue;
				String linewords[] = line.split(",");
				replacements.put(linewords[0], new ArrayList<String>(Arrays.asList(linewords)));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}

	@Override
	public List<Word> replacements (Word original){
		if (!replacements.containsKey(original.lowercase)) {
			ArrayList<Word> words = new ArrayList<Word>();
			words.add(new Word(original.value));
			return words;
		}
		ArrayList<String> repstrings = replacements.get(original.lowercase);
		ArrayList<Word> words = new ArrayList<Word>();
		for (String repstring : repstrings) {
			Word word = new Word(repstring);
			if (original.originallyUpper) {
				word = new Word(repstring.substring(0, 1).toUpperCase() + repstring.substring(1).toLowerCase());
			}
			words.add(word);
		}
		return words;
	}

	@Override
	public List<List<TextPart>> sectionReplacements (List<TextPart> section) {
		List<List<TextPart>> result = new ArrayList<List<TextPart>>();
		
		setSectionReplace(section);
		
		List<TextPart> current = getNext();
		while (current != null) {
			result.add(current);
			current = getNext();
		}
		
		return result;
	}

	@Override
	public List<TextPart> setSectionReplace(List<TextPart> section) {
		sectionSize = section.size();
		wordReplacements = new ArrayList<List<TextPart>>();
		numReplacements = new ArrayList<Integer>();
		totalReplacements = 1;
		for (TextPart part : section) {
			if (part instanceof Word) {
				List<TextPart> theseReplacements = new ArrayList<TextPart> ();
				theseReplacements.addAll(replacements((Word) part));
				wordReplacements.add(theseReplacements);
				numReplacements.add(theseReplacements.size());
				totalReplacements *= theseReplacements.size();
				
			} else {
				List<TextPart> theseReplacements = new ArrayList<TextPart> ();
				theseReplacements.add(part);
				wordReplacements.add(theseReplacements);
				numReplacements.add(theseReplacements.size());
			}
		}
		
		//System.out.print(numReplacements);
		//System.out.println(totalReplacements);
		
		currentcount = 1;
		current = new ArrayList<Integer>();
		for(int i = 0; i<numReplacements.size(); i++) {
			current.add(0);
		}
		
		List<TextPart> curres = new ArrayList<TextPart>();
		for (int i=0; i<wordReplacements.size(); i++) {
			curres.add(wordReplacements.get(i).get(current.get(i)));
		}
			
		return curres;
	}

	@Override
	public List<TextPart> getNext() {

		currentcount++;
		
		if (currentcount > totalReplacements) return null;
		
		List<Integer> last = current;
		current = new ArrayList<Integer>();
		for (int i=0; i<sectionSize; i++) {
			current.add(last.get(i));
		}
		
		for (int i=0; i<sectionSize; i++) {
			if (current.get(i) == numReplacements.get(i)-1) {
				current.set(i, 0);
			} else {
				current.set(i, current.get(i)+1);
				break;
			}
		}
		
		List<TextPart> curres = new ArrayList<TextPart>();
		for (int i=0; i<wordReplacements.size(); i++) {
			curres.add(wordReplacements.get(i).get(current.get(i)));
		}
			
		return curres;
	}

	@Override
	public int getTotalReplacements() {
		return totalReplacements;
	}
}
