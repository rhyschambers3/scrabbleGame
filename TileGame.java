/*--------------------------------------------------------------------------
GWU CSCI 1112 Spring 2022
author: <Rhys Chambers>

This class encapsulates the game logic needed to support a Scrabble like
tile based spelling game
--------------------------------------------------------------------------*/
class TileGame {
    //                            A,B,C,D, E,F,G,H,I,J,K,L,M,N,O,P, Q,R,S,T,U,V,W,X,Y, Z
    /// the points for a given alpha
    static final int[] points  = {1,3,3,2, 1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
    /// the maximum number of tiles (uses) for a given alpha in a single word
    static final int[] tilebag = {9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2, 1,6,4,6,4,2,2,1,2, 1};
    public static final int MAXTILES = 98;  // ignores 2 blanks


    /// Looks up the points associated with a given character and returns
    /// that point value
    /// @param ch the character to look up a point value for
    /// @return a positive point value representing the point value for 
    ///         that character if ch is an uppercase alpha; otherwise, a 
    ///         zero value
    public static int getTilePoints( char ch ) {
        // Convert ch into its ordinal alphabetical position 
        int ord = (int)ch - (int)'A';

        // sanity check - validate ch is in the range of uppercase alphas
        if(ord < 0) { return 0; }
        if(ord >= 26) { return 0; }

        // ch must be an uppercase alpha, so return the value in the ordinal
        // position of the points table
        return points[ord];
    }

    /// Computes the total score for a word and returns that point total
    /// @param word a character array that contains the word that we need
    ///        the point total for 
    /// @return the total score for the word passed in.  If the function
    ///         fails for any reason, it returns zero
    public static int getWordScore( char[] word ) {
        // sanity check - validate that the word is valid
        if( word == null ) { return 0; }

        // the running total for the summation
        int total = 0;

        // iterate over every character in the word and add its points 
        // to the total  
        for( int i = 0; i < word.length; i++ ) {
            total += getTilePoints( word[i] );
        }

        // return the total
        return total;
    }

    /// Make a deep copy of the model tilebag array.  The model must not be 
    /// changed, so need to copy the array and use the copy as a 'scratchpad'
    /// for computing tile usage
    /// @return a copy of the tilebag array/an array of the number of tiles
    ///         available for each alpha ordinal position.  
    public static int[] copyTileBag() {
	    int[] bagCopy = new int [tilebag.length];
	    for (int i= 0; i < tilebag.length; i++)
            {
		   bagCopy[i] = tilebag[i];
	    } 
	    
        return bagCopy;
	
    }

    /// Draw a random subset of tiles from the tilebag
    /// @param count the number of tiles to draw from the tilebag
    /// @return an array of characters drawn from the tilebag 
    ///         according to the limits of the number of tiles
    ///         for a given character; or null if the count is 
    ///         invalid or the operation fails for any reason
    public static char[] drawHand( int count ) {
        // sanity check - validate that the count does not exceed
        // the number of available tiles
        if( count > MAXTILES ) { return null; }

        // deep copy the tilebag.  Why?  because if we don't
        // any changes to the data would destroy the model and the
        // game would no longer have consistent rules from round to
        // round.
        int[] tb = copyTileBag();
        // create a hand of the specified size
        char[] hand = new char[count];

        // draw a tile to fill the next open hand position
        for( int i = 0; i < count; i++ ) {
            // but the tile must be valid 
            // can't draw more tiles than are available in tilebag
            boolean valid = false;
            while( !valid ) {
                // generate a random number from a uniform distribution
                int pos = UniformRandom.uniform(0,25);
                // if the tile generated is available
                if( tb[pos] > 0 ) {
                    valid = true;   // it's valid
                    tb[pos]--;      // decrement the available count
                    hand[i] = (char)((int)'A' + pos);  // put in hand
                }
            }
        }
        return hand;
    }
   
    /// Determines where the characters in hand can be used to 
    /// spell the word.  Each character in hand can only be used once.
    /// If we are trying to spell a word with two A's in it, there
    /// must be at least two A's in hand.
    /// @param hand the jumble of characters available to spell with
    /// @param word the word that we are testing trying to spell
    /// @return true if the characters in hand can be used to spell
    ///         the word; otherwise, false.
    public static boolean canSpell( char[] hand, char[] word ) {
        // TODO : Add your code here
	//loop through the hand to check if each letter is in word
	//create an array to make sure the letters and numbers of letters are the same in word and hand
	
        int start = (int) 'A';
	int [] checkerHand = new int[26];
	int [] checkerWord = new int[26];
	
	if (hand == null){
	   return false;
	}
	if (word == null){
	   return false;
	}
	//loop and populate alphabet counter for the characters in hand
	for (int i = 0; i < hand.length; i++){
		char c = hand[i];
		int posAlpha = (int) c - start;
		checkerHand[posAlpha]++;
             }

	// loop and populate alphabet counter for the characters in word
	for (int i = 0; i < word.length; i++){
	     char w = word[i];
	     int posAlphaW = (int) w - start;
		    checkerHand[posAlphaW]--;
	     
	 }
	//check that the arrays are the same
	for (int i = 0; i < checkerHand.length; i++){
	   if (checkerHand[i ]< 0){
		return false;
       	   }		 
    }
    return true;
    }

    /// Returns the highest scoring word according to getWordScore from 
    /// dictionary that can be spelled by the characters in hand according
    /// to canSpell
    /// @param hand the jumble of characters available to spell with
    /// @param dictionary an array of words that we test against
    /// @return the highest scoring word that can be spelled given the
    ///         input hand according to getWordScore; otherwise, null
    ///         if no word in dictionary can be spelled with hand
    public static String getBestWord( char[] hand, String[] dictionary ) {
       
	    // go through each word in the dictionary. find the character value 
	    // at each letter in the specified word. check if we can spell with canSpell 
	    // and if we can, return the score of that word
	 if (hand == null || dictionary == null){
	    return null;
	 }
	int best = 0;
	String bestWord = null;
	//check if you the word can be spelled with the words in the dictionary
	for (int i = 0; i < dictionary.length; i++){
		//check if the hand is at least the same size as dictionary word

	       	  char [] c = dictionary[i].toCharArray();
		  if (hand.length >= c.length){

		    if (canSpell(hand, c) == true){
		   //if the wordscore of the given word is better than the current score, replace 
		  	 //the word and score with the new best	
		  	 if (getWordScore(c) >= best){
		       best = getWordScore(c);
		       bestWord = dictionary[i];
		   }
	        }
		//if the word can't be spelled by the hand return null
		   	
	 	}			
    }
    return bestWord;

}
}
