(function(){

	var pocketdocControllers = angular.module('pocketdocControllers', []);

	pocketdocControllers.controller('questionController',
		[ "$http", "$scope", "$location", '$cookies','$cookieStore', '$anchorScroll', "User", "nextQuestionFactory", "runFactory", '$mdDialog',
    function( $http, $scope, $location, $cookies, $cookieStore, $anchorScroll, User, nextQuestionFactory, runFactory, $mdDialog ) {

		$scope.finished = false;

		$scope.no_suggestion = "Noch keine Handlungsempfehlung möglich.";
		$scope.no_diagnosis = "Noch keine Diagnosis möglich.";
		$scope.questions = [];
		$scope.currentQuestion = {};
		$scope.loading = false;
        $scope.diagnosis = null;
        $scope.recommendation = null;

        $scope.user = User;

        $scope.login = function( callback ) {
            if ( !$scope.user.loggedIn ) {
                $http({
                    'method': 'POST',
                    'url': 'http://pocketdoc.herokuapp.com/login',
                    'withCredentials': false,
                    headers: { 'Content-Type': 'text/plain' },
                    data: { name: "admin", password: "1234" }
                }).success(function(data, status, headers, config) {
                    console.log( "SUCCESS on Login:", data, status, headers, config );
                    $scope.user.id = data.id;
                    $scope.user.loggedIn = true;

                    if ( callback && angular.isFunction( callback ) ) {
                        callback();
                    } else {
                        $scope.getQuestion();
                    }
                }).error(function(data, status, headers, config) {
                    console.log( "ERROR on Login:", data, status, headers, config );
                });
            }
        };

        $scope.start = function() {
            $scope.login( $scope.restartRun );
        }

        $scope.getDiagnosis = function() {
            if ($scope.user.loggedIn) {

                runFactory.getDiagnosis({Id: $scope.user.id}, function (result) {
                    debugger;

                });
            } else {
                console.log("NOT LOGGED IN");
            }
        };

		$scope.getQuestion = function() {
			if ($scope.user.loggedIn) {
                $scope.loading = true;
                $("#contentContainer").animate({ scrollTop: $(document).height() }, "slow");

				console.log("Neue Frage laden...");

                return nextQuestionFactory.get({Id: $scope.user.id}, function (newQuestion) {
                    console.log("Neue Frage erhalten...");
                    console.log(newQuestion);
                    $scope.loading = false;

                    if (angular.isUndefined(newQuestion.descriptions)) {
                        $location.url("/diagnosis");
                        // well... no description found...
                        return;
                    }

                    // before showing the next question, check if there's a diagnosis!
                    runFactory.getDiagnosis({Id: $scope.user.id}, function (diagnosisResult) {
                        debugger;

                        $scope.showQuestion( newQuestion );

                        if( !angular.isUndefined( diagnosisResult.diagnosis ) ) {
                            //show diagnosis now. If not OK with the user, continue with the questions

                            diagnosisResult.diagnosis.descriptions.forEach(function (description) {
                                if (angular.equals(description.language_name, "Deutsch")) {
                                    diagnosisResult.german_description = description.description;
                                }
                            });

                            $scope.askDiagnosisOk( diagnosisResult );
                            //var accepted = confirm( "Diagnosis:" + diagnosisResult.german_description );
                        }
                    });
                    /*
                    if (angular.isUndefined(result.descriptions)) {
                        $location.url("/diagnosis");
                        // well... no description found...
                    } else {

                        // only get German for now
                        result.descriptions.forEach(function (description) {
                            if (angular.equals(description.language_name, "Deutsch")) {
                                result.german_description = description.description;
                            }
                        });

                        var newQuestion = {
                            'id': result.question_id,
                            'status': 'unanswered',
                            'subject': result.name,
                            'text': result.german_description,    // TODO: I18N
                            'answer_yes': result.answer_yes,
                            'answer_no': result.answer_no
                        };

                        console.log(newQuestion);
                        $scope.currentQuestion = newQuestion;
                        $scope.loading = false;

                        $("#contentContainer").animate({ scrollTop: $(document).height() }, "slow");
                    }
                    */

                }, function( result ){
                    // CONNECTION ERROR
                    console.log( "CONNECTION FAILED - BACKEND DEAD" );
                });
			} else {
                console.log( "NOT LOGGED IN" );
            }
		};

        $scope.showQuestion = function( newQuestion ) {
            if (angular.isUndefined(newQuestion.descriptions)) {
                // well... no description found...
                $scope.finished = true;
            } else {

                // only get German for now
                newQuestion.descriptions.forEach(function (description) {
                    if (angular.equals(description.language_name, "Deutsch")) {
                        newQuestion.german_description = description.description;
                    }
                });

                var questionToAdd = {
                    'id': newQuestion.question_id,
                    'status': 'unanswered',
                    'subject': newQuestion.name,
                    'text': newQuestion.german_description,    // TODO: I18N
                    'answer_yes': newQuestion.answer_yes,
                    'answer_no': newQuestion.answer_no
                };
                //$scope.questions.push(questionToAdd);
                $scope.finished = false;
                console.log(questionToAdd);
                $scope.currentQuestion = questionToAdd;
                $scope.loading = false;

                $("#contentContainer").animate({ scrollTop: $(document).height() }, "slow");
            }
        };

        $scope.askDiagnosisOk = function( diagnosis, e){
            var confirm = $mdDialog.confirm()
                .title('Diagnose Gefunden')
                .content( 'Unsere Diagnose: ' + diagnosis.german_description )
                .ariaLabel('Lucky day')
                .ok('Diagnose Ok')
                .cancel('Weitere Fragen beantworten')
                .targetEvent(e);

            $mdDialog.show(confirm).then(function() {
                // Ok
                console.log( "Yes, that's it!" );
                // show details
                $scope.questions = {};
                User.run.recommendation = "";
                User.run.diagnosis = diagnosis.german_description;

                if ( diagnosis.action_suggestions.length ) {
                    diagnosis.action_suggestions.forEach(function (action) {
                        action.descriptions.forEach(function (desc) {
                            if (angular.equals(desc.language_name, "Deutsch")) {
                                User.run.recommendation += desc.description;
                            }
                        });
                    });
                }

                debugger;

                $location.url("/diagnosis");
            }, function() {
                console.log( "No, continue" );
            });
        };

        /**
         * Restarts the run and starts a new one.
         */
        $scope.restartRun = function() {
            if ($scope.user.loggedIn) {
                runFactory.resetRun({Id: $scope.user.id}, function (result) {
                    $scope.finished = false;
                    $scope.currentQuestion = {};
                    $scope.getQuestion();
                });
            } else {
                console.log("NOT LOGGED IN");
            }

        };
		
		/**
		 * Gets triggered when a question was answered
		 * @param  {[type]}
		 * @param  {[type]}
		 */
		$scope.answerQuestion = function( answer ) {
			
			var question = $scope.currentQuestion;
			console.log(question, answer);
			question.status = answer;

			var idToSend;
			if ( answer === "positive") {
				idToSend = question.answer_yes;
			} else {
				idToSend = question.answer_no;
			}
			
			$scope.questions.push(question);
			$scope.currentQuestion = {};
            $scope.loading = true;

			//send answer to / run / user /:id (as PUT and with request answer_id : < Number >)
			var asd = runFactory.sendAnswer({Id: $scope.user.id}, {answer_id: idToSend.answer_id}, function (result) {
				// get next question
				$scope.getQuestion();
			});

		};
		/**
		 * checks if a question was answered already
		 * @param  {[type]}
		 * @return {Boolean}
		 */
//		$scope.isAnswered = function( question ) {
//		  return question.status === "unanswered";
//		}
		/**
		 * returns to the main page
		 * @return {[type]}
		 */
		$scope.goBack = function() {
		  $location.url('/');
		};
        $scope.goToRun = function() {
            $location.url('/run');
        };
		$scope.isLoading = function() {
			return $scope.loading;
		};

        $scope.start();
	} ]);

	pocketdocControllers.controller('mainController', [ "$http", "$scope", "$location", function( $http, $scope, $location ) {

        $scope.run = function() {
		  $location.url('/run');
		};
	} ]);

/*	
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
	
*/
	
})();
