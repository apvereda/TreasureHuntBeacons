'use strict';

var mongoose = require('mongoose');
var treasureModel = mongoose.model('Treasure');
var hintModel = mongoose.model('Hint')
var util = require('util')


var isGameFinished = false;
var nameOfWinner = ""; 

function process_script(script, parameters) {
  parameters.forEach(function (element) {
    script.replace(element.key, element.value);
  });
}

function findAllTreasures(callback){
  treasureModel.find(function(error, treasures){
    if(error) callback(error,null);
    callback(null,treasures);
  });
}

function findAllHints(callback){
  hintModel.find(function(error, hints){
    if(error) callback(error,null);
    callback(null,hints);
  });
}

function findTreasureById(treasureId, callback) {
  treasureModel.find({name: treasureId}, function (error, treasure) {
    if (error) callback(error, null)
    callback(null, treasure)
  });
}

function findTreasureByIdAndUpdate(treasureId, player, callback) {
  treasureModel.findOneAndUpdate({name: treasureId}, {$addToSet: {players: player}}, function (error, treasure) {
    if (error) callback(error)
    callback(null, treasure);
  });
}

function findDistinctPlayers(treasureId, callback){
  treasureModel.distinct("players.playerid", function(error, listOfPlayers){
    if(error) callback(error);
    callback(null, listOfPlayers);
  });
}


function initialize_data(){

  //insert first data
treasureModel.create({name: "treasure1", hint: "first hint", script: "this is the script"}, function(err){
if(err) return err;
});

 var textOfHint1 = "It is white but it turns yellow when it falls on the floor";
 var textOfHint2 = "I stand up and make your day brighter";
 var textOfHint3 = "I lose my head in the mornings and get it back at nights";

hintModel.update({name: "hint 1"}, {$set: {"text": textOfHint1}}, {upsert: true}, function(err){
  if(err) return err;
  console.log("saved hint 1")
});

hintModel.update({name: "hint 2"}, {$set: {"text": textOfHint2}}, {upsert: true}, function(err){
  if(err) return err;
  console.log("saved hint 2")
});

hintModel.update({name: "hint 3"}, {$set: {"text": textOfHint3}}, {upsert: true}, function(err){
  if(err) return err;
  console.log("saved hint 3")
});
  
}


initialize_data();
function getRandomHint(knownHints, callback){
  hintModel.find(function(error, hints){
    if(error) return error
    if(knownHints == undefined) knownHints = [];

    var remainingHints = hints.filter(function(element){
          return knownHints.indexOf(element.name) == -1
    });

    callback(remainingHints.splice(Math.floor(Math.random()*remainingHints.length), 1)[0])
  });
}


function setFinishedState(userName){
  nameOfWinner = userName;
  isGameFinished = true;
}

function restartGameState(){
  isGameFinished = false;
  nameOfWinner = "";
}

exports.restartGame = function(req, res){
  restartGameState();
  res.send("Game state restarted");
}


exports.finish_Game = function(req, res){
    setFinishedState(req.body.userName);
    res.send("Game finished");
}

exports.get_script = function (req, res) {
  console.log("get script");
    var fs = require('fs');
    fs.readFile('Script/Script.bsh', 'UTF-8', function (err, data) {
      if (err) res.send(err);
      // console.log(data);
      var script = data;
      var beaconName = req.query.beaconName;
      findAllHints(function (err, hints) {
        //Get all the hints and fill the variable from the script
        var hintsArray = "";
        hints.forEach(function (element) {
          hintsArray += "hintsList.put(\"" + element.name + "\", \"" + element.text + "\"); \n";
        });
        //replace tag with data
        script = script.replace("@@hintsListValues@@", hintsArray);
        script = script.replace("@@beaconName@@", "beaconName = \"" + beaconName +"\";");
        console.log("script sent");
        res.send(script);
      });
    });

}



/**
 * Looks the beacon up in the database via beacon id.
 * Check if the player is already registered on that beacon and returns
 * a flag and the list of players that already visited the beacon.
 */

exports.get_game_status = function (req, res) {
  console.log("get game status");
  var beaconId = req.body.beaconId
  var playerId = req.body.playerId


  findTreasureById(beaconId, function (error, treasure) {
    if (error)
      res.send(error)
    var response = {};
    response.script = treasure[0].script
    response.visited = false
    if (treasure[0].players.some(function (element) {
        return element.playerid == playerId
      })) response.visited = true

    response.visitedBy = treasure[0].players
    res.json(response)
  });
};

exports.update_treasure = function (req, res) {
  var beaconId = req.body.beaconId
  var playerToAdd = {};
  var response = {
    gameFinished:isGameFinished,
    winnerName:nameOfWinner
  }

  playerToAdd.playerid = req.body.playerId;
  playerToAdd.timestamp = Date.now();

  findTreasureByIdAndUpdate(beaconId, playerToAdd, function (error, treasure) {
    if (error)
      res.send(error)
    response.message = ("Updated Correctly")
    res.send(response)

  });
  
}

exports.getPlayersFromTreasure = function(req, res){
  var treasure = req.query.treasureName;
  findDistinctPlayers(treasure, function(error, listOfPlayers){
    if(error)
      res.send(error);
    res.send(listOfPlayers);
  });

}

exports.initialize_data = function(req, res){

    //insert first data
  initialize_data();
  res.send("data added");

  }

exports.checkConnection = function (req, res) {

  res.send("Connection successful");

}
