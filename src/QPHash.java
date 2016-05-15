/**
 * QPHash provides the client with hashing capabilities for keys of type String.
 * Primary purpose of QPHash is to count and store the occurrences of unique words in works
 * of literature.
 *
 * @author Evan Gordon
 * Created for University of Washington CSE 373. Last Modified May 14th, 2016.
 */

public class QPHash {
	public static final int DEFAULT_TABLE_SIZE = 12553; // Prime default hashTable size
	public static final int HASH_CONSTANT = 229; // Prime multiplicative constant for hash function
	private QPNode[] hashTable;
	private int tableIndex; // Tracks current index for getNextKey()

	/**
	 * Instantiates a QPHash with the default hash table size.
	 */
	public QPHash(){
		this(DEFAULT_TABLE_SIZE);
	}

	/**
	 * Instantiates a QPHash with the desired hash table size.
	 * @param startSize : Desired size of hash table
	 */
	public QPHash(int startSize){
		hashTable = new QPNode[startSize];
		tableIndex = 0;
	}

	/**
	 * This function allows rudimentary iteration through the QPHash.
	 * The ordering is not important so long as all added elements are returned only once.
	 * It should return null once it has gone through all elements
	 * @return Returns the next element of the hash table. Returns null if it is at its end.
	 */
	public String getNextKey(){
		while (tableIndex < hashTable.length) {
			if (hashTable[tableIndex] != null) {
				return hashTable[tableIndex++].hashedString;
			}
			tableIndex++;
		}
		return null;
	}

	/**
	 * Adds the key to the hash table.
	 * If there is a collision, a new location should be found using quadratic probing.
	 * If the key is already in the hash table, it increments that key's counter.
	 * @param keyToAdd : the key which will be added to the hash table; case-sensitive.
	 */
	public void insert(String keyToAdd) {
		int hashCode = hash(keyToAdd);
		if (hashTable[hashCode] == null) {
			hashTable[hashCode] = new QPNode(keyToAdd);
		}
		assert hashTable[hashCode].hashedString.equals(keyToAdd);
		hashTable[hashCode].count++;
	}

	/**
	 * Returns the number of times a key has been added to the hash table.
	 * @param keyToFind : The key being searched for; case-sensitive.
	 * @return returns the number of times that key has been added.
	 */
	public int findCount(String keyToFind){
		int hashCode = hash(keyToFind);
		if (hashTable[hashCode] != null) {
			return hashTable[hashCode].count;
		}
		return 0;
	}

	/**
	 * Generates a hashcode for the desired key
	 * @param keyToHash : The key being hashed; case-sensitive.
	 * @return returns the hashcode generated
	 */
	private int hash(String keyToHash){
		int hashCode = 0;

		// Generate initial hashCode
		for (int i = 0; i < keyToHash.length(); i++) {
			hashCode = HASH_CONSTANT * hashCode + keyToHash.charAt(i);
		}
		hashCode %= hashTable.length;
		hashCode = Math.abs(hashCode);
		hashCode = probe(hashCode, keyToHash);
		assert hashCode >= 0 && hashCode <= hashTable.length;
		return hashCode;
	}

	/**
	 * Checks for possible hashCode collisions and corrects for them
	 * @param hashCode : Hashcode to examine for collisions
	 * @param keyToHash : Key that hashCode was generated from
	 * @return Returns same hashCode if no collisions were detected; If there were collisions,
	 * 			returns a hashCode modified to correct for possible collisions.
	 */
	private int probe(int hashCode, String keyToHash) {
		boolean collisionDetected = hashTable[hashCode] != null &&
				!hashTable[hashCode].hashedString.equals(keyToHash);

		// Modification of generated hashCode not necessary
		if (!collisionDetected) {
			return hashCode;
		}

		// Correct possible collision using quadratic probing
		int collisionFactor = 1;
		while (collisionDetected) {
			hashCode += Math.pow(collisionFactor, 2); // Adjust hashcode by squared collision factor
			if (hashCode >= hashTable.length) {
				hashCode -= hashTable.length;
			}

			// Check if there is still a collision
			collisionDetected = hashTable[hashCode] != null &&
					!hashTable[hashCode].hashedString.equals(keyToHash);
			collisionFactor++;
		}
		assert hashTable[hashCode] == null || hashTable[hashCode].hashedString.equals(keyToHash);
		return hashCode;
	}

	/**
	 * Simple class that stores a single hashed key value and its count
	 */
	private class QPNode {
		public String hashedString; // Key value hashed to this node
		public int count; // Count of key

		public QPNode(String toHash) {
			hashedString = toHash;
			count = 0;
		}
	}
}
