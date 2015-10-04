(function(){

	var app = angular.module('PocketDocComm', []);
	
	app.controller('Communicate', ['$rootScope', '$http', function($rootScope, $http){
		
		$rootScope.login = function()
		{
			alert("Send login");
			$http.post('https://pocketdoc.herokuapp.com/login', {name: 'admin', password: '1234'})
			.success(function(data, status, headers, config){
				console.log(data);
				console.log(status);
				console.log(headers);
				console.log(config);
			})
			.error(function(data, status, headers, config){
				alert("Error in login!");
			});
		};
		
		$rootScope.getNextQuestion = function()
		{
			$http.get('https://pocketdoc.herokuapp.com/nextQuestion/user/3')
			.success(function(data, status, headers, config){
				console.log(data);
				console.log(status);
				console.log(headers);
				console.log(config);
			})
			.error(function(data, status, headers, config){
				alert("Error in login!");
			});
		};
		
		/*
		$rootScope.$on('commLogin', function(e, msg){
			alert("Send login");
			$http.post('https://pocketdoc.herokuapp.com/login', {name: 'admin', password: '1234'})
			.success(function(data, status, headers, config){
				console.log(data);
				console.log(status);
				console.log(headers);
				console.log(config);
			})
			.error(function(data, status, headers, config){
				alert("Error in login!");
			});
			
		});
		
		$rootScope.$on('commGetNextQuestion', function(e, msg){
			
			$http.get('https://pocketdoc.herokuapp.com/nextQuestion/user/3')
			.success(function(data, status, headers, config){
				console.log(data);
				console.log(status);
				console.log(headers);
				console.log(config);
			})
			.error(function(data, status, headers, config){
				alert("Error in login!");
			});
			
		});
		*/
		
	}]);
	
})();