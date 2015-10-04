angular.module('pocketDocApp').controller('actionSuggestionController', function($scope, $http,$q , syndromFactory, ngTableParams,actionSuggestionFactory, actionSuggestionDescriptionFactory, answerToActionSuggestionScoreDistributionFactory, questionFactory, questionDescriptionFactory,syndromToActionSuggestionScoreDistributionFactory) {
    //Default Initialisierung von benutzen Variablen
    $scope.actionSuggestion  = {};
    $scope.actionSuggestion.german_description="";
    $scope.questionsLoaded = false;
    $scope.syndromsLoaded = false;
    $scope.actionSuggestionsLoaded = false;
    $scope.allAnswerScoreDistributions = [];
    $scope.allSyndromScoreDistributions = [];
    $scope.syndroms = [];
    $scope.actionSuggestions = [];
    $scope.questions = [];

    //Hinzufügen einer neuen Handlungsempfehlung
    $scope.addActionSuggestion = function(){
        newActionSuggestion = new actionSuggestionFactory();
        //POST mit neuer Handlungsempfehlung
        newActionSuggestion.$save(function(){
            //Die neue Handlungsempfehlung den bereits vorhandenen Handlungsempfehlungen anfügen
            $scope.actionSuggestions.push(angular.copy(newActionSuggestion));
            /**
             * Die neu Hinzugefügte Handlungsempfehlung suchen und als momentan angezeigte Handlungsempfehlung setzten
             * Dies ist nötig weil newActionSuggestion nach dieser Methode nicht mehr definiert ist.
             */
            $scope.actionSuggestions.forEach(function(actionSuggestion){
                if(actionSuggestion.action_suggestion_id === newActionSuggestion.action_suggestion_id){
                    $scope.setActionSuggestion(actionSuggestion);
                }
            });
        });
    }

    /**
     * Löschen einer Handlungsempfehlung
     * @param actionSuggestion - zu löschende Handlungsempfehlung
     */
    $scope.removeActionSuggestion = function (actionSuggestion){
        /**
         * Da wir über alle Handlungsempfehlungen iterieren und mittels ID vergleich die zu löschende Handlungsemmpfehlung suchen
         * können wir nicht splice auf das array anwenden deshalb müssen wir ein neues erstellen und die nicht zu löschenden
         * Handlungsempfehlungen dort hinein verschieben.
         */
        newActionSuggestions = []
        $scope.actionSuggestions.forEach(function(oldActionSuggestion){
            if(!angular.equals(oldActionSuggestion.action_suggestion_id,actionSuggestion.action_suggestion_id)){
                newActionSuggestions.push(oldActionSuggestion);
            }
        });
        $scope.actionSuggestions = angular.copy(newActionSuggestions);


        if($scope.actionSuggestions.length !== 0){
            $scope.setActionSuggestion($scope.actionSuggestions[0]);
        }

        /**
         * Da wir über alle Score Verteilungen iterieren und mittels id Vergleich die zu löschende Score Verteilung suchen
         * ist es nicht möglich die Verteilung mittels splice aus dem Array zu entfernen deshalb machen wir hier ein neues
         * Array mit den nicht zu löschenden Score Verteilungen
         */
        newAnswerScores = [];
        $scope.allAnswerScoreDistributions.forEach(function(scoreDistribution){
            if(!angular.equals(actionSuggestion.action_suggestion_id,scoreDistribution.action_suggestion_id)){
                newAnswerScores.push(scoreDistribution); //cant splice from array we are iterating over thats why we make a new one
            }
        });
        $scope.allAnswerScoreDistributions= newAnswerScores;

        /**
         * Da wir über alle Score Verteilungen iterieren und mittels id Vergleich die zu löschende Score Verteilung suchen
         * ist es nicht möglich die Verteilung mittels splice aus dem Array zu entfernen deshalb machen wir hier ein neues
         * Array mit den nicht zu löschenden Score Verteilungen
         */
        newSyndromScores = [];
        $scope.allSyndromScoreDistributions.forEach(function(scoreDistribution){
            if(!angular.equals(actionSuggestion.action_suggestion_id,scoreDistribution.action_suggestion_id)){
                newSyndromScores.push(scoreDistribution); //cant splice from array we are iterating over thats why we make a new one
            }
        });

        $scope.allSyndromScoreDistributions= newSyndromScores;
        //Hier wird nun der DELETE REQUEST abgefeuert.
        actionSuggestion.$delete();
    }

    /**
     * Hier wird die anzuzeigende Handlungsempfehlung gesetzt
     * 1. Wir suchen wieder die Deutsche Beschreibung
     * 2. Wir suchen die Score Verteilungen zwischen aktueller Handlungsempfehlung und Syndromen
     * 3. Wir suchen die Score Verteilungen zwischen aktueller Handlungsempfehlung und Antworten(Ja, Nein)
     * @param actionSuggestion - anzuzeigende Handlungsempfehlung
     */
    $scope.setActionSuggestion = function(actionSuggestion){
        $scope.actionSuggestion = actionSuggestion;
        findGermanDescription();
        getSyndromScoreDistributionsForCurrentActionSuggestion();
        getAnswerScoreDistributionsForCurrentActionSuggestion();
    }

    //Die Deutsche Beschreibung soll immer angezeigt werden diese muss aber zuerst in den Beschreibungen gefunden werden
    function findGermanDescription(){
        if(angular.isDefined($scope.actionSuggestion.descriptions)){
            //Iteration über alle Beschreibungen mittels angular.equals wird hier die Deutsche Beschreibung gesucht.
            $scope.actionSuggestion.descriptions.forEach(function(description) {
                if(angular.equals(description.language_name, "Deutsch")){
                    $scope.actionSuggestion.german_description = description;
                }
            });
        }
    }

    /**
     * Beschreibung in der angegebenen Fremdsprache suchen und anzeigen
     * @param language - Sprache in der die anzuzeigende Beschreibung sein soll
     */
    $scope.setLanguage = function(language){
        $scope.actionSuggestion.descriptions.forEach(function(description){
            if(angular.equals(description.language_name, language)){
                $scope.actionSuggestion.currentDescription = description;
            }
        });
    }

    /**
     * Es soll eine neue Score Verteilung zwischen answer und Handlungsempfehlung geben
     * 1. Überprüfen handelt es sich bei der Antwort um eine Ja oder um eine Nein Antwort
     * 2. Neue Score Verteilung erstellen dabei wird dem actionSuggestion Objekt auf Grund
     *    von Punkt 1 entweder ein distribution_yes oder distribution_no hinzugefügt.
     * @param answer - Antwort welche ausgewählt wurde
     * @param question - Zur Antwort gehörende Frage
     */
    $scope.addScoreToAnswerScoreDistributions = function(question, answer){
        if(angular.equals(answer.answer_id,question.answer_yes.answer_id)){
            $scope.actionSuggestion.distribution_yes = new answerToActionSuggestionScoreDistributionFactory();
            $scope.actionSuggestion.distribution_yes.action_suggestion_id=$scope.actionSuggestion.action_suggestion_id;
            $scope.actionSuggestion.distribution_yes.answer_id = answer.answer_id;
            $scope.actionSuggestion.distribution_yes.score = 0;
            $scope.actionSuggestion.distribution_yes.$save(function(){
                $scope.allAnswerScoreDistributions.push(angular.copy($scope.actionSuggestion.distribution_yes));
                    getAnswerScoreDistributionsForCurrentActionSuggestion();
            });
        } else {
            $scope.actionSuggestion.distribution_no = new answerToActionSuggestionScoreDistributionFactory();
            $scope.actionSuggestion.distribution_no.action_suggestion_id=$scope.actionSuggestion.action_suggestion_id;
            $scope.actionSuggestion.distribution_no.answer_id = answer.answer_id;
            $scope.actionSuggestion.distribution_no.score = 0;
            $scope.actionSuggestion.distribution_no.$save(function(){
                $scope.allAnswerScoreDistributions.push(angular.copy($scope.actionSuggestion.distribution_no));
                getAnswerScoreDistributionsForCurrentActionSuggestion();
            });
        }
    }

    /**
     * Es soll eine neue Score Verteilung zwischen Syndrom und Handlungsempfehlung erstellt werden
     * @param syndrom - syndrom welches ausgewählt wurde
     */
    $scope.addScoreToSyndromScoreDistribution = function(syndrom){
        syndrom.distribution = new syndromToActionSuggestionScoreDistributionFactory();
        syndrom.distribution.action_suggestion_id=$scope.actionSuggestion.action_suggestion_id;
        syndrom.distribution.syndrom_id = syndrom.syndrom_id;
        syndrom.distribution.score = 0;
        syndrom.distribution.$save(function(){
            $scope.allSyndromScoreDistributions.push(angular.copy(syndrom.distribution));
            getSyndromScoreDistributionsForCurrentActionSuggestion();
        });
    }

    /**
     * Für Die Aktuelle Hanldungsempfehlung sollen die dazugehörigen Answer Score Verteilungen ermittelt werden.
     * 1. Alle Score Verteilungen zurücksetzten, damit nicht noch Score Verteilungen für die zuvor ausgewählte Handlungsempfehlung angezeigt werden
     * 2. Für Alle Fragen alle Antwort Score Verteilungen durchackern dabei diese herausfiltern welche die selbe action_suggestion_id wie die aktuelle
     *    Handlungsempfehlung haben. Und dann zu Ja oder Nein Verteilung hinzufügen.
     */
    function getAnswerScoreDistributionsForCurrentActionSuggestion(){
        $scope.questions.forEach(function(question){
            question.distribution_no = undefined
            question.distribution_yes = undefined;
        });
        $scope.allAnswerScoreDistributions.forEach(function(scoreDistribution){
            $scope.questions.forEach(function(question){
                if(angular.equals(scoreDistribution.action_suggestion_id, $scope.actionSuggestion.action_suggestion_id)){
                    if(question.answer_yes.answer_id===scoreDistribution.answer_id){
                        question.distribution_yes=angular.copy(scoreDistribution);
                    }
                    if(question.answer_no.answer_id===scoreDistribution.answer_id){
                        question.distribution_no=angular.copy(scoreDistribution);
                    }
                }
            });
        });
    }

    /**
     * Für Die Aktuelle Hanldungsempfehlung sollen die dazugehörigen Syndrom Score Verteilungen ermittelt werden.
     * 1. Alle Score Verteilungen zurücksetzten, damit nicht noch Score Verteilungen für die zuvor ausgewählte Handlungsempfehlung angezeigt werden
     * 2. Für Alle Syndrom Score Verteilungen alle Syndrome durchackern dabei diese herausfiltern welche die selbe action_suggestion_id wie die aktuelle
     *    Handlungsempfehlung haben. Und dann die Verteilung dem syndrom zuweisen.
     */
    function getSyndromScoreDistributionsForCurrentActionSuggestion(){
        $scope.syndroms.forEach(function(syndrom){
            syndrom.distribution = undefined
        });
        $scope.allSyndromScoreDistributions.forEach(function(scoreDistribution){
            $scope.syndroms.forEach(function(syndrom){
                if(angular.equals(scoreDistribution.action_suggestion_id, $scope.actionSuggestion.action_suggestion_id)){
                    if(syndrom.syndrom_id===scoreDistribution.syndrom_id){
                        syndrom.distribution=angular.copy(scoreDistribution);
                    }
                }
            });
        });
    }

    /**
     * Falls User nicht eingeloggt ist sollen die Daten nicht vom server geladen werden
     */
    if($scope.user.isLogged) {
        /**
         * Syndrome vom Server laden
         */
        $scope.syndroms = syndromFactory.query(function () {
            if ($scope.syndroms.length > 0) {
            }
            $scope.syndromsLoaded = true;
            /**
             * Syndrom Score Verteilungen vom Server laden
             */
            $scope.allSyndromScoreDistributions = syndromToActionSuggestionScoreDistributionFactory.query(function () {
                getSyndromScoreDistributionsForCurrentActionSuggestion();
            });
        });

        /**
         * Fragen vom Server laden
         */
        $scope.questions = questionFactory.query(function () {
            if ($scope.questions.length > 0) {
            }
            $scope.questionsLoaded = true;
            /**
             * Antwort Score Verteilungen vom Server laden
             */
            $scope.allAnswerScoreDistributions = answerToActionSuggestionScoreDistributionFactory.query(function () {
                getAnswerScoreDistributionsForCurrentActionSuggestion();
            });
        });

        /**
         * Handlungsempfehlungen vom Server laden
         */
        $scope.actionSuggestions = actionSuggestionFactory.query(function () {
            if ($scope.actionSuggestions.length > 0) {
                $scope.actionSuggestion = $scope.actionSuggestions[0];
            }
            $scope.actionSuggestionsLoaded = true;
            $scope.allActionSuggestionScoreDistributions = answerToActionSuggestionScoreDistributionFactory.query(function () {
                findGermanDescription();
            });
        });

        $q.all([$scope.questions.$promise, $scope.actionSuggestions.$promise, $scope.syndroms.$promise]).then(function () {
            getSyndromScoreDistributionsForCurrentActionSuggestion();
            getAnswerScoreDistributionsForCurrentActionSuggestion();
        });
    }


    //Watchers
    /**
     * Aktuelle Handlungsempfehlung beobachten sobald etwas geändert wird dem Server mitteilen
     */
    $scope.$watch('actionSuggestion', function(newValue, oldValue){
        if(angular.isDefined(newValue)){
            if(newValue != oldValue && newValue.action_suggestion_id === oldValue.action_suggestion_id){
                actionSuggestionFactory.update(newValue);
                newValue.descriptions.forEach(function(description){
                    actionSuggestionDescriptionFactory.update(description);
                });
            }
        }
    }, true);

    /**
     * Alle Fragen beobachten und sobald etwas geändert wird dem Server mitteilen
     */
    $scope.$watch('questions', function(newValue, oldValue){
        //Wir bekommen die alte Version der Fragen und die neue Version
        if(angular.isDefined(newValue)){
            newValue.forEach(function(newQuestion){
                oldValue.forEach(function(oldQuestion){
                    //Nun müssen wir bei jeder Frage die dazugehörige alte Frage finden
                    if(newQuestion.question_id === oldQuestion.question_id){
                        //Nun noch überprüfen ob sich der Score geändert hat
                        if(angular.isDefined(newQuestion.distribution_yes) && angular.isDefined(oldQuestion.distribution_yes) && !angular.equals(parseInt(newQuestion.distribution_yes.score,10), parseInt(oldQuestion.distribution_yes.score,10)) && newQuestion.distribution_yes.distribution_id === oldQuestion.distribution_yes.distribution_id){
                            if(angular.isDefined(newQuestion.distribution_yes.distribution_id) && !angular.equals(newQuestion.distribution_yes.score,"") && newQuestion.distribution_yes.score!==null){
                                newQuestion.distribution_yes.$update();

                                $scope.allAnswerScoreDistributions.forEach(function (scoreDistribution) {
                                    if (scoreDistribution.distribution_id === newQuestion.distribution_yes.distribution_id) {
                                        scoreDistribution.score = newQuestion.distribution_yes.score;
                                    }
                                });
                            }
                        }
                        //Nun noch überprüfen ob sich der Score geändert hat
                        if(angular.isDefined(newQuestion.distribution_no) && angular.isDefined(oldQuestion.distribution_no) && !angular.equals(parseInt(newQuestion.distribution_no.score,10), parseInt(oldQuestion.distribution_no.score,10)) && newQuestion.distribution_no.distribution_id === oldQuestion.distribution_no.distribution_id){
                            if(angular.isDefined(newQuestion.distribution_no.distribution_id) && !angular.equals(newQuestion.distribution_no.score,"") && newQuestion.distribution_no.score!==null){
                                newQuestion.distribution_no.$update();

                                $scope.allAnswerScoreDistributions.forEach(function (scoreDistribution) {
                                    if (scoreDistribution.distribution_id === newQuestion.distribution_no.distribution_id) {
                                        scoreDistribution.score= newQuestion.distribution_no.score;
                                    }
                                });
                            }
                        }
                    }
                });
            });
        }
    }, true);


    /**
     * Alle Syndrome beobachten und sobald etwas geändert wird dem server mitteilen
     */
    $scope.$watch('syndroms', function(newValue, oldValue){
        //Wir bekommen die alte Version der Syndrome und die neue Version
        if(angular.isDefined(newValue)){
            newValue.forEach(function(newSyndrom){
                oldValue.forEach(function(oldSyndrom){
                    //Nun müssen wir bei jedem Syndrom das dazugehörige alte Syndrom finden
                    if(newSyndrom.syndrom_id === oldSyndrom.syndrom_id){
                        //Nun noch überprüfen ob sich der Score geändert hat
                        if(angular.isDefined(newSyndrom.distribution) && angular.isDefined(oldSyndrom.distribution) && !angular.equals(parseInt(newSyndrom.distribution.score,10), parseInt(oldSyndrom.distribution.score,10)) && newSyndrom.distribution.distribution_id === oldSyndrom.distribution.distribution_id){
                            if(angular.isDefined(newSyndrom.distribution.distribution_id) && !angular.equals(newSyndrom.distribution.score,"") && newSyndrom.distribution.score!==null){
                                newSyndrom.distribution.$update();

                                $scope.allSyndromScoreDistributions.forEach(function (scoreDistribution) {
                                    if (scoreDistribution.distribution_id === newSyndrom.distribution.distribution_id) {
                                        scoreDistribution.score = newSyndrom.distribution.score;
                                    }
                                });
                            }
                        }
                    }
                });
            });
        }
    }, true);
});