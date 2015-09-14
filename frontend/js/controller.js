(function() {

	var pocketdocControllers = angular.module('pocketdocControllers', ['pocketdocBackend', 'pocketdocServices', 'ngMessages']);

	pocketdocControllers.controller('questionController',
	        ['$scope', '$location', 'RunService', 'DiagnosisData', '$mdDialog', 'DiagnosisService', '$translate', 'UserService', 'MetaDataService',
	function( $scope ,  $location ,  RunService ,  DiagnosisData ,  $mdDialog ,  DiagnosisService ,  $translate ,  UserService ,  MetaDataService) {
	
        var followUp = RunService.getFollowUp(),
            isFollowUp = followUp != null && followUp != false;

        $scope.isPreDiag = !isFollowUp; // Only show PreDiag when NOT in FollowUp-Mode
        $scope.forCurrentUser = true;
        $scope.isLoggedIn = UserService.isLoggedIn();
        $scope.user = UserService.getCurrentUser();
        $scope.revise = false;
        
        $scope.languages = MetaDataService.getLanguages();
        $scope.ageRanges = MetaDataService.getAgeRanges();
        
        $scope.checkValidity = function(){
            $scope.dataInvalid = typeof($scope.user.gender) === 'undefined' ||
                                 typeof($scope.user.age_category) === 'undefined' ;
                                 
            if ($scope.loggedIn)
                $scope.dataInvalid = typeof($scope.user.name) === 'undefined' ||
                                     $scope.user.name === "" ||
                                     $scope.dataInvalid;
        };
        
        $scope.checkValidity();
        
        $scope.changeCurrentUser = function( cur ) {
            $scope.forCurrentUser = cur;

            if ( cur ) {
                // Get Data and display it, e.g. name
                $scope.user = UserService.getCurrentUser();
            } else {
                // Remove Data from the currentUser
                $scope.user = {}
            }
        };

        $scope.loading = true;
		$scope.hidden = true;
        $scope.currentQuestion;
        $scope.answeredQuestions = [];
		
        /**
         * Sets the gender of the to-be-registered user.
         * 
         * @param {Number}
         * @author Roman Eichenberger, Philipp Christen
         */
		$scope.setGender = function(gender) {
            $scope.user.gender = gender;
		};

        /**
         * Changes the language of the to-be-registered user and tells the app
         * to use this language from now on.
         * 
         * @param  {Number}
         * @author Roman Eichenberger, Philipp Christen
         */
		$scope.changeLanguage = function(lang) {
            UserService.updateLanguage(
                {
                    lang: lang
                },
                function(data){
                    $translate.use( data.lang ).then(
                        function ( lang ) {
                            $scope.user.lang = lang;
                            $scope.$root.$broadcast( "languageChange", lang );
                        },
                        function ( lang ) {
                            console.log("Error occured while changing language");
                        }
                    );
                },
                function(error){
                    alert(error);
                }
            );
		};
        
        /**
         * Starts the actual run after saving the data for the current run.
         * 
         * @name confirmPreDiag
         * @author Philipp Christen
         */
        $scope.confirmPreDiag = function() {
            $scope.isPreDiag = false;

            //UserService.

            RunService.startRun(
                $scope.user,
                function( questionData ) {
                    // Success
                    $scope.currentQuestion = questionData;
                    $scope.loading = false;
                    $scope.hidden = false;
                },
                function( error ) {
                    alert( error );
                }
            );
        };
		
		$scope.answerQuestion = function( givenAnswer ) {
            $scope.loading = true;
			$scope.hidden = true;

            // Add previous question to the list of answered questions
            // "position" is used for getting the position in the list
            $scope.answeredQuestions.push(
                {
                    position: $scope.answeredQuestions.length,
                    question: $scope.currentQuestion,
                    answer: givenAnswer
                }
            );

            // then, check if the answer had a diagnosis/actionSuggestion attached to it
            if ( typeof( givenAnswer.diagnosis ) !== "undefined" &&
                 typeof( givenAnswer.action_suggestion ) !== "undefined" ) {

                DiagnosisService.getByID(
                    givenAnswer.diagnosis,
                    givenAnswer.action_suggestion,
                    function( diag ) {
                        $scope.showDialog(
                            givenAnswer,
                            diag.diagnosis,
                            diag.action_suggestion
                        );
                    },
                    function( error ) {
                        alert( error );
                    }
                );

            } else {
                $scope.givenAnswer = undefined;
                $scope.showNewQuestion( givenAnswer );
            }
		};

        /**
         * Mini-Controller for Custom Dialog, provides some simple methods
         * and makes the passed data available in the template.
         * 
         * @param {[type]} $scope           [description]
         * @param {[type]} $mdDialog        [description]
         * @param {[type]} diagnosis        [description]
         * @param {[type]} actionSuggestion [description]
         * @author Philipp Christen
         */
        var DialogController = function($scope, $mdDialog, diagnosis, actionSuggestion) {
            $scope.diagnosis = diagnosis;
            $scope.actionSuggestion = actionSuggestion;

            $scope.cancel = function() { $mdDialog.cancel(); };
            $scope.accept = function() { $mdDialog.hide(); };
        };

        /**
         * Shows the "diagnosis found" dialog.
         * 
         * @param  {[type]} givenAnswer      [description]
         * @param  {[type]} diagnosis        [description]
         * @param  {[type]} actionSuggestion [description]
         * @param  {jQuery.Event} ev [description]
         * @author Philipp Christen, Roman Eichenberger
         */
        $scope.showDialog = function( givenAnswer, diagnosis, actionSuggestion, ev ) {
            $scope.loading = false;
            $mdDialog.show({
                controller: DialogController,
                templateUrl: '../partials/diagDialog.html',
                targetEvent: ev,
                resolve: {
                    diagnosis: function(){ return diagnosis; },
                    actionSuggestion: function(){ return actionSuggestion; }
                }
            })
            .then( function() {
                RunService.acceptDiagnosis(
                    undefined,
                    function() {
                        $location.url("/diagnosis").replace();
                        DiagnosisData.diagnosis = diagnosis;
                        DiagnosisData.actionSuggestion = actionSuggestion;
                        DiagnosisData.userData = RunService.getUserData();
                    },
                    function( error ) {
                        alert( error );
                    }
                );
            }, function() {
                $scope.showNewQuestion( givenAnswer );
            });
        };

        $scope.showNewQuestion = function( givenAnswer ) {
            
            var success = function( questionData ) {
                // Show new question
                $scope.currentQuestion = questionData;
                $scope.loading = false;
                $scope.hidden = false;
            };
            
            var error = function( message ) {
                $scope.loading = false;
                // Error occured. Show Dialogue.
                $mdDialog.show(
                    $mdDialog.alert()
                        .title( $translate.instant('error_noMoreQuestions_title') )
                        .content( message )
                        .ariaLabel('Alert Dialog Demo')
                        .ok( $translate.instant('common_ok') )
                ).finally( function() {
                    $scope.goToMain();
                });
            };
            
            if (!$scope.revise) {
                RunService.answerQuestion(
                    {
                        question: $scope.currentQuestion,
                        answerId : givenAnswer.id
                    },
                    success,
                    error
                );
            } else {
                RunService.changeAnswer(
                    {
                        questionId: $scope.currentQuestion.id,
                        answerId: givenAnswer.id
                    },
                    success,
                    error
                );
            }
        };

        /**
         * User clicked on a question that they already answered to answer
         * it again.
         * 
         * @param  {Object} question
         * @param  {jQuery.Event} $event
         * @author Philipp Christen
         */
        $scope.reviseQuestion = function( qData, $event ) {
            var pos = qData.position;
            $scope.answeredQuestions.splice( pos, $scope.answeredQuestions.length-pos+1 );

            $scope.currentQuestion = qData.question;
            $scope.revise = true;
        };

        // immediately start with Run if it's a followUp
        if ( !$scope.isPreDiag ) {
            $scope.confirmPreDiag();
        }
	}]);
	
	pocketdocControllers.controller('diagnosisController',
            ['$scope', '$location', '$window', 'DiagnosisData', 'FollowUpData', 'UserService', 'FollowupService', 'RunService', '$mdDialog',
    function( $scope ,  $location ,  $window ,  DiagnosisData ,  FollowUpData ,  UserService ,  FollowupService ,  RunService ,  $mdDialog ) {
		
		$scope.diagnosis = DiagnosisData.diagnosis;
		$scope.actionSuggestion = DiagnosisData.actionSuggestion;
        $scope.followUp = RunService.getFollowUp();
        $scope.isFollowUp = $scope.followUp != null && $scope.followUp != false;
        $scope.isSameDiag = false;
        $scope.isLoggedIn = UserService.isLoggedIn();

        if ( !$scope.diagnosis) {
            $scope.goToMain();
        }


        $scope.goToMain = function() { $window.history.back(); /*$location.url('/'); */ };

        /**
         * User wants to register for a followUp. If they're already logged in,
         * that's alright, otherwise the login-dialogue gets shown.
         *
         * @name acceptFollowUp
         * @author Philipp Christen
         */
        $scope.acceptFollowUp = function() {
            if ( $scope.isLoggedIn ) {
                $scope.addFollowUp();
            } else {
                $scope.showLoginDialog();
            }
        };

        /**
         * add a follow-up to the existing ones for the current user.
         * Only registered and logged in users can do that, though.
         * A follow-up contains the old diagnosis and action suggestion,
         * as well as the owner (user) and the question where it should start.
         * Additionally, the current time will be saved, too.
         *
         * @name addFollowUp
         * @author Philipp Christen
         */
        $scope.addFollowUp = function() {
            // only save follow-up when logged in!
            var userID = UserService.getCurrentUser().id;

            if ( userID > -1 ) {
                var followUpData = $scope.getFollowUpData();
                followUpData.user = userID;
                followUpData.newest = true;
                
                FollowupService.registerFollowup( followUpData );
                $window.history.back(); // $location.url('/');
            } else {
                console.log( "Error: not logged in" );
            }
        };
        
        $scope.getFollowUpData = function(){
            return {
                "oldDiagnosis": $scope.diagnosis.id,
                "oldActionSuggestion": $scope.actionSuggestion.id,
                "startQuestion": 5,
                "timeAdded": Date.now()
            };  
        };

        /**
         * Shows the login dialogue.
         *
         * @name login
         * @author Philipp Christen
         */
        $scope.showLoginDialog = function() {
            $mdDialog.show({
                templateUrl: '../partials/loginDialog.html',
                clickOutsideToClose: true
            })
            .then( function( goToRegistration ) {
                $scope.isLoggedIn = UserService.isLoggedIn();

                if ( goToRegistration ) {
                    FollowUpData.data = $scope.getFollowUpData();
                    FollowUpData.userData = DiagnosisData.userData;
                    $location.url("/registration").replace();
                } else {
                    $scope.addFollowUp();
                }
            }, function() {
                console.log( "error" );
            });
        };
	}]);
	
    /**
     * Gets used to handle all login forms.
     *
     * @name loginController
     * @return {[type]}
     * @author Roman Eichenberger, Philipp Christen
     */
    pocketdocControllers.controller('loginController',
            ['$scope', '$location', '$mdDialog', 'UserService', '$route',
    function( $scope ,  $location ,  $mdDialog ,  UserService ,  $route ){
        
        $scope.location = $location;
        $scope.user = {};
        
        // Prevents changing of the location by using the backspace-button
        // or any other non-app-key. But it closes the dialog, giving
        // control back to the original scope where there's no listener.
        $scope.$on("$routeChangeStart", function (event, next, current) {
            $scope.loginDialogCancel();
            event.preventDefault();
        });
        
        $scope.loginDialogCancel = function() { $mdDialog.cancel(); };
            
        $scope.loginDialogRegister = function() { $mdDialog.hide( true ); };

        $scope.loginDialogSubmit = function() {
            
            $scope.loginForm.loginEmail.$setValidity('notFound', true);
            $scope.loginForm.loginEmail.$setValidity('notSet', true);
            $scope.loginForm.loginPassword.$setValidity('wrong', true);
            
            UserService.loginUser(
                {
                    email : $scope.user.email,
                    password : $scope.user.password 
                },
                function( data ) {
                    $scope.loggedIn = true;
                    $scope.$root.$broadcast("login", data);
                    $scope.user = {};
                    $mdDialog.hide( false );
                },
                function( error ) {
                    if (error.errorType == 0)
                        $scope.loginForm.loginEmail.$setValidity('notFound', false);
                    else if (error.errorType == 1)
                        $scope.loginForm.loginPassword.$setValidity('wrong', false);
                    else if (error.errorType == 2)
                        $scope.loginForm.loginEmail.$setValidity('notSet', false);
                    else
                        console.log( error.message );
                        
                    $scope.login_error = error.message;
                }
            );
            
        };
        
        /**
         * Mini-Controller for Custom Dialog, provides some simple methods.
         * 
         * @param {[type]} $scope           [description]
         * @param {[type]} $mdDialog        [description]
         * @author Philipp Christen
         */
        var ForgotPasswordController = function($scope, $mdDialog) {
            $scope.cancel = function() { $mdDialog.cancel(); };
            $scope.accept = function() { $mdDialog.hide(); };
        };

        /**
         * Shows the "Reset Password" dialogue.
         * 
         * @return {[type]} [description]
         * @author Philipp Christen
         */
        $scope.forgotPassword = function() {
            $mdDialog.hide();
            $mdDialog.show({
                controller: ForgotPasswordController,
                templateUrl: '../partials/forgotPasswordDialog.html',
                resolve: {
                }
            })
            .then( function() {}, function() {});
        };
    }]);
    
    /**
     * Gets used on the registration page and handles all interaction there.
     *
     * @name registrationController
     * @return {[type]}
     * @author Roman Eichenberger, Philipp Christen
     */
	pocketdocControllers.controller('registrationController',
            ['$scope', '$location', '$translate', '$window', '$mdDialog', 'FollowUpData', 'UserService', 'FollowupService', 'MetaDataService',
    function( $scope ,  $location ,  $translate ,  $window ,  $mdDialog ,  FollowUpData ,  UserService ,  FollowupService ,  MetaDataService ) {
		
        $scope.acceptedTerms = false;
		$scope.isProfile = UserService.getCurrentUser().id >= 0;
        $scope.languages = MetaDataService.getLanguages();
        $scope.ageRanges = MetaDataService.getAgeRanges();
        
        $scope.dataInvalid = true;
		
		$scope.user = UserService.getCurrentUser();
		var oldEmail = $scope.user.email;
		
        if (typeof(FollowUpData.userData) !== 'undefined')
        {
            $scope.user = _.extend($scope.user, FollowUpData.userData);
            delete FollowUpData.userData;
        }
        
		if ($scope.isProfile) {
			$scope.user.password = "";
        }
		
        $scope.checkValidity = function(){
        
            $scope.dataInvalid = false;
            $scope.dataInvalid = typeof($scope.user.name) === "undefined" || $scope.dataInvalid;
            $scope.dataInvalid = typeof($scope.user.gender) === "undefined" || $scope.dataInvalid;
            $scope.dataInvalid = typeof($scope.user.age_category) === "undefined" || $scope.dataInvalid;
            $scope.dataInvalid = $scope.registrationForm.$invalid || $scope.dataInvalid;
            
        };
        
		$scope.checkEmail = function(){
			$scope.checkPassword();
            
			if ( $scope.isProfile && oldEmail === $scope.user.email ) {
				$scope.registrationForm.email.$setValidity('used', true);
			} else {
				UserService.isEmailInUse(
					{
						email: $scope.user.email
					},
					function(data){
						$scope.registrationForm.email.$setValidity('used', !data.inUse);
					},
					function(error){
						
					}
				);
			}
		};
		
		$scope.checkPassword = function(){
            $scope.registrationForm.oldPassword.$setValidity('wrong', true);
            
			if ( $scope.isProfile ) {
				var oldPw = $scope.user.oldPassword;
				var newPw = $scope.user.newPassword;
				var valid = ( typeof(oldPw) !== "undefined" && oldPw !== "") 
                         || ((typeof(newPw) === "undefined" || newPw === "")
                         && $scope.user.email === oldEmail);
				$scope.registrationForm.oldPassword.$setValidity('req', valid);
			} else {
				$scope.registrationForm.regPassword.$setValidity('req', regPassword.value !== "");
			}
		};
        
        /**
         * Sets the gender of the to-be-registered user.
         * 
         * @param {Number}
         * @author Roman Eichenberger, Philipp Christen
         */
		$scope.setGender = function(gender) {
            $scope.user.gender = gender;
		};

        /**
         * Changes the language of the to-be-registered user and tells the app
         * to use this language from now on.
         * 
         * @param  {Number}
         * @author Roman Eichenberger, Philipp Christen
         */
		$scope.changeLanguage = function(lang) {
            UserService.updateLanguage(
                {
                    lang: lang
                },
                function(data){
                    $translate.use( data.lang ).then(
                        function ( lang ) {
                            $scope.user.lang = lang;
                            $scope.$root.$broadcast( "languageChange", lang );
                        },
                        function ( lang ) {
                            console.log("Error occured while changing language");
                        }
                    );
                },
                function(error){
                    alert(error);
                }
            );
		};
		
        /**
         * [cancelClick description]
         * @author Roman Eichenberger
         */
		$scope.cancelClick = function() {
			$window.history.back(); //$location.url('/');
		};
		
        /**
         * Click on Register --> Ask user if the email-address is correct.
         * If so, register this user, if not, close dialog and wait.
         * 
         * @author Philipp Christen
         */
		$scope.registerClick = function() {
            var email = $scope.user.email;

            var confirm = $mdDialog.confirm()
                .title( $translate.instant('reg_correctMail_title') )
                .content( $translate.instant('reg_correctMail_content', { mail: email }) )
                .ariaLabel( $translate.instant('reg_correctMail_title') )
                .ok( $translate.instant('common_yes') )
                .cancel( $translate.instant('common_no') )
                .clickOutsideToClose(false);

            $mdDialog.show( confirm ).then(
                function() {
                    $scope.register();
                },
                function() {
                }
            );
		};

        /**
         * Actually registers a new user.
         * 
         * @author Roman Eichenberger, Philipp Christen
         */
        $scope.register = function() {
            UserService.createUser(
                $scope.user,
                function( data ) {
                    $scope.$root.$broadcast("login", data);  
                    
                    if (typeof(FollowUpData.data) !== 'undefined' )
                    {
                        FollowUpData.data.user = data.id;
                        FollowupService.registerFollowup(
                            FollowUpData.data,
                            function(data){},
                            function(error){
                                alert(error);
                            }
                        );
                    }
                    
                    $window.history.back(); //$location.url('/');
                },
                function( error ) {
                    alert( error );
                }
            );
        };
		
        /**
         * User clicks on "Save" which should save the changes made on the user
         * 
         * @author Roman Eichenberger, Philipp Christen
         */
		$scope.saveClick = function(){
            UserService.updateUser(
				$scope.user,
				function( data ){
					$translate.use( $scope.user.lang );
					$window.history.back(); // $location.url('/');
				},
				function( error ){
                    if (error.errorType === 1)
                        $scope.registrationForm.oldPassword.$setValidity('wrong', false);
                    else
    					alert( error.message );
				}
			);
		};
		
        /**
         * User clicks on "Delete Profile" which asks the user if they're
         * serious and if so, deletes the profile.
         * 
         * @author Roman Eichenberger, Philipp Christen
         */
		$scope.deleteClick = function(){
			
			var confirm = $mdDialog.confirm()
				.title( $translate.instant('reg_deleteProfile_title') )
				.content( $translate.instant('reg_deleteProfile_content') )
				.ariaLabel( $translate.instant('reg_deleteProfile_title') )
				.ok( $translate.instant('common_yes') )
				.cancel( $translate.instant('common_no') )
				.clickOutsideToClose(false);

			$mdDialog.show( confirm ).then(
				function() {
					UserService.deleteUser(
						undefined,
						function( data ){
							$scope.$root.$broadcast("logout", data);
							$window.history.back(); // $location.url('/');
						},
						function( error ){
							alert( error );
						}
					);
				},
				function() {
				}
			);
		};

        /**
         * Moved from HeaderController, will be handled here now.
         * Goes back to the main screen and logs the user out.
         *
         * @author Philipp Christen
         */
        $scope.logout = function() {
            UserService.logoutUser(
                {},
                function( data ) {
                    $scope.$root.$broadcast("logout", data);
                    $window.history.back(); // $location.url('/');
                },
                function( error ) {
                    alert( error );
                }
            );
        };
        
        /**
         * Mini-Controller for terms dialog.
         * 
         * @author Roman Eichenberger
         */
        var DialogController = function($scope, $mdDialog) {
            
            $scope.close = function() { $mdDialog.cancel(); };
        };
        
        /**
         * Show terms in dialog.
         * 
         * @author Roman Eichenberger
         */
        $scope.showTermsDialog = function() {
            $mdDialog.show({
                controller: DialogController,
                templateUrl: '../partials/TermsDialog.html',
                clickOutsideToClose: true
            });
        };
        
        
		
	}]);
	
	pocketdocControllers.controller('mainController',
           [ '_', '$scope', '$location', '$http', '$translate', 'UserService', 'FollowupService', '$mdDialog', 'DiagnosisService', '$interval', 'RunService',  
    function( _ ,  $scope ,  $location ,  $http ,  $translate ,  UserService ,  FollowupService ,  $mdDialog ,  DiagnosisService ,  $interval ,  RunService ) {
		
        $scope.followUps = [];

        $scope.hasNoFollowUps = function() { return _.isEmpty( $scope.followUps ); }

        /**
         * Run has to be told that there's NO followUp.
         *
         */
        $scope.run = function() {
            RunService.setFollowUp( null );
            $location.url('/run');
		};
		
		$scope.$on( "login", function( event, data ) {
            $scope.handleLogin( data );
		});
		
		$scope.$on( "logout", function( event, data ) {
			$scope.handleLogout();
		});

        /**
         * Delete a followUp from the list on the main page.
         * Shows a confirmation dialog to the user before deleting it.
         * 
         * @param  {[type]} followUp [description]
         * @param  {[type]} $event   [description]
         * @author Philipp Christen
         */
        $scope.deleteFollowUp = function ( followUp, $event ) {
            var confirm = $mdDialog.confirm()
                .title( $translate.instant('main_followUp_delete_title') )
                .content( $translate.instant('main_followUp_delete_content') )
                .ariaLabel( $translate.instant('main_followUp_delete_title') )
                .ok( $translate.instant('common_yes') )
                .cancel( $translate.instant('common_no') )
                .clickOutsideToClose(false);

            $mdDialog.show( confirm ).then(
                function() {
                    FollowupService.deleteFollowup(
                        followUp.id,
                        function( removedID ){
                            $scope.followUps = _.reject( $scope.followUps, function(fUp){ return fUp.id === removedID; });
                        },
                        function( error ){
                            alert( error );
                        }
                    );
                },
                function() { /* wasn't successfull */ }
            );
        };

        /**
         * Starts a followUp.
         *
         * Idea: Save active followUp on the user, then check if there's an
         * active followUp when the run gets started (qController) and act
         * accordingly.
         *
         * @name   startFollowUp
         * @param  {Object} followUp
         * @param  {jQuery.event} $event
         * @author Philipp Christen
         */
        $scope.startFollowUp = function ( followUp, $event ) {
            FollowupService.startFollowup( followUp.id );
            $location.url( '/run' );
        };

        /**
         * Gets called upon login. Sets certain values that are provided by the
         * logged in user, e.g. their name.
         *
         * @name   handleLogin
         * @param  {Object} user
         * @author Philipp Christen
         */
        $scope.handleLogin = function ( user ) {
            $scope.userName = user.name;
            $scope.loggedIn = true;
            $scope.followUps = FollowupService.getFollowupsForUser( user.id );
            currentUser = user;
        };

        $scope.getDiagnosis = function( followUp ) {
            return DiagnosisService.getDiagByID( followUp.oldDiagnosis ).short_desc;
        };

        /**
         * Gets called when a user logged out. Resets user-related settings.
         *
         * @name   handleLogout
         * @author Philipp Christen
         */
        $scope.handleLogout = function () {
            $scope.userName = "";
            $scope.loggedIn = false;
            $scope.followUps = [];
            currentUser = undefined;
        };

        /**
         * Calculates the age of the followUp and returns true if it's less
         * than 3 hours.
         * 
         * @param  {Timestamp}  timeAdded
         * @return {Boolean}
         * @author Philipp Christen
         */
        $scope.isFollowUpReady = function( timeAdded ) {
            return new Date() - timeAdded > 3*60*60 *1000;
        };

        $scope.getRemainingTime = function( timeAdded ) {
            return timeAdded + 3*60*60*1000 - new Date();
        };

        /**
         * Checks if a string is empty or not set.
         * 
         * @param  {String}  str
         * @return {Boolean}
         * @author Philipp Christen
         */
        $scope.isEmpty = function( str ) {
            return !str || str === "";
        };

        /*
        if followUp is "locked", count down. for that we need to poke angular
        every second...
        */
        $interval(function(){
            // nothing is required here, interval triggers digest automaticaly
        },1000)
        
        var currentUser = UserService.getCurrentUser();

        $translate.use( currentUser.lang );
        
        if ( currentUser.id !== -1 ) {
            $scope.handleLogin( currentUser );
        } else {
            $scope.handleLogout();
        }

        // Animate the newest followUp and mark it as read.
        if ( !$scope.hasNoFollowUps() ) {
            _.each( $scope.followUps, function( fUp ) {
                if ( fUp.newest ) {
                    FollowupService.markAsRead( fUp.id );
                }
            });
        }
	}]);

    pocketdocControllers.controller('HeaderController',
            ['$scope', '$rootScope', '$window', '$mdDialog', '$timeout', '$mdSidenav', '$log', '$translate', '$location', 'UserService', 'MetaDataService', '$cookies',
    function( $scope ,  $rootScope ,  $window ,  $mdDialog ,  $timeout ,  $mdSidenav ,  $log ,  $translate ,  $location ,  UserService ,  MetaDataService ,  $cookies ) {
		
        $scope.lang = UserService.getLang();
        $scope.languages = MetaDataService.getLanguages();
		$scope.location = $location;
		
		$scope.$on( "login", function( event, data ) {
            handleLogin( data );
        });
        
        $scope.$on( "logout", function( event, data ) {
            handleLogout( data );
        });

        var handleLogin = function( data ) {
            $scope.loggedIn = true;
            $scope.lang = UserService.getLang();
            $translate.use( $scope.lang );
        };

        var handleLogout = function( data ) {
            $scope.loggedIn = false;
            $scope.lang = UserService.getLang();
            $translate.use( $scope.lang );
        };

        $scope.$on( "languageChange", function( event, lang ) {
            $scope.lang = lang;
            $translate.use( lang );
        });
		
		$scope.$on("resize", function(event, data){
			$scope.resize();
		});

        $scope.changeLanguage = function( lang ) {
            UserService.updateLanguage(
				{
					lang: lang
				},
				function(data){
                    $translate.use( data.lang ).then(
                        function ( lang ) {
					        $scope.lang = data.lang;
                        },
                        function ( lang ) {
							console.log("Error occured while changing language");
						}
                    );
				},
				function(error){
					alert(error);
				}
			);
        };

        $scope.openMenu = function() {
            $mdSidenav( 'left' ).open();
        };

        $scope.close = function () {
            $mdSidenav('left').close();
        };

        $scope.profile = function() {
            $scope.close();
            $location.url("/profile");
        };

        /**
         * Shows dialog if a functionality is not yet implemented.
         * 
         * @param  {String} functionality
         * @author Philipp Christen
         */
        $scope.notImplementedYet = function( functionality ) {
            $mdDialog.show(
                $mdDialog.alert()
                    .title( $translate.instant('common_notImplemented') )
                    .content( $translate.instant('common_notImplemented_content', { fkt: functionality } ) )
                    .ariaLabel( $translate.instant('common_notImplemented') )
                    .ok( $translate.instant('common_ok') )
            );
        };

        /**
         * Navigates back to the main page.
         * 
         * @author Philipp Christen
         */
        $scope.goToMain = function () {
            $window.history.back(); // $location.url("/");
        };

        /**
         * RESET ALL LOCALSTORAGE FOR DEMONSTRATION PURPOSES!
         * 
         * @author Philipp Christen
         */
        $scope.resetForDemo = function() {
            var confirm = $mdDialog.confirm()
              .title( $translate.instant('header_reset_title') )
              .content( $translate.instant('header_reset_content') )
              .ariaLabel( $translate.instant('header_reset_title') )
              .ok( $translate.instant('common_yes') )
              .cancel( $translate.instant('common_no') );

            $mdDialog.show(confirm).then(function() {
                localStorage.clear();
                window.location.reload();
            }, function() {});
        };

        /**
         * Shows the Dialogue for logging in or registering.
         * 
         * @name showLoginDialog
         * @author Philipp Christen
         */
        $scope.showLoginDialog = function() {
            if ( $scope.loggedIn ) {
                $scope.profile();
            } else {
                $mdDialog.show({
                    templateUrl: '../partials/loginDialog.html',
                    clickOutsideToClose: true
                })
                .then( function( goToRegistration) {
                    if ( goToRegistration ) {
                        $location.url("/registration");
                    }
                }, function() {
                    console.log( "error" );
                });
            }
        };
        
        /**
         * Shows the About screen.
         * 
         * @author Roman Eichenberger
         */
        $scope.showAbout = function(){
            $scope.close();
            $location.url("/about");
        };
        
        /**
         * Shows the Terms screen
         * 
         * @author Roman Eicheberger
         */
        $scope.showTerms = function(){
            $scope.close();
            $location.url("/terms");
        };

        /**
         * Gets triggered on start
         *
         * @name start
         * @author Philipp Christen
         */
        var start = function() {
            var existingUser = UserService.getCurrentUser();

            if ( existingUser.id === -1 ) {
                var cookieUser = $cookies.pocketDocUser;
                
                if ( cookieUser && cookieUser !== "null" ) {
                    cookieUser = angular.fromJson( cookieUser );

                    UserService.loginUser(
                        cookieUser,
                        function( data ) {
                            $scope.$root.$broadcast("login", data);
                        },
                        function( error ) { console.log( error); }
                    );
                }
                
            }
        }();
    }]);

})();
