angular.module('pocketDocApp').factory("actionSuggestionFactory", function($http, $resource){

    var baseUrl = "/actionSuggestion/:id";

    return $resource(
        baseUrl, { id: '@action_suggestion_id' },{
            'update': { method:'PUT' }
        }
    );
});