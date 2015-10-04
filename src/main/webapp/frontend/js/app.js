'use strict';

/* App Module */

var pocketdocApp = angular.module('pocketdocApp', [
  "ngMaterial",
  "pocketdocControllers",
  "pocketdocFactories",
  "ngRoute",
  "ngCookies",
  "ngResource"
])
.config(['$routeProvider', '$locationProvider', '$httpProvider',
	function($routeProvider, $locationProvider, $httpProvider) {

	    $httpProvider.defaults.useXDomain = true;
	    $httpProvider.defaults.withCredentials = true;
		delete $httpProvider.defaults.headers.common['X-Requested-With'];


		$routeProvider
		.when('/run', {
			templateUrl: 'partials/run.html',
			controller: 'questionController'
		})
		.when('/', {
			templateUrl: 'partials/main.html',
			controller: 'mainController'
		})
		.otherwise({
			redirectTo: '/'
		});
	}
]);