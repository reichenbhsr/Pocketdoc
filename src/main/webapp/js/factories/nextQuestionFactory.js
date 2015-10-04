angular.module('pocketDocApp').factory("nextQuestionFactory", function ($resource) {
    var factory = {};

    var baseUrl = "/nextQuestion/user/:Id";
    return $resource(
        baseUrl,
        {Id: '@Id'},
        {
            get: {
                method: 'GET'
            }
        });
});