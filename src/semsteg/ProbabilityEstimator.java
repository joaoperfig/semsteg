package semsteg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ProbabilityEstimator {
	
	public HashMap<Integer, Double> probs;
	public List<Double> listprobs;
	public int maxReps;
	
	public ProbabilityEstimator () {
		// The following statistics were measured from safemode replacement table
		probs = new HashMap<Integer, Double>();
		probs.put(1, 96.25966291741197);
		probs.put(2, 2.040022334273538);
		probs.put(3, 0.7220085290641413);
		probs.put(4, 0.4876361089082472);
		probs.put(5, 0.1860798337970662);
		probs.put(6, 0.12477959488202513);
		probs.put(7, 0.10137923023193013);
		probs.put(8, 0.04378349030519205);
		probs.put(9, 0.012488016951519172);
		probs.put(10, 0.009772501856021043);
		probs.put(11, 0.006604400911273226);
		probs.put(12, 0.0023970287571372373);
		probs.put(13, 0.0022126419296651417);
		double total = 0;
		for (Map.Entry<Integer, Double> me : probs.entrySet()) {
	    	total += me.getValue();
	    }
		probs.put(14,  100-total );
		maxReps = probs.size();
		listprobs = new ArrayList<Double>();
		for (int i=1; i<=maxReps; i++) {
			listprobs.add(probs.get(i)/100);
		}
	}
	
	public double alternativesprob(int alternatives) {
		if (probs.containsKey(alternatives)) {
			return probs.get(alternatives)/100;
		}
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	public List<List<Integer>> multiplicativeCombinations(int words, int replacements) {
		List<Integer> primes = primeDeconstruct(replacements);
		int maxElements = primes.size();
		//System.out.print("primes ");
		//System.out.println(primes);
		
		List<List<Integer>> destinations = new ArrayList<List<Integer>>();
		
		List<Integer> current = new ArrayList<Integer>();
		for (int i=0; i<maxElements; i++) {
			current.add(0);
			if(primes.get(i) > maxReps) return new ArrayList<List<Integer>>(); // a prime is too big, no solutions exist
		}
		double maxCombinations = Math.pow(maxElements, maxElements);
		destinations.add(current);
		List<Integer> last;
		for (double count = 1; count < maxCombinations; count++) {
			last = current;
			current = new ArrayList<Integer>();
			for (int i=0; i<maxElements; i++) {
				current.add(last.get(i));
			}
			for (int i=0; i<maxElements; i++) {
				if (current.get(i) == maxElements-1) {
					current.set(i, 0);
				} else {
					current.set(i, current.get(i)+1);
					break;
				}
			}
			destinations.add(current);
		}
		
		List<List<Integer>> elements = new ArrayList<List<Integer>>();
		for (List<Integer> dest : destinations) {
			//System.out.println(dest);
			boolean overboard = false;
			current = new ArrayList<Integer>();
			for (int i = 0; i < maxElements; i++) {
				current.add(1);
			}
			int countfactors = 0;
			for (int i = 0; i < maxElements; i++) {
				int target = dest.get(i);
				int prime = primes.get(i);
				int multi = current.get(target)*prime;
				current.set(target, multi);
				if (multi > maxReps) {
					overboard = true;
					break;
				}
			}
			if (overboard) continue;
			elements.add(current);
			Collections.sort(current);
			//System.out.println(current);
		}
		Collections.sort(elements, new ListComparator());
		
		elements = new ArrayList<List<Integer>>(
			      new HashSet<List<Integer>>(elements));
		
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		
		for (List<Integer> element : elements) {
			//System.out.println(element);
			List<Integer> eset = new ArrayList<Integer>();
			for (int i=0; i<maxReps; i++) {
				eset.add(0);
			}
			for (Integer i : element) {
				eset.set(i-1, eset.get(i-1)+1);
			}
			result.add(eset);
			//System.out.println(eset);
		}	
		
		List<List<Integer>> finalresult = new ArrayList<List<Integer>>();
		for (List<Integer> eset : result) {
			int count = 0;
			List<Integer> finaleset = new ArrayList<Integer>();
			finaleset.add(0); // firstly assume no "1"s
			for (int i=1; i<eset.size(); i++) {
				finaleset.add(eset.get(i));
				count += eset.get(i);
			}
			//System.out.println(count);
			int onesNeeded = words - count;
			if (onesNeeded < 0) continue;
			finaleset.set(0, onesNeeded);
			finalresult.add(finaleset);
			//System.out.println(finaleset);
		}
		
		return finalresult;
	}
	
	@SuppressWarnings("unchecked")
	public double RP(int words, int replacements) {
		//System.out.println(replacements);
		List<List<Integer>> esets = multiplicativeCombinations(words, replacements);
		double total = 0;
		for (List<Integer> eset : esets) {
			//System.out.print(eset);
			//double res = multinomial(eset, words, listprobs);
			double res = effectiveMultinomial(eset, words, listprobs);
			//System.out.println(res);
			total += res;
		}
		
		return total;
	}
	
	public List<Integer> primeDeconstruct(int value){
        int n = value;
        List<Integer> factors = new ArrayList<Integer>();
        for (int i = 2; i <= n; i++) {
            while (n % i == 0) {
                factors.add(i);
                n /= i;
            }
        }
        return factors;
	}
	
	public double multinomial(List<Integer> results, int tests, List<Double> probabilities) {
		if (results.size() != probabilities.size()) System.out.println("ERROR: mismatching list sizes!");
		double result = factorial(tests);
		//System.out.print(tests);
		//System.out.print("! = ");
		//System.out.println(result);
		for (int i=0; i<results.size(); i++) {
			//System.out.print(result);
			//System.out.print("/");
			//System.out.print(factorial(results.get(i)));
			//System.out.print(" = ");
			result = result/factorial(results.get(i));
			//System.out.println(result);
			//System.out.print(result);
			//System.out.print("*(");
			//System.out.print(probabilities.get(i));
			//System.out.print("^");
			//System.out.print(results.get(i));
			//System.out.print(") = ");
			result = result*Math.pow(probabilities.get(i), results.get(i));
			//System.out.println(result);
		}
		return result;
	}
	
	public double effectiveMultinomial(List<Integer> results, int tests, List<Double> probabilities) {
		if (results.size() != probabilities.size()) System.out.println("ERROR: mismatching list sizes!");
		List<Double> multistack = new ArrayList<Double>();
		for (int i=0; i<results.size(); i++) {
			multistack.add(Math.pow(probabilities.get(i), results.get(i)));
		}
		return auxMultinomial(results, tests, multistack);
	}
	
	public double auxMultinomial(List<Integer> results, int tests, List<Double> multistack) {
		double result = 1;
		boolean done = true;
		if (tests > 1) {
			result = result*tests;
			done = false;
		}
		tests = tests - 1;
		List<Integer> newresults = new ArrayList<Integer>();
		for (int i=0; i<results.size(); i++) {
			int val = results.get(i);
			if (val > 1) {
				result = result/val;
				done = false;
			}
			newresults.add(val-1);
		}
		if (multistack.size() > 0) {
			result = result*multistack.get(0);
			multistack.remove(0);
			done = false;
		}
		if (done) return result;
		return result * auxMultinomial(newresults, tests, multistack);
	}
	
	
	public long factorial(long n) {
		if (n>20) System.out.println("WARNING:Trying to compute a factorial for a number larger than 20, WILL OVERFLOW!");
	    if (n == 0) {
	        return 1;
	    }
	    return n * factorialNoCheck(n - 1);
	}
	
	public long factorialNoCheck(long n) {
	    if (n == 0) {
	        return 1;
	    }
	    return n * factorialNoCheck(n - 1);
	}
	
	public List<Double> precompRP(int words){
		List<Double> res  = new ArrayList<Double> ();
		int upto = 512;
		double total = 0;
        for (int i=1; i<upto; i++) {
        	double val = RP(words, i);
        	total += val;
        	res.add(val);
        }
        res.add(1-total); // it is assumed that upto has the remaining probability
        return res;		
	}
	
	public double embeddingProb(int size, int alpha, List<Double> rp) {
		double total = 1;
		for (int i=0; i<rp.size(); i++) {
			int r = i+1; //there cannot be 0 replacements
			total *= Math.pow(f(alpha, r), rp.get(i));
		}
		return Math.pow(total, size);
	}
	
	public double f(int alphabet, int replacements) {
		return 1- Math.pow(1 - (1/((double)alphabet)), replacements);
	}

}
/*
1 -> 96.25966291741197 %
2 -> 2.040022334273538 %
3 -> 0.7220085290641413 %
4 -> 0.4876361089082472 %
5 -> 0.1860798337970662 %
6 -> 0.12477959488202513 %
7 -> 0.10137923023193013 %
8 -> 0.04378349030519205 %
9 -> 0.012488016951519172 %
10 -> 0.009772501856021043 %
11 -> 0.006604400911273226 %
12 -> 0.0023970287571372373 %
13 -> 0.0022126419296651417 %
14 -> 0.00031848633836089165 %
15 -> 0.00021791170519429426 %
16 -> 0.00023467414405539382 %
18 -> 3.352487772219912e-05 %
19 -> 0.00011733707202769691 %
21 -> 0.00011733707202769691 %
23 -> 5.0287316583298674e-05 %
25 -> 8.38121943054978e-05 % 
*/