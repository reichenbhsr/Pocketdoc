var express = require('express');
var app = express();

//Define root folder as the cwd
app.use('/', express.static(__dirname));

var port = Number(process.env.PORT || 5000);
app.listen(port, function() { 
    console.log('Your files will be served through this web server')
});