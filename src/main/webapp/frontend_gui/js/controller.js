(function(){
	
	var app = angular.module('PocketDoc', ['ngMaterial', "ngCookies", "ngResource"]).config(function($mdThemingProvider) {
	  $mdThemingProvider.theme('default')
		.primaryPalette('cyan')
		.accentPalette('green')
		.warnPalette('red');
	});

	app.factory('doneQuestionsSrv', function(){
		return {done : []};
	});
	
	app.controller('TemplateCtrl', ['$rootScope', '$scope', '$resource', '$cookies', '$timeout', function($rootScope, $scope, $resource, $cookies, $timeout){
		
		$rootScope.$on('changeMainContent', function(e, msg){
			$scope.mainContent = msg;
		});
		$rootScope.$on('changeBottomContent', function(e, msg){
			$scope.bottomContent = msg;
		});
		
		$rootScope.$on('commLogin', function(e, msg){
			
			var login = $resource('/login'); //https://pocketdoc.herokuapp.com/login
			login.save({name:"admin", password:"1234", isLogged:false}, function(data){
					console.log(data);
					$timeout(function(){
						console.log($cookies.session)
					  });  
					$rootScope.$emit('commGetNextQuestion', '');
			});
			

		});
		
		$rootScope.$on('commGetNextQuestion', function(e, msg){
			var login = $resource('/user/1'); //https://pocketdoc.herokuapp.com/nextQuestion/user/3
			console.log(login.get(function(data){console.log(data);}));

			
		});
		
		//PocketDocComm.login();
		$rootScope.$emit('commLogin', '');
		
		$rootScope.$emit('changeMainContent', 'partials/Splash.html');
		$rootScope.$emit('changeBottomContent', 'partials/Splash_next.html');

	}]);

	app.controller('SplashCtrl', ['$rootScope', '$scope', function($rootScope, $scope){
		
		$scope.clickNext = function(e){
			$rootScope.$emit('changeMainContent', 'partials/Questions.html');
			$rootScope.$emit('changeBottomContent', 'partials/Answer.html');
		};
		
	}]);

	app.controller('ListCtrl', ['$scope', 'doneQuestionsSrv', function($scope, doneQuestionsSrv){
		$scope.$watch(
			'doneQuestionsSrv.done',
			function(){
				$scope.done = doneQuestionsSrv.done;
			}
		);
	}]);

	app.controller('QuestionCtrl', ['$rootScope', '$scope', '$mdDialog', 'doneQuestionsSrv', function($rootScope, $scope, $mdDialog, doneQuestionsSrv){

        $scope.askedQuestions = 0;

		$scope.nextQuestions = [
			{ question: 'Haben sie Schmerzen?' },
			{ question: 'Haben sie starke Schmerzen?' },
			{ question: 'Haben sie schwache Schmerzen?' },
			{ question: 'Haben sie Husten?' }
		];
		
		$scope.getNextQuestion = function(){
			if ($scope.nextQuestions.length > 0)
				return $scope.nextQuestions.splice(0,1)[0].question;
			else
				return undefined;
		};
		
		$scope.addDoneQuestion = function(questionObject){
			doneQuestionsSrv.done.push(
				questionObject
			);

            $scope.askedQuestions = $scope.askedQuestions + 1;
			$scope.currentQuestion = $scope.getNextQuestion();
			
			if (typeof $scope.currentQuestion == "undefined")
			{
				$rootScope.$emit('changeMainContent', 'partials/Diagnosis.html');
				$rootScope.$emit('changeBottomContent', '');
			}
            else
            {
                if ($scope.askedQuestions % 2 === 0 )
                    $scope.askDiagnosisOk();
            }
		};

		$scope.currentQuestion = $scope.getNextQuestion();
		
		$scope.clickYes = function(e){
			$scope.addDoneQuestion({question: $scope.currentQuestion, answer: 'yes'});
		};
		
		$scope.clickNo = function(e){
			$scope.addDoneQuestion({question: $scope.currentQuestion, answer: 'no'});
		};

        $scope.askDiagnosisOk = function(e){
            var confirm = $mdDialog.confirm()
                .title('Diagnose Gefunden')
                .content('Unsere Diagnose: Erkältung.')
                .ariaLabel('Lucky day')
                .ok('Diagnose Ok')
                .cancel('Weitere Fragen beantworten')
                .targetEvent(e);

            $mdDialog.show(confirm).then(function() {
                // Ok
                $rootScope.$broadcast('changeMainContent', 'partials/Diagnosis.html');
                $rootScope.$broadcast('changeBottomContent', '');
            }, function() {
                // Weiter fragen
            });
        };
	}]);

	app.controller('DiagnosisCtrl', ['$scope', function($scope){
		
		$scope.diagnosis = 'Erkältung';
		$scope.suggestion = 'Bleiben sie zu Hause.';
		
	}]);
	
	
	
})();
