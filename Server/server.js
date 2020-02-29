var express = require('express'),
  app = express(),
  port = process.env.PORT || 3000,
  mongoose = require('mongoose'),
  Task = require('./api/models/treasureModel'), //created model loading here
  bodyParser = require('body-parser');
  
// mongoose instance connection url connection
mongoose.Promise = global.Promise;
mongoose.connect(process.env.MONGOLAB_URI || 'mongodb://localhost/TreasureGamedb') 


app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());


var routes = require('./api/routes/hintGameRoutes'); //importing route
routes(app); //register the route


app.listen(port, '0.0.0.0');


console.log('todo list RESTful API server started on: ' + port);
app.use(function(req, res) {
    res.status(404).send({url: req.originalUrl + ' not found'})
  });