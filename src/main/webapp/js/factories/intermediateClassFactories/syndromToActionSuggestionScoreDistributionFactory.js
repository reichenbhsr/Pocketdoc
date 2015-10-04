angular.module('pocketDocApp').factory("syndromToActionSuggestionScoreDistributionFactory", function($http,$resource){
    var factory = {};

    var baseUrl = "/syndromToActionSuggestionScoreDistribution/:id";

    return $resource(
        baseUrl, { id: '@distribution_id' },{
            'update': { method:'PUT' }
        }
    );
});