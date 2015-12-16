angular.module('pocketDocApp').factory("syndromFactory", function($http, $resource){
    var factory = {};

    var baseUrl = "/syndrom/:id";

    return $resource(
        baseUrl, { id: '@syndrom_id' },{
            'createSyndromFromAnswer': { method: 'POST' },
            'update': { method:'PUT' }
        }
    );
});