'use strict';

/* Controllers */
var pocketdocControllers = angular.module('pocketdocControllers', []);

pocketdocControllers.controller('questionController',
    [ "$http", "$scope", "$location", '$cookies','$cookieStore', "User", "nextQuestionFactory", "runFactory", "actionSuggestionFactory",
        function( $http, $scope, $location, $cookies, $cookieStore, User, nextQuestionFactory, runFactory, actionSuggestionFactory ) {

    $scope.user = User
    $scope.finished = false;

    $scope.no_suggestion = "Noch keine Handlungsempfehlung möglich.";
    $scope.no_diagnosis = "Noch keine Diagnosis möglich.";
    $scope.questions = [
        {
            'id'     : 3,
            'status' : 'unanswered',
            'subject': 'Bauchschmerzen?',
            'text'   : 'Haben Sie Bauchschmerzen?',
            'answer_yes': 12,
            'answer_no': 13
        },
        {
            'id'     : 4,
            'status' : 'unanswered',
            'subject': 'Kopfschmerzen?',
            'text'   : 'Haben Sie Kopfschmerzen?',
            'answer_yes': 14,
            'answer_no': 15
        }
    ];
    $scope.loading = false;
    $scope.diagnosis = null;
    $scope.recommendation = null;


    $scope.login = function() {
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
               debugger;
               //$scope.getQuestion();
           }).error(function(data, status, headers, config) {
               console.log( "ERROR on Login:", data, status, headers, config );
           });
       }
    }

    $scope.getDiagnosis = function() {
        if ($scope.user.loggedIn) {

            runFactory.getDiagnosis({Id: $scope.user.id}, function (result) {
                debugger;

            });
        } else {
            console.log("NOT LOGGED IN");
        }
    }

    $scope.getQuestion = function() {
        if ($scope.user.loggedIn) {
            $scope.loading = true;

            return nextQuestionFactory.get({Id: $scope.user.id}, function (newQuestion) {
                $scope.loading = false;

                // before showing the next question, check if there's a diagnosis!
                runFactory.getDiagnosis({Id: $scope.user.id}, function (diagnosisResult) {
                    debugger;

                    if( angular.isUndefined( diagnosisResult.diagnosis ) ) {
                        //no diagnosis found, get show next question
                        $scope.showQuestion( newQuestion );
                    } else {
                        //show diagnosis now. If not OK with the user, continue with the questions

                        diagnosisResult.diagnosis.descriptions.forEach(function (description) {
                            if (angular.equals(description.language_name, "Deutsch")) {
                                diagnosisResult.german_description = description.description;
                            }
                        });
                        var accepted = confirm( "Diagnosis:" + diagnosisResult.german_description );

                        if ( accepted ) {
                            // show details
                            console.log("SHOW DETAILS");
                            $scope.questions = {};
                            $scope.recommendation = null;
                            $scope.diagnosis = diagnosisResult.german_description;

                            if ( diagnodiagnosisResult.action_suggestion.length ) {
                                diagnosisResult.action_suggestion.forEach(function (action) {
                                    action.descriptions.forEach(function (desc) {
                                        if (angular.equals(desc.language_name, "Deutsch")) {
                                            $scope.recommendation += desc.description;
                                        }
                                    });
                                });
                            }
                            //get actionSuggestion
                            $scope.getActionSuggestion();

                        } else {
                            // continue with question
                            $scope.showQuestion( newQuestion );
                        }
                    }
                });

            });
        } else {
            console.log( "NOT LOGGED IN" );
        }
    }


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
            $scope.questions.push(questionToAdd);
            $scope.finished = false;
        }
    }

    $scope.getActionSuggestion = function() {
        if ($scope.user.loggedIn) {

            actionSuggestionFactory.getSuggestion({Id: $scope.user.id}, function (result) {
                debugger;

                result.descriptions.forEach(function (description) {
                    if (angular.equals(description.language_name, "Deutsch")) {
                        result.german_description = description.description;
                    }
                });

                $scope.recommendation = result.german_description;
            });
        } else {
            console.log("NOT LOGGED IN");
        }
    }

    $scope.restart = function () {
        if ($scope.user.loggedIn) {
            runFactory.resetRun({Id: $scope.user.id}, function (result) {
                $scope.finished = false;
                $scope.questions = [];
                $scope.getQuestion();
            });
        } else {
            console.log("NOT LOGGED IN");
        }
    }

    /**
     * Gets triggered when a question was answered
     * @param  {[type]}
     * @param  {[type]}
     */
    $scope.answerQuestion = function( question, answer ) {
        question.status = answer;

        var idToSend;
        if ( answer === "positive") {
            idToSend = question.answer_yes;
        } else {
            idToSend = question.answer_no;
        }

        //send answer
        runFactory.sendAnswer({Id: $scope.user.id}, {answer_id: idToSend.answer_id}, function (result) {
            // get next question
            $scope.getQuestion();
        });
    }
    /**
     * checks if a question was answered already
     * @param  {[type]}
     * @return {Boolean}
     */
    $scope.isAnswered = function( question ) {
      return question.status === "unanswered";
    }
    /**
     * returns to the main page
     * @return {[type]}
     */
    $scope.goBack = function() {
      $location.url('/');
    }
    $scope.isLoading = function() {
        return $scope.loading;
    }
} ]);

pocketdocControllers.controller('mainController', [ "$http", "$scope", "$location", function( $http, $scope, $location ) {
    $scope.run = function() {
      $location.url('/run');
    }
} ]);