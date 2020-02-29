'use strict';
module.exports = function (app) {
  var List = require('../controllers/hintGameController');

  // todoList Routes
  app.route('/status')
    .post(List.get_game_status);

  app.route('/update')
    .post(List.update_treasure);

  app.route('/init')
    .post(List.initialize_data);
    
  app.route('/test')
    .get(List.checkConnection);  
  
  app.route('/script')
    .get(List.get_script);

  app.route('/finishGame')
    .post(List.finish_Game);
    
  app.route('/getPlayers')
    .post(List.getPlayersFromTreasure);

  app.route('/restartGame')
    .get(List.restartGame);


};