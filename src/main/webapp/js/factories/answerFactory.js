angular.module('pocketDocApp').factory("answerFactory", function($http){
    var factory = {};

    var baseUrl = "/answer/:id";

    factory.getAnswers = function() {
        return $http.get(baseUrl);
    }

    return factory;
});