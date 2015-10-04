angular.module('pocketDocApp').factory("questionFactory", function($http, $resource){
    var factory = {};

    var baseUrl = "/question/:id";

    return $resource(
        baseUrl, { id: '@question_id' },{
            'update': { method:'PUT' }
        }
    );
});