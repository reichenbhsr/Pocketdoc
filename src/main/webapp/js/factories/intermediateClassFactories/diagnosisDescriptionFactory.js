angular.module('pocketDocApp').factory("diagnosisDescriptionFactory", function($resource){
    var factory = {};

    var baseUrl = "/diagnosisDescription/:id";

    return $resource(
        baseUrl, { id: '@description_id' },{
            'update': { method:'PUT' }
        }
    );
});