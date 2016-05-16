public class Test {

	public static void main(String[] args) {
		FileInput.init();

		QPHash QPShakes = new QPHash();
		ChainingHash CHBacon = new ChainingHash();
		String[] bac = FileInput.readBacon();
		String[] sha = FileInput.readShakespeare();

		// Read in contents of Bacon and Shakespeare to respective hashes
		for (String s : bac) {
			CHBacon.insert(s);
		}
		for (String s : sha) {
			QPShakes.insert(s);
		}

		// Initialize square error and greatest difference computation variables
		double squaredError = 0.0;
		double greatestDifference = 0.0;
		String greatestKey = "";

		// Compute square error for contents of Shakespeare and all overlapping entries
		String key = QPShakes.getNextKey();
		while (key != null) {
			double shakeFreq = (double) QPShakes.findCount(key) / sha.length;
			double bacFreq = (double) CHBacon.findCount(key) / bac.length;
			double difference = Math.abs(shakeFreq - bacFreq);
			squaredError += Math.pow(difference, 2);
			if (difference > greatestDifference) { // Found word with larger frequency difference.
				greatestDifference = difference;
				greatestKey = key;
			}
			key = QPShakes.getNextKey();
		}

		// Compute square error for contents unique to Bacon
		key = CHBacon.getNextKey();
		while (key != null) {
			if (QPShakes.findCount(key) == 0) {
				double bacFreq = (double) CHBacon.findCount(key) / bac.length;
				if (bacFreq > greatestDifference) { // no need to compute difference here; will always be bacFreq.
					greatestDifference = bacFreq;
					greatestKey = key;
				}
				squaredError += Math.pow(bacFreq, 2);
			}
			key = CHBacon.getNextKey();
		}

		System.out.println("Total square error: " + squaredError);
		System.out.println("Most different word: " + greatestKey);
	}

}
