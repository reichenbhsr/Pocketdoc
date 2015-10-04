angular.module('pocketDocApp').factory("diagnosisFactory", function($http, $resource){
    var factory = {};

    var baseUrl = "/diagnosis/:id";

    return $resource(
        baseUrl, { id: '@diagnosis_id' },{
            'update': { method:'PUT' }
        }
    );
});