angular.module('pocketDocApp').factory("questionDescriptionFactory", function($resource){
    var factory = {};

    var baseUrl = "/questionDescription/:id";

    return $resource(
        baseUrl, { id: '@description_id' },{
            'update': { method:'PUT' }
        }
    );
});