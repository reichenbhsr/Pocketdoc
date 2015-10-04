angular.module('pocketDocApp').factory("testRunFactory", function($http, $resource){
    var factory = {};

    var baseUrl = "/testrun/user/:Id";

    return $resource(
        baseUrl,{} ,
        {Id: '@Id'},
        {
            'query': { method:'GET'}
        }
    );
});