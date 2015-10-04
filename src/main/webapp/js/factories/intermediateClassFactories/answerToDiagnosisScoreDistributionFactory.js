angular.module('pocketDocApp').factory("answerToDiagnosisScoreDistributionFactory", function($http, $resource){
    var factory = {};

    var baseUrl = "/answerToDiagnosisScoreDistribution/:id";

    return $resource(
        baseUrl, { id: '@distribution_id' },{
            'update': { method:'PUT' }
        }
    );
});