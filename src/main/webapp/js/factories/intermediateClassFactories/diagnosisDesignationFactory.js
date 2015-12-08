angular.module('pocketDocApp').factory("diagnosisDesignationFactory", function($resource){
    var factory = {};

    var baseUrl = "/diagnosisDesignation/:id";

    return $resource(
        baseUrl, { id: '@designation_id' },{
            'update': { method:'PUT' }
        }
    );
});