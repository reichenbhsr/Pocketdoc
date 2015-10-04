var app = angular.module('pocketDocApp',['ngTable', 'ngRoute', 'ngResource', 'ngCookies']);

app.config(function ($routeProvider, $httpProvider) {

    $httpProvider.defaults.withCredentials = true;

    $routeProvider
        .when('/',
        {
            controller: 'diagnosisController',
            templateUrl: 'partials/diagnoses.html'
        })
        .when('/diagnoses',
        {
            controller: 'diagnosisController',
            templateUrl: 'partials/diagnoses.html'
        })
        .when('/questions',
        {
            controller: 'questionController',
            templateUrl: 'partials/questions.html'
        })
        .when('/action_suggestions',
        {
            controller: 'actionSuggestionController',
            templateUrl: 'partials/action_suggestions.html'
        })
        .when('/syndroms',
        {
            controller: 'syndromController',
            templateUrl: 'partials/syndroms.html'
        })
        .when('/run',
        {
            templateUrl: 'partials/run.html'
        })
        .when('/settings',
        {
            controller: 'settingController',
            templateUrl: 'partials/settings.html'
        })
        .otherwise({ redirectTo: '/' });
});