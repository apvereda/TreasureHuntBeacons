void treasureHunt(String status, List<String> clues) {
	if (dac.read("clues") == null) {
		// The player has just started the hunt; 
		// entities are written to the profile
		int i = 0; 
		for (String clue : clues) {
			dac.write("clues/" + i++, clue); 
		}
		dac.write("treasures", 0);
	}
	String c = dac.read("clues"); 
	dac.remove("clues/", c);
	if (status == "playing") {
	    // Checks no. of treasures already found
		int treasures = dac.read("treasures");  
		if (treasures != 4) {
			dac.write("treasures", treasures++);
			dac.showToast("The new clue is " + c); 
		}else { // The player has won the game  	
			dac.showToast("Congratulations. You Win!");
			dac.write("treasures", 5);
			String me = dac.read("Personal/name");
			String now = dac.read("System/now");
			dac.remoteWrite("winner/name", me);
			dac.remoteWrite("winner/time", now);
			dac.remoteWrite("gameover");
		}
	// Somebody else has won the game
	}else { dac.showToast("You lose!"); } 
}
