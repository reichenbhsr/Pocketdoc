'use strict';

describe('service tests', function() {

	// load modules
	beforeEach(module('pocketdocApp'));

	// Test service availability
	it('check the existence of User factory', inject(function(User) {
		expect(User).toBeDefined();
	}));

	it('check if user is logged out by default', inject(function(User) {
		expect(User.loggedIn).toBe(false);
	}));


	it('check the existence of DataService factory', inject(function(DataService) {
		expect(DataService).toBeDefined();
	}));

	it('check the existence of histories in the data', inject(function(DataService) {
		expect(DataService.histories).toBeDefined();
	}));

	it('should contain all the users of each history', inject(function(DataService) {
		for (var i = 0; i < DataService.histories.length; i++) { 
			expect(DataService.users[DataService.histories[i].user_id]).toBeDefined();
		}
	}));

});