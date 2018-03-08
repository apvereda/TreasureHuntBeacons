String URL = "https://goo.gl/pzuDuu";
if (utils.count("\"Treasure_Find_Game\" : \"treasure\"") == 1) {
	utils.append("Treasure_Find_Game", "\"treasure\": {
						\"value\": \"Treasure 02, Search the way\", 
						\"hint\": \"It seems that the NORTH face is the best way\"}");
	utils.notify("It seems that the NORTH face is the best way");
	String name = utils.get("\"Treasure_Find_Game\" : \"player\"");
	Calendar now = Calendar.getInstance();
	utils.upload(URL, "utils.notify(\"player "+name+" already got this hint at "+now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+"\")");
}
