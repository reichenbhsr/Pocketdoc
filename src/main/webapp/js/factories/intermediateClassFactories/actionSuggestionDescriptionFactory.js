angular.module('pocketDocApp').factory("actionSuggestionDescriptionFactory", function($resource){
    var factory = {};

    var baseUrl = "/actionSuggestionDescription/:id";

    return $resource(
        baseUrl, { id: '@description_id' },{
            'update': { method:'PUT' }
        }
    );
});