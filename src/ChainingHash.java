/**
 * ChainingHash provides the client with enhanced hashing capabilities for keys of type String.
 * Primary purpose of ChainingHash is to count and store the occurrences of unique words in works
 * of literature.
 *
 * @author Evan Gordon
 * Created for University of Washington CSE 373. Last Modified May 13th, 2016.
 */

public class ChainingHash {
		public static final int DEFAULT_TABLE_SIZE = 12553; // Prime default hashTable size
        public static final int HASH_CONSTANT = 229; // Prime multiplicative constant for hash function
		private ChainingNode[] hashTable;
        private int tableIndex; // Tracks current hashTable position for getNextKey()
        private int listIndex; // Tracks current key for getNextKey()
        private int wordCount; // Tracks total word count of all keys in hashTable
        private int keyCount; // Tracks total number of unique keys in hashTable
        private int loadCount; // Tracks total number of non-null indices in hashTable for load factor

        /**
         * Instantiates a ChainingHash with the default hash table size.
         */
		public ChainingHash(){
            this(DEFAULT_TABLE_SIZE);
		}

        /**
        * Instantiates a ChainingHash with the desired hash table size.
        * @param startSize : Desired size of hash table
        */
		public ChainingHash(int startSize){
			hashTable = new ChainingNode[startSize];
            wordCount = 0;
            tableIndex = 0;
            listIndex = 0;
            loadCount = 0;
		}

		/**
		 * This function allows rudimentary iteration through the ChainingHash.
		 * The ordering is not important so long as all added elements are returned only once.
		 * It should return null once it has gone through all elements
		 * @return Returns the next element of the hash table. Returns null if it is at its end.
		 */
		public String getNextKey(){
            String nextKey = null;
            while (tableIndex < hashTable.length && nextKey == null) {
                if (hashTable[tableIndex] != null) { // Found an entry; examine its list
                    ChainingNode current = hashTable[tableIndex];
                    for (int i = 0; i < listIndex; i++) { // Traverse to current list-position
                        current = current.next;
                    }
                    listIndex++;
                    if (current.next == null) { // No keys remain for this index of hashTable
                        listIndex = 0; // Ensure we look at the beginning of next node's list
                        tableIndex++; // Move to next index of hashTable
                    }
                    nextKey = current.hashedString;
                } else { // Continue searching hashTable for nodes that are not null
                    tableIndex++;
                }
            }
			return nextKey;
		}

        /**
		 * Adds the key to the hash table.
		 * If there is a collision, it should be dealt with by chaining the keys together.
		 * If the key is already in the hash table, it increments that key's counter.
		 * @param keyToAdd : the key which will be added to the hash table
		 */
		public void insert(String keyToAdd){
            wordCount++;
            int hashCode = hash(keyToAdd);
            if (hashTable[hashCode] == null) { // We must utilize another index of the hashTable; increment loadCount
                loadCount++;
            }
            ChainingNode current = hashTable[hashCode];

            // Check each node in list for the desired key
            while (current != null && !current.hashedString.equals(keyToAdd)) {
                current = current.next;
            }
            if (current == null) { // Key was not in list; must create new entry for this key.
                current = new ChainingNode(keyToAdd); // keyToAdd is not in list; create new node
                current.next = hashTable[hashCode];
                hashTable[hashCode] = current;
                keyCount++;
            }
            assert current.hashedString.equals(keyToAdd);
            current.count++;
		}

        /**
		 * Returns the number of times a key has been added to the hash table.
		 * @param keyToFind : The key being searched for
		 * @return returns the number of times that key has been added.
		 */
		public int findCount(String keyToFind){
            int hashCode = hash(keyToFind);
            ChainingNode current = hashTable[hashCode];
            while (current != null) {
                if (current.hashedString.equals(keyToFind)) {
                    return current.count;
                }
                current = current.next;
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
            for (int i = 0; i < keyToHash.length(); i++) {
                hashCode = HASH_CONSTANT * hashCode + keyToHash.charAt(i);
            }
            hashCode %= hashTable.length;
            hashCode = Math.abs(hashCode);
            assert hashCode >= 0 && hashCode <= hashTable.length;
            return hashCode;
		}

        /**
         * Returns the total count of all words in the hash table
         * @return : Returns the total number of words in the hash table
         */
        public int getWordCount() {
            return wordCount;
        }

        /**
         * Returns the number of keys in the hash table
         * @return : Returns the total number of keys in the hash table
         */
        public int getKeyCount() {
            return keyCount;
        }

        /**
         * Computes the load factor (lambda) of the hashTable
         * @return : Returns the computed load factor
         */
        public double loadFactor() {
            return (double) loadCount / hashTable.length;
        }

        /**
         * Simple class that stores a single hashed key value and its count,
         * as well as a reference to the next node with the same hashCode, i.e.
         * the next node that stores a key with the same hashCode.
         */
		private class ChainingNode {
			public ChainingNode next;
			public String hashedString; // Key value hashed to this node
			public int count; // Count of key value

            public ChainingNode(String toHash) {
                hashedString = toHash;
                count = 0;
            }
		}
}
