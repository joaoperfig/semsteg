package semsteg;

import java.util.List;

public class Test {

	public static void main(String[] args) {
		Word word1 = new Word("World");
		Word word2 = new Word("hello");
		Word word3 = new Word("BIG");
		System.out.print(word1.lowercase); System.out.print(" ");
		System.out.print(word1.uppercase); System.out.print(" ");
		System.out.print(word1.originallyUpper); System.out.print(" "); System.out.println();
		System.out.print(word2.lowercase); System.out.print(" ");
		System.out.print(word2.uppercase); System.out.print(" ");
		System.out.print(word2.originallyUpper); System.out.print(" "); System.out.println();
		System.out.print(word3.lowercase); System.out.print(" ");
		System.out.print(word3.uppercase); System.out.print(" ");
		System.out.print(word3.originallyUpper); System.out.print(" "); System.out.println();
		String text = "Olá isto é todo um texto!!!";
		Parser p = new Parser();
		ParsedText pt = p.parse(text);
		System.out.println(pt.getText());
		System.out.println(pt.len());
		System.out.println("Loading Replacements");
		Replacer rep = new WordnetReplacer();
		System.out.println("Done loading");
		System.out.println("Replacements for speediness:");
		System.out.println(rep.replacements(new Word("speediness")));
		HashingFunction hf = new HashCodeHasher("01");
		System.out.println(hf.hash("The room was quite small"));
		System.out.println(hf.hash("The room was very small"));
		System.out.println(hf.hash("The room was pretty small"));
		Embeder embed = new Embeder(rep, "01", 10, hf);
		text = "The Fifth Dynasty's speediness marked the end of the great pyramid constructions with speediness during the Old Kingdom. Pyramids of the era were smaller and becoming more standardized, though intricate relief decoration also proliferated. Neferirkare's pyramid deviated from convention as it was originally built as a step pyramid: a design that had been antiquated after the Third Dynasty (26th or 27th century BC).[b] This was then encased in a second step pyramid with alterations intended to convert it into a true pyramid;[c] However, the pharaoh's death left the work to be completed by his successors. The remaining works were completed in haste, using cheaper building material.\r\n" + 
				"\r\n" + 
				"Because of the circumstances, Neferirkare's monument lacked several basic elements of a pyramid complex: a valley temple, a causeway, and a cult pyramid. Instead, these were replaced by a small settlement of mudbrick houses south of the monument from where cult priests could conduct their daily activities, rather than the usual pyramid town near the valley temple. The discovery of the Abusir papyri in the 1890s is owed to this. Normally, the papyrus archives would have been contained in the pyramid town where their destruction would have been assured. The pyramid became part of a greater family cemetery. The monuments to Neferirkare's consort, Khentkaus II; and his sons, Neferefre and Nyuserre Ini, are found in the surrounds. Though their construction began under different rulers, all four of these monuments were completed during the reign of Nyuserre. ";
		pt = p.parse(text);
		embed.embed(pt, "0101101001101010110111101011111111");
		
		
		ParsedText sentence = p.parse("He had likable speediness.");
		System.out.println(hf.hash("Computing replacements for 'He had likable speediness.'"));
		List<List<TextPart>> replacements = rep.sectionReplacements (sentence.parts);
		for (List<TextPart> replacement : replacements) {
			System.out.println(replacement);
		}
	}

}
