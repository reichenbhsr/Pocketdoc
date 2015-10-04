angular.module('pocketDocApp').factory("runFactory", function($http, $resource){
    var factory = {};

    var baseUrl = "/run/user/:Id";

    return $resource(
        baseUrl,{} ,
        {Id: '@Id'},
        {
            'query': { method:'GET'}
        }
    );
});