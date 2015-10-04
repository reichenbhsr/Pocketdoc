angular.module('pocketDocApp').factory("perfectDiagnosisFactory", function($http, $resource){
    var factory = {};

    var baseUrl = "/perfectDiagnosis/:id";

    return $resource(
        baseUrl, { id: '@diagnosis_id' },{
        }
    );
});