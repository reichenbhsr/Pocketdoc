angular.module('pocketDocApp').factory("answerToActionSuggestionScoreDistributionFactory", function($http, $resource){
    var factory = {};

    var baseUrl = "/answerToActionSuggestionScoreDistribution/:id";

    return $resource(
        baseUrl, { id: '@distribution_id' },{
            'update': { method:'PUT' }
        }
    );
});