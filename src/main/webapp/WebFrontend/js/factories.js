'use strict';

var pocketdocFactories = angular.module('pocketdocFactories', []);

pocketdocFactories.factory("loginFactory", function($http, $resource){

    var baseUrl = "../login";

    return $resource(baseUrl, {},{
        'login': {method: 'PUT'}
    });

});

pocketdocFactories.factory("logoutFactory", function($http, $resource){

    var baseUrl = "../logout";

    return $resource(baseUrl, {},{
        'logout': {method: 'POST'},
        'forgotPassword' : {method: 'PUT'}
    });

});

pocketdocFactories.factory("userFactory", function($http, $resource){
    var baseUrl = "../user";

    return $resource(
        baseUrl, {},{
            'isInUse': {method: 'POST'},
            'createUser': {method: 'POST'},
            'updateUser': {method: 'PUT'},
            'getUser': {method: 'GET'},
            'deleteUser': {method: 'DELETE'}
        }
    );
});

pocketdocFactories.factory("runFactory", function($http, $resource){
    var factory = {};

    var baseUrl = "../run/user/:Id";

    return $resource(
        baseUrl, { Id: '@Id' },{
            'startFollowup': {method: 'POST' },
            'sendAnswer': { method:'PUT' },
            'resetRun': {method: 'DELETE'}
        }
    );
});

pocketdocFactories.factory("nextQuestionFactory", function ($resource) {
    var baseUrl = "../nextQuestion/user/:Id";
    return $resource(
        baseUrl, {},{
            'getNextQuestion': { method: 'POST'}
        }
    );
});

pocketdocFactories.factory("followupFactory", function ($resource) {
    var baseUrl = "../followup/:Id";
    return $resource(
        baseUrl, {},{
            'createFollowup' : { method: 'POST' },
            'getFollowupsForUser': { method: 'GET'},
            'removeFollowup' : { method: 'DELETE' }
        }
    );
});

pocketdocFactories.factory("languageFactory", function ($resource) {
    var baseUrl = "../language";
    return $resource(
        baseUrl, {},{
            'getLanguages': { method: 'GET'}
        }
    );
});

pocketdocFactories.factory('_', function() {
  return window._; //Underscore must already be loaded on the page
});