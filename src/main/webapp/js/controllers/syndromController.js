angular.module('pocketDocApp').controller('syndromController', function($scope, $http,$q , syndromFactory ,diagnosisFactory, ngTableParams, actionSuggestionFactory,questionFactory, diagnosisDescriptionFactory,syndromToActionSuggestionScoreDistributionFactory, perfectDiagnosisFactory) {
    //Default Initialisierung von benutzen Variablen
    $scope.diagnoses = [];
    $scope.syndroms = [];
    $scope.actionSuggestions = [];
    $scope.syndrom="";
    $scope.symptomsLoaded = false;
    $scope.actionSuggestionsLoaded = false;
    $scope.syndromsLoaded = false;
    $scope.questions = [];
    $scope.allScoreDistributions = [];

    //Hinzufügen eines neuen Syndroms
    $scope.addSyndrom = function(){
        newSyndrom = new syndromFactory();
        //POST mit neuem Syndrom
        newSyndrom.$save(function(){
            //Das neue Syndrom den bereits vorhandenen Syndromen anfügen
            $scope.syndroms.push(angular.copy(newSyndrom));
            /**
             * Das neu Hinzugefügte Syndrom suchen und als momentan angezeigtes Syndrom setzten
             * Dies ist nötig weil newSyndrom nach dieser Methode nicht mehr definiert ist.
             */
            $scope.syndroms.forEach(function(syndrom){
                if(syndrom.syndrom_id === newSyndrom.syndrom_id){
                    $scope.setSyndrom(syndrom);
                }
            });
        });
    }

    /**
     * Löschen eines Syndroms
     * @param syndrom - zu löschendes Syndrom
     */
    $scope.removeSyndrom = function (syndrom){
        /**
         * Da wir über alle Syndrome iterieren und mittels ID vergleich das zu löschende Syndrom suchen
         * können wir nicht splice auf das array anwenden deshalb müssen wir ein neues erstellen und die nicht zu löschenden
         * Syndrome dort hinein verschieben.
         */
        newSyndroms = []
        $scope.syndroms.forEach(function(oldSyndrom){
            if(!angular.equals(oldSyndrom.syndrom_id,syndrom.syndrom_id)){
                newSyndroms.push(oldSyndrom);
            }
        });
        $scope.syndroms = angular.copy(newSyndroms);


        if($scope.syndroms.length !== 0){
            $scope.setSyndrom($scope.syndroms[0]);
        }

        /**
         * Da wir über alle Score Verteilungen iterieren und mittels id Vergleich die zu löschende Score Verteilung suchen
         * ist es nicht möglich die Verteilung mittels splice aus dem Array zu entfernen deshalb machen wir hier ein neues
         * Array mit den nicht zu löschenden Score Verteilungen
         */
        newScores = [];
        $scope.allScoreDistributions.forEach(function(scoreDistribution){
            if(!angular.equals(syndrom.syndrom_id,scoreDistribution.syndrom_id)){
                newScores.push(scoreDistribution); //cant splice from array we are iterating over thats why we make a new one
            }
        });

        $scope.allScoreDistributions= newScores;
        //Hier wird nun der DELETE REQUEST abgefeuert
        syndrom.$delete();
    }

    /**
     * Hier wird nun das anzuzeigende Syndrom gesetzt
     * 1. Wir suchen die Score Verteilungen zwischen aktuellem Syndrom und Handlungsempfehlugen
     * 2. Wir suchen die Relevanten Fragen für das aktuelle Syndrom
     * @param syndrom
     */
    $scope.setSyndrom = function(syndrom){
        $scope.syndrom = syndrom;
        getScoreDistributionsForCurrentSyndrom();
        getRelevantQuestionsForSyndrom();
    }

    /**
     * Hier werden die zum syndrom gehörenden Fragen ermittelt zuerst müssen die bisherig gesetzten relevanten Fragen zurückgesetzt
     * werden damit nicht die relevanten Fragen des vorher ausgewählten Syndroms angezeigt werden.
     */
    function getRelevantQuestionsForSyndrom(){
        $scope.questions.forEach(function(question){
            question.isRelevantForSyndrom = false;
            if(angular.isDefined($scope.syndrom.symptoms)){
                $scope.syndrom.symptoms.forEach(function(symptom){
                    if(angular.equals(symptom.answer_id, question.answer_yes.answer_id)){
                        question.isRelevantForSyndrom = true;
                    }
                });
            }
        });
    }


    $scope.addToSyndrom = function(question) {
        removeTheseQuestions = [];
        /**
         * question.isRelevantForSyndrom liefert den falschen bool Wert
         * wenn true dann ist der momentane inhalt false
         * wenn false dann ist der momentane inhalt true
         */
        if (angular.isDefined($scope.syndrom.symptoms)) {
            if (question.isRelevantForSyndrom) { //wir müssen löschen
                $scope.syndrom.symptoms.forEach(function (symptom) {
                    if (symptom.answer_id === question.answer_yes.answer_id) {
                        removeTheseQuestions.push(question);
                    }
                });
            }
            if (!question.isRelevantForSyndrom) { //Existiert bereits? Wenn nein hinzufügen!
                add = true;
                $scope.syndrom.symptoms.forEach(function (symptom) {
                    if (symptom.answer_id === question.answer_yes) {
                        add = false;
                    }
                });
                if (add) {
                    $scope.syndrom.symptoms.push({answer_id: question.answer_yes.answer_id});
                }
            }
        }

        /**
         * Da wir über alle Symptome eines Symptoms iterieren und mittels id Vergleich die zu löschende Relevante Frage suchen
         * ist es nicht möglich die relevante Frage mittels splice aus dem Array zu entfernen deshalb machen wir hier ein neues
         * Array mit den nicht zu löschenden relevanten Fragen
         */
        newSymptoms = [];
        if (angular.isDefined($scope.syndrom.symptoms)) {
            $scope.syndrom.symptoms.forEach(function (symptom) {
                add = true;
                removeTheseQuestions.forEach(function (removeQuestion) {
                    if (removeQuestion.answer_yes.answer_id === symptom.answer_id) {
                        add = false;
                    }
                });
                if (add) { //Relevante Frage nur hinzufügen wenn nicht in den zu löschenden relevanten Fragen enthalten
                    newSymptoms.push(symptom);
                }
            });
        }
        $scope.syndrom.symptoms = angular.copy(newSymptoms);
        //Hier wird nun der UPDATE REQUEST abgefeuert
        $scope.syndrom.$update();
    }

    /**
     * Es soll eine neue Score Verteilung zwischen syndrom und Handlungsempfehlung erstellt werden
     * @param action_suggestion - ausgewählte Handlungsempfehlung
     */
    $scope.addScoreToActionSuggestionScoreDistributions= function(action_suggestion){
        distribution = new syndromToActionSuggestionScoreDistributionFactory();
        distribution.syndrom_id=$scope.syndrom.syndrom_id;
        distribution.action_suggestion_id = action_suggestion.action_suggestion_id;
        distribution.score = 0;
        distribution.$save(function(){
            $scope.allScoreDistributions.push(angular.copy(distribution));
            getScoreDistributionsForCurrentSyndrom();
        });
    }

    /**
     * Für das aktuelle Syndrom sollen die dazugehörigen Handlungsempfehlung Score Verteilungen ermittelt werden.
     * 1. Alle Score Verteilungen zurücksetzten, damit nicht noch Score Verteilungen für das zuvor ausgewählte Syndrom angezeigt werden
     * 2. Für Alle Handlungsempfehlung Score Verteilungen alle Handlungsempfehlungen durchackern dabei diese herausfiltern welche die selbe syndrom_id wie das aktuelle
     *    Syndrom haben. Und dann die Verteilung der Handlungsempfehlung zuweisen.
     */
    function getScoreDistributionsForCurrentSyndrom(){
        $scope.actionSuggestions.forEach(function(action_suggestion){
            action_suggestion.distribution = undefined
        });
        $scope.allScoreDistributions.forEach(function(scoreDistribution){
            if(scoreDistribution.syndrom_id === $scope.syndrom.syndrom_id){
                $scope.actionSuggestions.forEach(function(actionSuggestion){
                    if(actionSuggestion.action_suggestion_id===scoreDistribution.action_suggestion_id){
                        actionSuggestion.distribution=angular.copy(scoreDistribution);
                    }
                });
            }
        });
    }

    /**
     * Falls User nicht eingeloggt ist sollen die Daten nicht vom server geladen werden
     */
    if($scope.user.isLogged) {
        /**
         * Handlungsempfehlungen vom Server laden
         */
        $scope.actionSuggestions = actionSuggestionFactory.query(function () {
            $scope.actionSuggestionsLoaded = true;
            /**
             * Handlungsempfehlung Score Verteilungen vom Server laden
             */
            $scope.allScoreDistributions = syndromToActionSuggestionScoreDistributionFactory.query(function () {
                getScoreDistributionsForCurrentSyndrom()
            });

        });

        /**
         * Fragen vom Server laden.
         * Hier könnten diese herausgefiltert werden, welche nicht ein Symptom sind wenn nötig z.B. Sind sie männlich?
         */
        $scope.questions = questionFactory.query(function () {
            newQuestions = [];
            $scope.questions.forEach(function (question) {
                    newQuestions.push(question);
            });
            $scope.questions = angular.copy(newQuestions);

            $scope.symptomsLoaded = true;
        });

        /**
         * Syndrome vom Server laden
         */
        $scope.syndroms = syndromFactory.query(function () {
            if ($scope.syndroms.length > 0) {
                $scope.syndrom = $scope.syndroms[0];
            }
            $scope.syndromsLoaded = true;
        });

        $q.all([$scope.questions.$promise, $scope.syndroms.$promise, $scope.actionSuggestions.$promise]).then(function () {
            getScoreDistributionsForCurrentSyndrom();
            getRelevantQuestionsForSyndrom();

        });
    }

    //Watchers
    /**
     * Aktuelles Syndrom beobachten sobald etwas geändert wird dem Server mitteilen
     */
    $scope.$watch('syndrom', function(newValue, oldValue){
        if(angular.isDefined(newValue)){
            if(newValue != oldValue && newValue.syndrom_id === oldValue.syndrom_id){
                syndromFactory.update(newValue);
            }
        }
    }, true);

    /**
     * Alle Handlungsempfehlungen beobachten und sobald etwas geändert wird dem Server mitteilen
     */
    $scope.$watch('actionSuggestions', function(newValue, oldValue){
        //Wir bekommen die alte Version der Handlungsempfehlungen und die neue Version
        if(angular.isDefined(newValue)){
            newValue.forEach(function(newActionSuggestion){
                oldValue.forEach(function(oldActionSuggestion){
                    //Nun müssen wir bei jeder Handlungsempfehlung die dazugehörige alte Handlungsempfehlung finden
                    if(newActionSuggestion.action_suggestion_id === oldActionSuggestion.action_suggestion_id){
                        //Nun noch überprüfen ob sich der Score geändert hat
                        if(angular.isDefined(newActionSuggestion.distribution) && angular.isDefined(oldActionSuggestion.distribution) && !angular.equals(parseInt(newActionSuggestion.distribution.score,10), parseInt(oldActionSuggestion.distribution.score,10)) && newActionSuggestion.distribution.action_suggestion_id === oldActionSuggestion.distribution.action_suggestion_id){
                            if(angular.isDefined(newActionSuggestion.distribution.distribution_id) && !angular.equals(newActionSuggestion.distribution.score,"")  && newActionSuggestion.distribution.score!==null){
                                newActionSuggestion.distribution.$update();
                                $scope.allScoreDistributions.forEach(function (scoreDistribution) {
                                    if (scoreDistribution.distribution_id === newActionSuggestion.distribution.distribution_id) {
                                        scoreDistribution.score = newActionSuggestion.distribution.score;
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