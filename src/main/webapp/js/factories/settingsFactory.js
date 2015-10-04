angular.module('pocketDocApp').factory("settingsFactory", function($http){
    var factory = {};

    var baseUrl = "/setting";

    factory.getSettings = function() {
        return $http.get(baseUrl);
    }

    return factory;
});