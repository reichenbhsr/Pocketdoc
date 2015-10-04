'use strict';

/* App Module */

var pocketdocApp = angular.module('pocketdocApp', [
  "ngMaterial",
  "pocketdocControllers",
  "pocketdocFactories",
  "pocketdocBackend",
  "ngRoute",
  "ngCookies",
  "ngResource",
  "ngMessages",
  "pascalprecht.translate"
]).config(['$routeProvider', '$locationProvider', '$httpProvider', '$mdThemingProvider', '$translateProvider',
	function($routeProvider, $locationProvider, $httpProvider, $mdThemingProvider, $translateProvider) {

	    $httpProvider.defaults.useXDomain = true;
	    $httpProvider.defaults.withCredentials = true;
		delete $httpProvider.defaults.headers.common['X-Requested-With'];
		
		$routeProvider
		.when('/', {
			templateUrl: 'partials/Splash.html',
			controller: 'mainController'
		})
		.when('/run', {
			templateUrl: 'partials/Questions.html',
			controller: 'questionController'
		})
		.when('/diagnosis', {
			templateUrl: 'partials/Diagnosis.html',
			controller: 'diagnosisController'
		})
		.when('/registration', {
			templateUrl: 'partials/Registration.html',
			controller: 'registrationController'
		})
		.when('/profile', {
			templateUrl: 'partials/Registration.html',
			controller: 'registrationController'
		})
		.when('/about', {
			templateUrl: 'partials/About.html',
			controller: 'mainController'
		})
		.when('/terms', {
			templateUrl: 'partials/Terms.html',
			controller: 'mainController'
		})
		.otherwise({
			redirectTo: '/'
		});
		
		$mdThemingProvider.theme('default').accentPalette('green');

		$translateProvider.useSanitizeValueStrategy('escaped');

		$translateProvider.useStaticFilesLoader({
			prefix: 'i18n/translations-',
			suffix: '.json'
		});
		$translateProvider.preferredLanguage('de');
	}
]);

pocketdocApp.filter('reverse', function() {
  return function(items) {
    return items.slice().reverse();
  };
});
