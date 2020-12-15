package semsteg;

public class HashCodeHasher implements HashingFunction {
	// HashingFunction using Java's hashcode
	
	String alphabet;
	
	public HashCodeHasher(String alpha) {
		alphabet = alpha;
	}

	@Override
	public char hash(String s) {
		int code = s.hashCode();
		code = Math.abs(code) % alphabet.length();
		return alphabet.charAt(code);
	}
	
}
