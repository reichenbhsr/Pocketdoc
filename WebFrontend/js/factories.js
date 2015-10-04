'use strict';

var pocketdocFactories = angular.module('pocketdocFactories', []),
    root = "http://pockedoc.herokuapp.com";

/* Factories */
pocketdocFactories.factory("nextQuestionFactory", function ($resource) {
    var baseUrl = root + "/nextQuestion/user/:Id";
    return $resource(
        baseUrl, {Id: '@Id'},{
            'get': { method: 'GET'}
        }
    );
});

pocketdocFactories.factory("runFactory", function($http, $resource){
    var factory = {};

    var baseUrl = root+"/run/user/:Id";

    return $resource(
        baseUrl, { Id: '@Id' },{
            'sendAnswer': { method:'PUT' },
            'resetRun': {method: 'DELETE'},
            'getDiagnosis': {method: 'GET'}
        }
    );
});

pocketdocFactories.factory("User", function() {
    var user = {
        name: "admin",
        id: 3,
        loggedIn: false,
        run: {
            diagnosis: "",
            recommendation: ""
        }
    };
    return user;
});

pocketdocFactories.factory('_', function() {
  return window._; //Underscore must already be loaded on the page
});