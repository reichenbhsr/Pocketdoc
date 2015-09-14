'use strict';

describe('HeaderController', function() {
	var scope,
		$location,
		store = {};

	beforeEach( function(){
		module('pocketdocApp');

		inject(function ( $rootScope, $controller, _$location_) {
			$location = _$location_;
			scope = $rootScope.$new();
		});

		spyOn(localStorage, 'getItem').andCallFake(function(key) {
			return store[key];
		});
		
		Object.defineProperty(sessionStorage, "setItem", { writable: true });
		
		spyOn(localStorage, 'setItem').andCallFake(function(key, value) {
			store[key] = value;
		});

	});

	afterEach(function () {
		store = {};
	});

	it('should provide german as the default language', inject(function($controller) {
		var ctrl = $controller('HeaderController', {
			$scope:scope
		});

		expect(scope.lang).toBe( "de" );
	}));

	it('should be logged in after login-broadcast was triggered', inject(function($controller) {
		var ctrl = $controller('HeaderController', {
			$scope:scope
		});

		scope.$emit("login");

		expect(scope.loggedIn).toBe( true );
	}));

	it('should be logged out after logout-broadcast was triggered', inject(function($controller) {
		var ctrl = $controller('HeaderController', {
			$scope:scope
		});

		scope.$emit("logout");

		expect(scope.loggedIn).toBe( false );
	}));

	it('should change the language upon broadcast', inject(function($controller) {
		var ctrl = $controller('HeaderController', {
			$scope:scope
		});

		scope.$emit("languageChange", 'en');

		expect( scope.lang ).toBe( 'en' );
	}));

	it('should try to change window location when going to main', inject(function($controller) {
		var ctrl = $controller('HeaderController', {
			$scope:scope
		});

		spyOn(window.history, 'back');
		scope.goToMain();

		expect(window.history.back).toHaveBeenCalled();
	}));

});

describe('QuestionController', function() {
	var scope,
		$location,
		store = {};

	beforeEach( function(){
		module('pocketdocApp');

		inject(function ( $rootScope, $controller, _$location_) {
			$location = _$location_;
			scope = $rootScope.$new();
		});

		spyOn(localStorage, 'getItem').andCallFake(function(key) {
			return store[key];
		});
		
		Object.defineProperty(sessionStorage, "setItem", { writable: true });
		
		spyOn(localStorage, 'setItem').andCallFake(function(key, value) {
			store[key] = value;
		});

	});

	afterEach(function () {
		store = {};
	});


	it('should be for the current User by default', inject(function($controller) {
		var ctrl = $controller('questionController', {
			$scope:scope
		});

		expect(scope.forCurrentUser).toBe( true );
	}));

	it('should not be logged in', inject(function($controller) {
		var ctrl = $controller('questionController', {
			$scope:scope
		});

		expect(scope.isLoggedIn).toBe( false );
	}));


	it('should not be valid  in regards to the user by default', inject(function($controller) {
		var ctrl = $controller('questionController', {
			$scope:scope
		});

		scope.checkValidity();

		expect(scope.dataInvalid).toBe( true );
	}));

	it('should not accept false values for a name', inject(function($controller) {
		var ctrl = $controller('questionController', {
			$scope:scope
		});

		scope.user.name = "";
		scope.checkValidity();

		expect(scope.dataInvalid).toBe( true );
	}));

	it('should accept correct values for a user', inject(function($controller) {
		var ctrl = $controller('questionController', {
			$scope:scope
		});

		scope.user.name = "Test";
		scope.user.gender = 0;
		scope.user.age_category = 0;
		scope.checkValidity();

		expect(scope.dataInvalid).toBe( false );
	}));

	it('should set the gender of the user correctly', inject(function($controller) {
		var ctrl = $controller('questionController', {
			$scope:scope
		});

		scope.setGender( 1 );

		expect(scope.user.gender).toBe( 1 );
	}));

	it('should change the language of an user', inject(function($controller) {
		var ctrl = $controller('questionController', {
			$scope:scope
		});

		expect(scope.user.lang).toBe( 'de' );
		scope.changeLanguage("en");
		expect(scope.user.lang).toBe( 'en' );
	}));
});

describe('DiagnosisController', function() {
	var scope,
		$location;

	beforeEach( function(){
		module('pocketdocApp');

		inject(function ( $rootScope, $controller, _$location_) {
			$location = _$location_;
			scope = $rootScope.$new();
		});
	});

	it('should not be logged in by default', inject(function($controller) {
		var ctrl = $controller('diagnosisController', {
			$scope:scope,
			DiagnosisData:{
				diagnosis: {}
			}
		});

		expect(scope.isLoggedIn).toBe( false );
	}));

	it('should show loggin dialog if not logged in and accepting followUP', inject(function($controller) {
		var ctrl = $controller('diagnosisController', {
			$scope:scope,
			DiagnosisData:{
				diagnosis: {}
			}
		});
		
		spyOn(scope, 'showLoginDialog');
		
		scope.acceptFollowUp();

		expect(scope.showLoginDialog).toHaveBeenCalled();
	}));

	it('should show error in console if not logged in and adding followup', inject(function($controller) {
		var ctrl = $controller('diagnosisController', {
			$scope:scope,
			DiagnosisData:{
				diagnosis: {}
			}
		});
		
		spyOn(console, 'log');
		
		scope.addFollowUp();

		expect(console.log).toHaveBeenCalledWith("Error: not logged in");
	}));
});