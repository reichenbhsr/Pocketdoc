angular.module('pocketDocApp').controller('questionController', function($scope, $http,$q , diagnosisFactory, ngTableParams,actionSuggestionFactory,answerToActionSuggestionScoreDistributionFactory, questionFactory, questionDescriptionFactory,answerToDiagnosisScoreDistributionFactory, perfectDiagnosisFactory) {
    //Default Initialisierung von benutzen Variablen
    $scope.questions = [];
    $scope.question="";
    $scope.dependsYes;
    $scope.dependsOnQuestion;
    $scope.question.german_description="";
    $scope.questionsLoaded = false;
    $scope.diagnosesLoaded = false;
    $scope.actionSuggestionsLoaded = false;
    $scope.allDiagnosisScoreDistributions = [];
    $scope.allActionSuggestionScoreDistributions = [];
    $scope.diagnoses = [];
    $scope.actionSuggestions = [];

    //Hinzufügen einer neuen Frage
    $scope.addQuestion = function(){
        newQuestion = new questionFactory();
        //POST mit neuer Frage
        newQuestion.$save(function(){
            //Die neue Frage den bereits vorhandenen Fragen anfügen
            $scope.questions.push(angular.copy(newQuestion));
            /**
             * Die neu Hinzugefügte Frage suchen und als momentan angezeigte Frage setzten
             * Dies ist nötig weil newQuestion nach dieser Methode nicht mehr definiert ist.
             */
            $scope.questions.forEach(function(question){
                if(question.question_id === newQuestion.question_id){
                    $scope.setQuestion(question);
                }
            });
        });
    }

    /**
     * Löschen einer Frage
     * @param question - zu löschende Frage
     */
    $scope.removeQuestion = function (question){
        /**
         * Da wir über alle Frage iterieren und mittels ID vergleich die zu löschende Frage suchen
         * können wir nicht splice auf das array anwenden deshalb müssen wir ein neues erstellen und die nicht zu löschenden
         * Fragen dort hinein verschieben.
         */
        newQuestions = []
        $scope.questions.forEach(function(oldQuestion){
            if(!angular.equals(oldQuestion.question_id,question.question_id)){
                newQuestions.push(oldQuestion);
            }
        });
        $scope.questions = angular.copy(newQuestions);


        if($scope.questions.length !== 0){
            $scope.setQuestion($scope.questions[0]);
        }

        /**
         * Da wir über alle Score Verteilungen iterieren und mittels id Vergleich die zu löschende Score Verteilung suchen
         * ist es nicht möglich die Verteilung mittels splice aus dem Array zu entfernen deshalb machen wir hier ein neues
         * Array mit den nicht zu löschenden Score Verteilungen
         */
        newScores = [];
        $scope.allDiagnosisScoreDistributions.forEach(function(scoreDistribution){
            if(!angular.equals(question.question_id,scoreDistribution.question_id)){
                newScores.push(scoreDistribution); //cant splice from array we are iterating over thats why we make a new one
            }
        });

        $scope.allDiagnosisScoreDistributions= newScores;
        //Hier wird nun der DELETE REQUEST abgefeuert.
        question.$delete();
    }

    /**
     * Es wird ausgewählt auf welche Frage die aktuelle Frage eine Abhängigkeit hat
     * @param newQuestion - die aktuelle Frage soll abhängig von dieser Frage sein
     */
    $scope.setQuestionForDependency = function (newQuestion){
        $scope.dependsYes = undefined; //reset von Radio button
        $scope.dependsOnQuestion = newQuestion;
        if(angular.isDefined($scope.question) && angular.isDefined($scope.question.dependence) && angular.isDefined($scope.dependsOnQuestion)){
            if($scope.question.dependence.answer_id === $scope.dependsOnQuestion.answer_yes.answer_id){
                $scope.dependsYes = true;
            }
            if($scope.question.dependence.answer_id === $scope.dependsOnQuestion.answer_no.answer_id){
                $scope.dependsYes = false;
            }
        }
    }

    /**
     * Hier wird die Abhängigkeit dann wirklich gesetzt damit dies funktioniert muss gewährleistet sein dass
     * die $scope.dependsOnQuestion gesetzt ist.
     * @param answerYes - Radio Button Value
     */
    $scope.setAnswerForDependency = function (answerYes){
        if(angular.isDefined($scope.dependsOnQuestion)){
            if(answerYes){
                $scope.question.dependence = $scope.dependsOnQuestion.answer_yes;
                $scope.dependsOnQuestion.answer_yes.has_dependency=true;
            }
            if(!answerYes){
                $scope.question.dependence = $scope.dependsOnQuestion.answer_no;
                $scope.dependsOnQuestion.answer_no.has_dependency=true;
            }
            $scope.question.$update();
            $scope.dependsYes = answerYes;
        }
    }

    $scope.setForceDependentAsking = function (){ // RE
        $scope.question.force_dependent_asking = !$scope.question.force_dependent_asking;
        console.log($scope.question.force_dependent_asking);
        $scope.question.$update();
    }

    /**
     * Auf einen Knopfklick soll die Abhängigkeit gelöscht werden
     * 1. Überprüfen ob die aktuelle Frage auf die Ja oder Nein Antwort der $scope.dependsOnQuestion verlinkt, dann den Bool
     *    has_dependency auf false setzten dieser wird gebraucht um den Radio Button zu deaktivieren falls es bereits eine Abhängigkeit
     *    darauf gibt.
     * 2. Zurücksetzten der durch die Abhängigkeit verwendeten Variablen
     */
    $scope.resetDependency = function(){
        if(angular.isDefined($scope.question.dependence)){
            if($scope.question.dependence.answer_id =  $scope.dependsOnQuestion.answer_yes.answer_id){
                $scope.dependsOnQuestion.answer_yes.has_dependency=false;
            }
            if($scope.question.dependence.answer_id =  $scope.dependsOnQuestion.answer_no.answer_id) {
                $scope.dependsOnQuestion.answer_no.has_dependency=false;
            }
        }
        $scope.dependsOnQuestion=undefined;
        $scope.dependsYes=undefined;
        $scope.question.dependence = undefined;
        $scope.question.$update();
    }

    /**
     * Diese Methode hilft es herauszufinden ob die aktuelle Frage eine Abhängigkeit hat und auf welche Antwort welcher Frage
     * 1. Zurücksetzen der durch die Abhängigkeit verwendeten Variablen, um sicherzustellen das nicht die Abhängigkeit der vorher
     *    dargestellten Frage noch angezeigt wird.
     * 2. Dannach muss durch alle Fragen iteriert werden und falls die dependence.answer_id der momentan iterierten Ja bzw nein answer_id
     *    entspricht wird die $scope.depensOnQuestion gesetzt.
     */
    function findDependency(){
        $scope.dependsOnQuestion = undefined;
        $scope.dependsYes = undefined;
        if(!angular.isDefined($scope.questions) || !angular.isDefined($scope.question) || !angular.isDefined($scope.question.dependence)){
            return;
        }
        $scope.questions.forEach(function(question){
            if($scope.question.dependence.answer_id === question.answer_yes.answer_id){
               $scope.dependsOnQuestion = question;
               $scope.dependsYes = true;
            }
            if($scope.question.dependence.answer_id === question.answer_no.answer_id){
                $scope.dependsOnQuestion = question;
                $scope.dependsYes = false;
            }
        });
    }

    /**
     * Hier wird die anzuzeigende Frage gesetzt
     * 1. Wir suchen wieder die Deutsche Beschreibung
     * 2. Wir suchen die Score Verteilungen zwischen den Antworten Ja, Nein der aktuellen Frage und Diagnosen
     * 3. Wir suchen die Score Verteilungen zwischen den Antworten Ja, Nein der aktuellen Frage und Handlungsempfehlungen
     * 4. Abhängigkeit auf andere Frage prüfen
     * @param question - anzuzeigende Frage
     */
    $scope.setQuestion = function(question){
        $scope.question = question;
        findGermanDescription();
        getDiagnosisScoreDistributionsForCurrentQuestion();
        getActionSuggestionScoreDistributionsForCurrentQuestion();
        findDependency();
    }

    //Die Deutsche Beschreibung soll immer angezeigt werden diese muss aber zuerst in den Beschreibungen gefunden werden
    function findGermanDescription(){
        if(angular.isDefined($scope.question.descriptions)){
            $scope.question.descriptions.forEach(function(description) {
                if(angular.equals(description.language_name, "Deutsch")){
                    $scope.question.german_description = description;
                }
            });
        }
    }

    /**
     * Es soll eine neue Score Verteilung zwischen answer und Diagnosen geben
     * 1. überprüfen handelt es sich bei der Antwort um eine Ja oder um eine Nein Antwort
     * 2. Neue Score Verteilung erstellen dabei wird dem diagnosis Objekt auf Grund von
     *    Punkt 1 entweder ein distribution_yes oder distribution_no hinzugefügt
     * @param diagnosis - Diagnose welche ausgewählt wurde
     * @param answer - Antwort welche ausgewählt wurde
     */
    $scope.addScoreToDiagnosisScoreDistributions = function(diagnosis, answer){
        if(angular.equals(answer.answer_id,$scope.question.answer_yes.answer_id)){
            diagnosis.distribution_yes = new answerToDiagnosisScoreDistributionFactory();
            diagnosis.distribution_yes.diagnosis_id=diagnosis.diagnosis_id;
            diagnosis.distribution_yes.answer_id = answer.answer_id;
            diagnosis.distribution_yes.score = 0;
            diagnosis.distribution_yes.$save(function(){
                $scope.allDiagnosisScoreDistributions.push(angular.copy(diagnosis.distribution_yes));
                getDiagnosisScoreDistributionsForCurrentQuestion();
                getActionSuggestionScoreDistributionsForCurrentQuestion();
            });
        } else {
            diagnosis.distribution_no = new answerToDiagnosisScoreDistributionFactory();
            diagnosis.distribution_no.diagnosis_id=diagnosis.diagnosis_id;
            diagnosis.distribution_no.answer_id = answer.answer_id;
            diagnosis.distribution_no.score = 0;
            diagnosis.distribution_no.$save(function(){
                $scope.allDiagnosisScoreDistributions.push(angular.copy(diagnosis.distribution_no));
                getDiagnosisScoreDistributionsForCurrentQuestion();
                getActionSuggestionScoreDistributionsForCurrentQuestion();
            });
        }
    }

    /**
     * Es soll eine neue Score Verteilung zwischen answer und Handlungsempfehlungen geben
     * 1. überprüfen handelt es sich bei der Antwort um eine Ja oder um eine Nein Antwort
     * 2. Neue Score Verteilung erstellen dabei wird dem actionSuggestion Objekt auf Grund von
     *    Punkt 1 entweder ein distribution_yes oder distribution_no hinzugefügt
     * @param actionSuggestion - Handlungsempfehlung welche ausgewählt wurde
     * @param answer - Antwort welche ausgewählt wurde
     */
    $scope.addScoreToActionSuggestionScoreDistributions = function(actionSuggestion, answer){
        if(angular.equals(answer.answer_id,$scope.question.answer_yes.answer_id)){
            actionSuggestion.distribution_yes = new answerToActionSuggestionScoreDistributionFactory();
            actionSuggestion.distribution_yes.action_suggestion_id=actionSuggestion.action_suggestion_id;
            actionSuggestion.distribution_yes.answer_id = answer.answer_id;
            actionSuggestion.distribution_yes.score = 0;
            actionSuggestion.distribution_yes.$save(function(){
                $scope.allActionSuggestionScoreDistributions.push(angular.copy(actionSuggestion.distribution_yes));
                getDiagnosisScoreDistributionsForCurrentQuestion();
                getActionSuggestionScoreDistributionsForCurrentQuestion();
            });
        } else {
            actionSuggestion.distribution_no = new answerToActionSuggestionScoreDistributionFactory();
            actionSuggestion.distribution_no.action_suggestion_id=actionSuggestion.action_suggestion_id;
            actionSuggestion.distribution_no.answer_id = answer.answer_id;
            actionSuggestion.distribution_no.score = 0;
            actionSuggestion.distribution_no.$save(function(){
                $scope.allActionSuggestionScoreDistributions.push(angular.copy(actionSuggestion.distribution_no));
                getDiagnosisScoreDistributionsForCurrentQuestion();
                getActionSuggestionScoreDistributionsForCurrentQuestion();
            });
        }
    }

    /**
     * Frage in der angegebenen Fremdsprache suchen und anzeigen
     * @param language - Sprache in der die anzuzeigende Beschreibung sein soll
     */
    $scope.setLanguage = function(language){
        $scope.question.descriptions.forEach(function(description){
            if(angular.equals(description.language_name, language)){
                $scope.question.currentDescription = description;
            }
        });
    }

    /**
     * Für Die Aktuelle Frage sollen die dazugehörigen Answer Score Verteilungen ermittelt werden, welche die Diagnosen betreffen
     * 1. Alle Score Verteilungen, welche die Diagnosen betreffen zurücksetzten, damit nicht noch Score Verteilungen für die zuvor ausgewählte Frage angezeigt werden
     * 2. Für Alle Diagnosen alle Diagnosen Score Verteilungen durchackern dabei diese herausfiltern welche die selbe diagnosis_id wie die diagnose der äusseren Iteration haben
     *    Und dann zu Ja oder Nein Verteilung hinzufügen.
     */
    function getDiagnosisScoreDistributionsForCurrentQuestion(){
        $scope.diagnoses.forEach(function(diagnosis){
            diagnosis.distribution_no = undefined
            diagnosis.distribution_yes = undefined;
        });
        $scope.allDiagnosisScoreDistributions.forEach(function(scoreDistribution){
            $scope.diagnoses.forEach(function(diagnosis){
                if(angular.equals(scoreDistribution.diagnosis_id, diagnosis.diagnosis_id)){
                    if($scope.question.answer_yes.answer_id===scoreDistribution.answer_id){
                        diagnosis.distribution_yes=angular.copy(scoreDistribution);
                    }
                    if($scope.question.answer_no.answer_id===scoreDistribution.answer_id){
                        diagnosis.distribution_no=angular.copy(scoreDistribution);
                    }
                }
            });
        });
    }

    /**
     * Für Die Aktuelle Frage sollen die dazugehörigen Answer Score Verteilungen ermittelt werden, welche die Handlungsempfehlungen betreffen
     * 1. Alle Score Verteilungen, welche die Handlungsempfehlungen betreffen zurücksetzten, damit nicht noch Score Verteilungen für die zuvor ausgewählte Frage angezeigt werden
     * 2. Für alle Diagnosen Score Verteilungen alle Handlungsempfehlungen durchackern dabei diese herausfiltern welche die selbe action_suggestion_id wie die score Verteilung der äusseren Iteration haben
     *    Und dann zu Ja oder Nein Verteilung hinzufügen.
     */
    function getActionSuggestionScoreDistributionsForCurrentQuestion(){
        $scope.actionSuggestions.forEach(function(actionSuggestion){
            actionSuggestion.distribution_no = undefined
            actionSuggestion.distribution_yes = undefined;
        });
        $scope.allActionSuggestionScoreDistributions.forEach(function(scoreDistribution){
            $scope.actionSuggestions.forEach(function(actionSuggestion){
                if(angular.equals(scoreDistribution.action_suggestion_id, actionSuggestion.action_suggestion_id)){
                    if($scope.question.answer_yes.answer_id===scoreDistribution.answer_id){
                        actionSuggestion.distribution_yes=angular.copy(scoreDistribution);
                    }
                    if($scope.question.answer_no.answer_id===scoreDistribution.answer_id){
                        actionSuggestion.distribution_no=angular.copy(scoreDistribution);
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
         * Diagnosen vom Server laden
         */
        $scope.diagnoses = diagnosisFactory.query(function () {
            if ($scope.diagnoses.length > 0) {
                $scope.diagnosis = $scope.diagnoses[0];
                findGermanDescription();
            }
            $scope.diagnosesLoaded = true;
            /**
             * Diagnose Score Verteilungen vom Server laden
             */
            $scope.allDiagnosisScoreDistributions = answerToDiagnosisScoreDistributionFactory.query(function () {
            });
        });

        /**
         * Fragen vom Server laden
         */
        $scope.questions = questionFactory.query(function () {
            if ($scope.questions.length > 0) {
                $scope.question = $scope.questions[0];
                findGermanDescription();
            }
            $scope.questionsLoaded = true;
        });

        /**
         * Handlungsempfehlungen vom Server laden
         */
        $scope.actionSuggestions = actionSuggestionFactory.query(function () {
            if ($scope.actionSuggestions.length > 0) {
            }
            $scope.actionSuggestionsLoaded = true;
            /**
             * Handlungsempfehlung Score Verteilungen vom Server laden
             */
            $scope.allActionSuggestionScoreDistributions = answerToActionSuggestionScoreDistributionFactory.query(function () {
            });
        });

        $q.all([$scope.questions.$promise, $scope.diagnoses.$promise]).then(function () {
            getDiagnosisScoreDistributionsForCurrentQuestion();
            findDependency();
            getActionSuggestionScoreDistributionsForCurrentQuestion();
        });
    }

    //Watchers
    /**
     * Aktuelle Frage beobachten sobald etwas geändert wird dem Server mitteilen
     */
    $scope.$watch('question', function(newValue, oldValue){
        if(angular.isDefined(newValue)){
            if(newValue != oldValue && newValue.question_id === oldValue.question_id){
                questionFactory.update(newValue);
                newValue.descriptions.forEach(function(description){
                    questionDescriptionFactory.update(description);
                });
            }
        }
    }, true);

    /**
     * Alle Fragen beobachten und sobald etwas geändert wird dem Server mitteilen
     */
    $scope.$watch('diagnoses', function(newValue, oldValue){
        //Wir bekommen die alte Version der Diagnosen und die neue Version
        if(angular.isDefined(newValue)){
            newValue.forEach(function(newDiagnosis){
                oldValue.forEach(function(oldDiagnosis){
                    //Nun müssen wir bei jeder Diagnose die dazugehörige alte Diagnose finden
                    if(newDiagnosis.diagnosis_id === oldDiagnosis.diagnosis_id){
                        //Nun noch überprüfen ob sich der Score geändert hat
                        if(angular.isDefined(newDiagnosis.distribution_yes) && angular.isDefined(oldDiagnosis.distribution_yes) && !angular.equals(parseInt(newDiagnosis.distribution_yes.score,10), parseInt(oldDiagnosis.distribution_yes.score,10)) && newDiagnosis.distribution_yes.distribution_id === oldDiagnosis.distribution_yes.distribution_id){
                            if(angular.isDefined(newDiagnosis.distribution_yes.distribution_id) && !angular.equals(newDiagnosis.distribution_yes.score,"") && newDiagnosis.distribution_yes.score!==null){
                                newDiagnosis.distribution_yes.$update();

                                $scope.allDiagnosisScoreDistributions.forEach(function (scoreDistribution) {
                                    if (scoreDistribution.distribution_id === newDiagnosis.distribution_yes.distribution_id) {
                                        scoreDistribution.score = newDiagnosis.distribution_yes.score;
                                    }
                                });
                            }
                        }
                        //Nun noch überprüfen ob sich der Score geändert hat
                        if(angular.isDefined(newDiagnosis.distribution_no) && angular.isDefined(oldDiagnosis.distribution_no) && !angular.equals(parseInt(newDiagnosis.distribution_no,10), parseInt(oldDiagnosis.distribution_no.score,10)) && newDiagnosis.distribution_no.distribution_id === oldDiagnosis.distribution_no.distribution_id){
                            if(angular.isDefined(newDiagnosis.distribution_no.distribution_id) && !angular.equals(newDiagnosis.distribution_no.score,"") && newDiagnosis.distribution_no.score!==null){
                                newDiagnosis.distribution_no.$update();

                                $scope.allDiagnosisScoreDistributions.forEach(function (scoreDistribution) {
                                    if (scoreDistribution.distribution_id === newDiagnosis.distribution_no.distribution_id) {
                                        scoreDistribution.score= newDiagnosis.distribution_no.score;
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
     * Alle Handlungsempfehlungen beobachten und sobald etwas geändert wird dem server mitteilen
     */
    $scope.$watch('actionSuggestions', function(newValue, oldValue){
        //Wir bekommen die alte Version der Hanldungsempfehlungen und die neue Version
        if(angular.isDefined(newValue)){
            newValue.forEach(function(newActionSuggestion){
                oldValue.forEach(function(oldActionSuggestion){
                    //Nun müssen wir bei jeder Hanldungsempfehlung die dazugehörige alte Handlungsempfehlung finden
                    if(newActionSuggestion.action_suggestion_id === oldActionSuggestion.action_suggestion_id){
                        //Nun noch überprüfen ob sich der Score geändert hat
                        if(angular.isDefined(newActionSuggestion.distribution_yes) && angular.isDefined(oldActionSuggestion.distribution_yes) && !angular.equals(parseInt(newActionSuggestion.distribution_yes.score,10), parseInt(oldActionSuggestion.distribution_yes.score,10)) && newActionSuggestion.distribution_yes.distribution_id === oldActionSuggestion.distribution_yes.distribution_id){
                            if(angular.isDefined(newActionSuggestion.distribution_yes.distribution_id) && !angular.equals(newActionSuggestion.distribution_yes.score,"") && newActionSuggestion.distribution_yes.score!==null){
                                newActionSuggestion.distribution_yes.$update();

                                $scope.allActionSuggestionScoreDistributions.forEach(function (scoreDistribution) {
                                    if (scoreDistribution.distribution_id === newActionSuggestion.distribution_yes.distribution_id) {
                                        scoreDistribution.score = newActionSuggestion.distribution_yes.score;
                                    }
                                });
                            }
                        }
                        //Nun noch überprüfen ob sich der Score geändert hat
                        if(angular.isDefined(newActionSuggestion.distribution_no) && angular.isDefined(oldActionSuggestion.distribution_no) && !angular.equals(parseInt(newActionSuggestion.distribution_no.score,10), parseInt(oldActionSuggestion.distribution_no.score,10)) && newActionSuggestion.distribution_no.distribution_id === oldActionSuggestion.distribution_no.distribution_id){
                            if(angular.isDefined(newActionSuggestion.distribution_no.distribution_id) && !angular.equals(newActionSuggestion.distribution_no.score,"") && newActionSuggestion.distribution_no.score!==null){
                                newActionSuggestion.distribution_no.$update();

                                $scope.allActionSuggestionScoreDistributions.forEach(function (scoreDistribution) {
                                    if (scoreDistribution.distribution_id === newActionSuggestion.distribution_no.distribution_id) {
                                        scoreDistribution.score= newActionSuggestion.distribution_no.score;
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