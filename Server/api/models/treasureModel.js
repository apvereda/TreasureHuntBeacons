'use strict';
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

/*
var PlayersSchema = new Schema({
  players:{
    type:[{
      playerid:String,
      timestamp:{
        type:Date,
        default: Date.now
      }
    }]
  }
})*/

var TreasureSchema = new mongoose.Schema({
  name: {
    type: String,
    required: ''
  },
  players: {
    type: [{
      playerid: String,
      timestamp: {
        type: Date,
        default: Date.now
      }
    }],
  },
  script: String

});
var HintSchema = new mongoose.Schema({
  name: {
    type: String,
    required: ''
  },
  text: {
    type: String
  }

})


module.exports = mongoose.model('Treasure', TreasureSchema);
module.exports = mongoose.model('Hint', HintSchema)