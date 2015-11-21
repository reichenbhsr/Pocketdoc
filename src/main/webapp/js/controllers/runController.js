angular.module('pocketDocApp').controller('runController', function ($scope, $http, $filter, $q, runFactory, nextQuestionFactory, testRunFactory, syndromFactory, answerToDiagnosisScoreDistributionFactory, answerToActionSuggestionScoreDistributionFactory, syndromToActionSuggestionScoreDistributionFactory) {


    $scope.finished = false;
    $scope.userId = 1;
    $scope.oldQuestions = [];
    $scope.action_suggestion_quantity = 3;

    $scope.no_suggestion = "Noch keine Handlungsempfehlung möglich.";
    $scope.no_diagnosis = "Noch keine Diagnosis möglich.";

    /*
     Das ganze funktioniert nur wenn der user eingelogt ist
     */
    if ($scope.user.isLogged) {

        /**
         * Die Funktion loadQuestion lädt die sinnvollste nächste Frage vom Server.
         *
         * Wenn diese eine Antwort abgibt, dann wird die nächste Frage angezeigt.
         *
         * Sonst wird es als Diagnosefund betrachtet, beendet den Fragedurchlauf und zeigt dem Benutzer das Resultat
         *
         * In jedem Fall werden die Rankings geholt
         */
        var loadQuestion = function () {
            return nextQuestionFactory.get({Id: $scope.userId}, function (result) {
                if (angular.isUndefined(result.question)){
                    finish();
                }
                else{
                    result.answer_no = result.question.answer_no;
                    result.answer_yes = result.question.answer_yes;
                    result.question_id = result.question.question_id;
                    result.descriptions = result.question.descriptions;
                    result.name = result.question.name;
                    result.is_symptom = result.question.is_symptom;

                    result.descriptions.forEach(function (description) {
                        if (angular.equals(description.language_name, "Deutsch")) {
                            result.german_description = description.description;
                        }
                    });
                }

                loadTestRunResults();
            });
        }

        /**
         * Mit dieser Funktion wird ein Fragelauf neu gestartet.
         * Dies wird zuerst dem Server mitgeteilt, dann werden Gui Elemente neu gesetzt.
         * Zum Schluss wird die nächste Frage beim Server geholt.
         */
        $scope.restart = function () {
            var remove = $http.delete("/run/user/" + $scope.userId);
            $scope.question = null;
            remove.then(function (result) {
                $scope.finished = false;
                $scope.oldQuestions = [];
                $scope.question = loadQuestion();
            });
        }

        /*
         Als erstes wird die nächste Frage abgeholt und der Testdurchlauf resetet
         */
        $scope.restart();

        /**
         * Diese Funktion holt die Rankings beim Server ab.
         * Es werden Diagnosenranking, ActionSuggestionranking und die Syndrome abgeholt.
         * Sobald diese Daten geholt wurden werden die RankingScoreverteilungen geholt
         * @returns Rankings
         */
        var loadTestRunResults = function () {
            return testRunFactory.get({Id: $scope.userId}, function (result) {
                $scope.testDiagnoses = result.diagnoses;
                $scope.testActionSuggestions = result.action_suggestions;
                $scope.syndroms = syndromFactory.query(function (syndroms) {
                    getRankingMaps();
                });
            });
        }


        /**
         * Diese Funktion holt die Scoreverteilungen der folgenden Elementen ab.
         * - Diagnosen
         * - Handlungsempfehlungen
         * - Syndrome
         *
         * Diese Daten werden in Maps gespeichert, mit dem Key der jeweiligen Id und dem Value Scoreverteilung
         */
        function getRankingMaps() {
            //Diagnoses
            if (angular.isDefined($scope.testDiagnoses)) {
                answerToDiagnosisScoreDistributionFactory.query({Id: $scope.userId}, function (result) {
                    $scope.answerToDiagnosisMap = {};
                    $scope.oldQuestions.forEach(function (question) {

                        var rankingList = [];
                        $scope.testDiagnoses.forEach(function (diagnosis) {
                            var found = false;
                            result.forEach(function (scoreDistribution) {
                                if (diagnosis.diagnosis_id === scoreDistribution.diagnosis_id && question.answer_id === scoreDistribution.answer_id) {
                                    rankingList.push(scoreDistribution);
                                    found = true;
                                }
                            });
                            if (!found) {
                                rankingList.push(undefined);
                            }
                        });
                        $scope.answerToDiagnosisMap[question.answer_id] = angular.copy(rankingList);
                    });
                });
            }

            //Action Suggestion
            if (angular.isDefined($scope.testActionSuggestions)) {
                answerToActionSuggestionScoreDistributionFactory.query({Id: $scope.userId}, function (result) {
                    $scope.answerToActionSuggestionMap = {};
                    $scope.oldQuestions.forEach(function (question) {

                        var rankingList = [];
                        $scope.testActionSuggestions.forEach(function (actionSuggestion) {
                            var found = false;
                            result.forEach(function (scoreDistribution) {
                                if (actionSuggestion.action_suggestion_id === scoreDistribution.action_suggestion_id && question.answer_id === scoreDistribution.answer_id) {
                                    rankingList.push(scoreDistribution);
                                    found = true;
                                }
                            });
                            if (!found) {
                                rankingList.push(undefined);
                            }
                        });
                        $scope.answerToActionSuggestionMap[question.answer_id] = angular.copy(rankingList);
                    });
                });

            }

            //Syndroms
            if (angular.isDefined($scope.testActionSuggestions)) {
                syndromToActionSuggestionScoreDistributionFactory.query({Id: $scope.userId}, function (result) {
                    $scope.syndromToActionSuggestionMap = {};
                    $scope.syndroms.forEach(function (syndrom) {

                        var rankingList = [];
                        $scope.testActionSuggestions.forEach(function (actionSuggestion) {
                            var found = false;
                            result.forEach(function (scoreDistribution) {
                                if (actionSuggestion.action_suggestion_id === scoreDistribution.action_suggestion_id && syndrom.syndrom_id === scoreDistribution.syndrom_id) {
                                    rankingList.push(scoreDistribution);
                                    found = true;
                                }
                            });
                            if (!found) {
                                rankingList.push(undefined);
                            }
                        });
                        $scope.syndromToActionSuggestionMap[syndrom.syndrom_id] = angular.copy(rankingList);
                    });
                });
            }
        }

        /**
         * Diese Funktion lädt die Resultate beim Server.
         * Es werden Diagnose und Handlungsempfehlungsliste abgeholt.
         *
         * Dabei wird überall die deutsche Beschreibung in einer neuen Variable abgespeichert.
         * @returns {*}
         */
        var loadRunResults = function () {
            return runFactory.get({Id: $scope.userId}, function (result) {
                if (angular.isDefined(result.diagnosis)) {
                    result.diagnosis.descriptions.forEach(function (description) {
                        if (angular.equals(description.language_name, "Deutsch")) {
                            result.diagnosis.german_description = description.description;
                        }
                    });
                }
                if (angular.isDefined(result.action_suggestions)) {
                    result.action_suggestions.forEach(function (action_suggestion) {
                        action_suggestion.descriptions.forEach(function (description) {
                            if (angular.equals(description.language_name, "Deutsch")) {
                                action_suggestion.german_description = description.description;
                            }
                        });
                    });
                }
            });
        }

        /**
         * Mit dieser funktion kann ein neues SyndromScore hinzugefügt werden
         * @param syndrom_id
         * @param index Die Plazierung der Handlungsempfehlung in der Tabelle
         */
        $scope.addSyndromScore = function (syndrom_id, index) {
            var distribution = new syndromToActionSuggestionScoreDistributionFactory();
            distribution.action_suggestion_id = $scope.testActionSuggestions[index].action_suggestion_id;
            distribution.syndrom_id = syndrom_id;
            distribution.score = 0;
            distribution.$save(function () {
                getRankingMaps();
            });
        }

        /**
         * Mit dieser funktion kann ein SyndromScore verändert werden
         * @param syndrom_id
         * @param index Die Plazierung der Handlungsempfehlung in der Tabelle
         * @param ranking Das Ranking Objekt in der Tabelle
         */
        $scope.setSyndromScore = function (syndrom_id, index, ranking) {
            var distribution = new syndromToActionSuggestionScoreDistributionFactory();
            distribution.distribution_id = ranking.distribution_id;
            distribution.action_suggestion_id = $scope.testActionSuggestions[index].action_suggestion_id;
            distribution.syndrom_id = syndrom_id;
            distribution.score = ranking.score;
            distribution.$update(function () {
                getRankingMaps();
            });
        }

        /**
         * Mit dieser funktion kann ein neues DiagnoseScore hinzugefügt werden
         * @param answer_id
         * @param index Die Plazierung der diagnose in der Tabelle
         */
        $scope.addDiagnosisScore = function (answer_id, index) {
            var distribution = new answerToDiagnosisScoreDistributionFactory();
            distribution.diagnosis_id = $scope.testDiagnoses[index].diagnosis_id;
            distribution.answer_id = answer_id;
            distribution.score = 0;
            distribution.$save(function () {
                getRankingMaps();
            });
        }

        /**
         * Mit dieser funktion kann ein DiagnoseScore verändert werden
         * @param answer_id
         * @param index Die Plazierung der Diagnose in der Tabelle
         * @param ranking Das Ranking Objekt in der Tabelle
         */
        $scope.setDiagnosisScore = function (answer_id, index, ranking) {
            var distribution = new answerToDiagnosisScoreDistributionFactory();
            distribution.distribution_id = ranking.distribution_id;
            distribution.diagnosis_id = $scope.testDiagnoses[index].diagnosis_id;
            distribution.answer_id = answer_id;
            distribution.score = ranking.score;
            distribution.$update(function () {
                getRankingMaps();
            });
        }

        /**
         * Mit dieser funktion kann ein neues ActionSuggestionScore hinzugefügt werden
         * @param answer_id
         * @param index Die Plazierung der Handlungsempfehlung in der Tabelle
         */
        $scope.addActionSuggestionScore = function (answer_id, index) {
            var distribution = new answerToActionSuggestionScoreDistributionFactory();
            distribution.action_suggestion_id = $scope.testActionSuggestions[index].action_suggestion_id;
            distribution.answer_id = answer_id;
            distribution.score = 0;
            distribution.$save(function () {
                getRankingMaps();
            });
        }

        /**
         * Mit dieser funktion kann ein ActionSuggestionScore verändert werden
         * @param answer_id
         * @param index Die Plazierung der Handlungsempfehlung in der Tabelle
         * @param ranking Das Ranking Objekt in der Tabelle
         */
        $scope.setActionSuggestionScore = function (answer_id, index, ranking) {
            var distribution = new answerToActionSuggestionScoreDistributionFactory();
            distribution.distribution_id = ranking.distribution_id;
            distribution.action_suggestion_id = $scope.testActionSuggestions[index].action_suggestion_id;
            distribution.answer_id = answer_id;
            distribution.score = ranking.score;
            distribution.$update(function () {
                getRankingMaps();
            });
        }

        /**
         * Mit dieser Funktion wird ein Fragelauf beendet.
         *
         * Danach werden die Resultate abgeholt
         */
        var finish = function () {
            $scope.finished = true;
            $scope.runResult = loadRunResults();
        }

        $scope.radio = false;


        /**
         * Mit dieser Methode wird eine Antwort abgegeben und dem Server geschickt
         * Wenn dies fertig ist wird die Frage in einer Liste mit alten Fragen hinzugefügt
         * Zudem wird dann die Nächste Frage abgeholt
         *
         * @param question Die beantwortete Frage
         * @param answerId Die Id der Antwort die abgegeben wurde
         * @param isAnswerYes Ob die Antwort Ja war. (Wird gebraucht um die Antwort später richtig anzuzeigen)
         */
        $scope.setAnswer = function (question, answerId, isAnswerYes) {
            question.isAnswerYes = isAnswerYes;
            question.answer_id = answerId;
            $scope.oldQuestions.push(question);

            var query = {
                answer: {answer_id: answerId},
                question: question
            };
            var put = $http.put("/run/user/" + $scope.userId, $filter('json')(query));
            $scope.question = null;
            put.then(function (result) {
                $scope.question = loadQuestion();
            });

            $scope.radio = false;
        }

        /**
         * Mit dieser Funktion wird ein Fragelauf beendet
         */
        $scope.finish = function () {
            finish();
        }

        /**
         * Mit dieser Funktion wird ein beendetes Fragelauf wieder neu gestartet.
         * Dabei muss eine nächste Frage geholt werden.
         */
        $scope.continue = function () {
            $scope.question = loadQuestion();
            $scope.finished = false;
        }
    }

});