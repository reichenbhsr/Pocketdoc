angular.module('pocketDocApp').controller('diagnosisController', function($scope, $http,$q , diagnosisFactory, ngTableParams, $window, questionFactory, diagnosisDescriptionFactory, diagnosisDesignationFactory,answerToDiagnosisScoreDistributionFactory, perfectDiagnosisFactory) {
    //Default Initialisierung von benutzen Variablen
    $scope.diagnoses = [];
    $scope.diagnosis="";
    $scope.diagnosis.german_description="";
    $scope.diagnosis.german_designation="";
    $scope.diagnosesLoaded = false;
    $scope.showPerfectDiagnosisTest = false
    $scope.questions = [];
    $scope.allScoreDistributions = [];
    $scope.questionsLoaded = false;
    $scope.rankingLists = [];

    //Hinzufügen einer neuen Diagnose
    $scope.addDiagnosis = function(){
        newDiagnosis = new diagnosisFactory();
        //POST mit neuer Diagnose
        newDiagnosis.$save(function(){
            //Die neue Diagnose den bereits vorhandenen Diagnosen anfügen
            $scope.diagnoses.push(angular.copy(newDiagnosis));
            /**
             * Die neu hinzugefügte Diagnose suchen und als momentan angezeigte Diagnose setzen.
             * Dies ist nötig weil newDiagnosis nach dieser Methode nicht mehr definiert ist.
             */
            $scope.diagnoses.forEach(function(diagnosis){
                if(diagnosis.diagnosis_id === newDiagnosis.diagnosis_id){
                    $scope.setDiagnosis(diagnosis);
                }
            });
        });
    }


    /**
     * Löschen einer Diagnose
     * @param diagnosis - zu löschende Diagnose
     */
    $scope.removeDiagnosis = function (diagnosis){
        /**
         * Da wir über alle Diagnosen iterieren und mittels ID vergleich die zu löschende Diagnose suchen
         * können wir nicht splice auf das array anwenden deshalb müssen wir ein neues erstellen und die nicht zu löschenden
         * Diagnosen dort hinein verschieben.
         */
        newDiagnoses = []
        $scope.diagnoses.forEach(function(oldDiagnosis){
            if(!angular.equals(oldDiagnosis.diagnosis_id,diagnosis.diagnosis_id)){
                newDiagnoses.push(oldDiagnosis);
            }
        });
        $scope.diagnoses = angular.copy(newDiagnoses);


        if($scope.diagnoses.length !== 0){
            $scope.setDiagnosis($scope.diagnoses[0]);
        }

        //delete scores from allScores
        newScores = [];

        /**
         * Da wir über alle Score Verteilungen iterieren und mittels id Vergleich die zu löschende Score Verteilung suchen
         * ist es nicht möglich die Verteilung mittels splice aus dem Array zu entfernen deshalb machen wir hier ein neues
         * Array mit den nicht zu löschenden Score Verteilungen
         */
        $scope.allScoreDistributions.forEach(function(scoreDistribution){
            if(!angular.equals(diagnosis.diagnosis_id,scoreDistribution.diagnosis_id)){
                newScores.push(scoreDistribution);
            }
        });

        $scope.allScoreDistributions= newScores;
        //Hier wird nun der DELETE REQUEST abgefeuert.
        diagnosis.$delete();
    }

    /**
     * Hier wird die anzuzeigende Diagnose gesetzt
     * 1. Wir suchen wieder die Deutsche Beschreibung
     * 2. Wir suchen die Score Verteilungen zwischen aktueller Diagnose und Antworten (Ja, Nein)
     * 3. Wir suchen welche Antworten für die aktuelle Diagnose als Typisch markiert sind. (getPerfectDiagnosisForCurrentDiagnosis)
     * @param diagnosis - anzuzeigende Diagnose
     */
    $scope.setDiagnosis = function(diagnosis){
        $scope.diagnosis = diagnosis;
        $scope.perfectDiagnosisTest = undefined;
        findGermanDescription();
        findGermanDesignation();
        getScoreDistributionsForCurrentDiagnosis();
        getPerfectDiagnosisForCurrentDiagnosis();
        $scope.showPerfectDiagnosisTest = false;
    }

    /**
     *
     * @param answer - Die Antwort welche als typisch gilt für die aktuelle Diagnose
     * @param question - Die zur Antwort gehörende Frage
     */
    $scope.setPerfectDiagnosis = function(answer, negativAnswer){
        removeTheseDiagnoses = [];
        /**
         * $scope.perfect_diagnosis_yes bzw. $scope.perfect_diagnosis_no liefern den falschen bool Wert
         * wenn true dann ist der momentane inhalt false
         * wenn false dann ist der momentane inhalt true
         */

        /**
         * Handelt es sich um eine JA Antwort
         */

        // RE: Durchlauf für ja und nein Antworten vereinheitlicht
        if (angular.isDefined($scope.diagnosis.perfect_diagnosis)){
            if (answer.perfect_diagnosis){
                $scope.diagnosis.perfect_diagnosis.forEach(function(perfectDiagnosis){
                    if(perfectDiagnosis.answer.answer_id === answer.answer_id){
                        removeTheseDiagnoses.push(perfectDiagnosis);
                    }
                });
            }
            else{
                add = true;
                $scope.diagnosis.perfect_diagnosis.forEach(function(perfectDiagnosis){
                    if(perfectDiagnosis.answer.answer_id === answer.answer_id){
                        add = false;
                    }
                });
                if(add){
                    $scope.diagnosis.perfect_diagnosis.push({answer:{answer_id:answer.answer_id, has_dependency:answer.has_dependency}});
                }

                // RE: Gegenteilige Antwort (Bei Ja nein und umgekehrt) wird deaktiviert
                negativAnswer.perfect_diagnosis = false;
                $scope.diagnosis.perfect_diagnosis.forEach(function(perfectDiagnosis){
                    if(perfectDiagnosis.answer.answer_id === negativAnswer.answer_id){
                        removeTheseDiagnoses.push(perfectDiagnosis);
                    }
                });
            }
        }

        /**
         * Da wir über alle typischen Antworten einer Diagnose iterieren und mittels id Vergleich die zu löschende typische Antwort suchen
         * ist es nicht möglich die typische Antwort mittels splice aus dem Array zu entfernen deshalb machen wir hier ein neues
         * Array mit den nicht zu löschenden typischen Antworten
         */
        newPerfectDiagnosis = [];
        if(angular.isDefined($scope.diagnosis.perfect_diagnosis)){
            $scope.diagnosis.perfect_diagnosis.forEach(function(perfectDiagnosis){
                add = true;
                removeTheseDiagnoses.forEach(function(removeDiagnose){
                    if(removeDiagnose.answer.answer_id === perfectDiagnosis.answer.answer_id){
                        add = false;
                    }
                });
                if(add){ //Typische Antwort nur hinzufügen wenn nicht in den zu löschenden typischen Antworten vorhanden
                    newPerfectDiagnosis.push(perfectDiagnosis);
                }
            });
        }
        $scope.diagnosis.perfect_diagnosis = angular.copy(newPerfectDiagnosis);
        //Hier wird nun der UPDATE REQUEST abgefeuert.
        $scope.diagnosis.$update();
        getPerfectDiagnosisForCurrentDiagnosis();
    }

    //Die Deutsche Beschreibung soll immer angezeigt werden diese muss aber zuerst in den Beschreibungen gefunden werden
    function findGermanDescription(){
        if(angular.isDefined($scope.diagnosis.descriptions)){
            $scope.diagnosis.descriptions.forEach(function(description) {
                if(angular.equals(description.language_name, "Deutsch")){
                    $scope.diagnosis.german_description = description;
                }
            });
        }
    }

    // Die Deutsche Bezeichnung soll immer angezeigt werden, daher muss diese zuerst gesucht werden.
    function findGermanDesignation(){
        if(angular.isDefined($scope.diagnosis.designations)){
            $scope.diagnosis.designations.forEach(function(designation) {
                if(angular.equals(designation.language_name, "Deutsch")){
                    $scope.diagnosis.german_designation = designation;
                }
            });
        }
    }

    /**
     * typische Antworten für das aktuelle Diagnose ermittelen
     * dafür müssen die bisherig gesetzen typischen antworten zurückgesetzt werden damit nicht
     * die typischen Antworten für die vorhin ausgewählte Diagnose angezeigt werden.
     */
    function getPerfectDiagnosisForCurrentDiagnosis(){
        $scope.questions.forEach(function(question){
            question.answer_no.perfect_diagnosis = false; //reset
            question.answer_yes.perfect_diagnosis = false; //reset
            if(angular.isDefined($scope.diagnosis.descriptions)){
                $scope.diagnosis.perfect_diagnosis.forEach(function(perfectDiagnosis){
                    if(angular.equals(question.answer_yes.answer_id, perfectDiagnosis.answer.answer_id)){
                        question.answer_yes.perfect_diagnosis = true;
                    }
                    if(angular.equals(question.answer_no.answer_id, perfectDiagnosis.answer.answer_id)){
                        question.answer_no.perfect_diagnosis = true;
                    }
                });
            }
        });
    }

    /**
     * Es soll eine neue Score Verteilung zwischen aktueller Diagnose und Answer erstellt werden
     * 1. Überprüfen handelt es sich bei der Antwort um eine Ja oder um eine Nein Antwort
     * 2. Neue Score Verteilung erstellen dabei wird dem question Objekt auf Grund von Punkt 1
     *    entweder ein distribution_yes oder distribution_no hinzugefügt.
     * @param answer - Antwort welche ausgewählt wurde
     * @param question - dazugehörige Frage
     */
    $scope.addScoreToCurrentDiagnosis = function(question, answer){
        if(angular.equals(answer.answer_id,question.answer_yes.answer_id)){
            question.distribution_yes = new answerToDiagnosisScoreDistributionFactory();
            question.distribution_yes.diagnosis_id=$scope.diagnosis.diagnosis_id;
            question.distribution_yes.answer_id = answer.answer_id;
            question.distribution_yes.score = 0;
            question.distribution_yes.$save(function(){
                $scope.allScoreDistributions.push(angular.copy(question.distribution_yes));
                getScoreDistributionsForCurrentDiagnosis();
                getRankingMap();
            });
        } else {
            question.distribution_no = new answerToDiagnosisScoreDistributionFactory();
            question.distribution_no.diagnosis_id=$scope.diagnosis.diagnosis_id;
            question.distribution_no.answer_id = answer.answer_id;
            question.distribution_no.score = 0;
            question.distribution_no.$save(function(){
                $scope.allScoreDistributions.push(angular.copy(question.distribution_no));
                getScoreDistributionsForCurrentDiagnosis();
                getRankingMap();
            });
        }
    }

    /**
     * Es soll eine neue Score Verteilung zwischen der ausgewählten Diagnose und Answer erstellt werden
     * 1. Mittels index die diagnose aus dem $scope.perfectDiagnosisTest.perfect_diagnosis_ranking array holen
     * 2. Neue Score Verteilung erstellen.
     * @param answer - Antwort welche ausgewählt wurde
     * @param index - dazugehöriger index (für diagnose in $scope.perfectDiagnosisTest.perfect_diagnosis_ranking)
     */
    $scope.addScoreToSelectedDiagnosis = function(answer,index){
        distribution = new answerToDiagnosisScoreDistributionFactory();
        distribution.diagnosis_id=$scope.perfectDiagnosisTest.perfect_diagnosis_ranking[index].diagnosis_id;
        distribution.answer_id = answer.answer_id;
        distribution.score = 0;
        distribution.$save(function(){
            $scope.allScoreDistributions.push(angular.copy(distribution));
            getScoreDistributionsForCurrentDiagnosis();
            getRankingMap();
        });
    }

    /**
     * Beschreibung in der angegebenen Fremdsprache suchen und anzeigen
     * @param language - Sprache in der die anzuzeigende Beschreibung sein soll
     */
    $scope.setLanguage = function(language){
        $scope.diagnosis.descriptions.forEach(function(description){
            if(angular.equals(description.language_name, language)){
                $scope.diagnosis.currentDescription = description;
            }
        });
        $scope.diagnosis.designations.forEach(function(designation){
            if(angular.equals(designation.language_name, language)){
                $scope.diagnosis.currentDesignation = designation;
            }
        });
    }

    /**
     * Für Die Aktuelle Diagnose sollen die dazugehörigen Answer Score Verteilungen ermittelt werden.
     * 1. Alle Score Verteilungen zurücksetzten, damit nicht noch Score Verteilungen für die zuvor ausgewählte Diagnose angezeigt werden
     * 2. Für Alle Answer Score Verteilungen alle Fragen durchackern dabei diese herausfiltern welche die selbe diagnosis_id wie die aktuelle
     *    Diagnose haben. Und ann zu Ja oder Nein Verteilung hinzufügen.
     */
    function getScoreDistributionsForCurrentDiagnosis(){
        $scope.questions.forEach(function(question){
            question.distribution_no = undefined
            question.distribution_yes = undefined;
        });
        $scope.allScoreDistributions.forEach(function(scoreDistribution){
            if(scoreDistribution.diagnosis_id === $scope.diagnosis.diagnosis_id){
                $scope.questions.forEach(function(question){
                    if(question.answer_yes.answer_id===scoreDistribution.answer_id){
                        question.distribution_yes=angular.copy(scoreDistribution);
                    }
                    if(question.answer_no.answer_id===scoreDistribution.answer_id){
                        question.distribution_no=angular.copy(scoreDistribution);
                    }
                });
            }
        });
    }

    /**
     * Perfekte Diagnose Testen Dabei wird dem Server die id der zu testenden Diagnose mitgeteilt dieser berechnet
     * daraufhin das Ergebnis und antwortet mit einer Reihenfolge und einem resultierenden Total Score welche jede Diagnose
     * erzielte.
     */
    $scope.testPerfectDiagnosis = function(){
        $scope.perfectDiagnosisTest = new perfectDiagnosisFactory();
        $scope.perfectDiagnosisTest.diagnosis_id = $scope.diagnosis.diagnosis_id;
        $scope.perfectDiagnosisTest.$get(function(){
            getRankingMap()
            $scope.showPerfectDiagnosisTest  = true;
        });
    }

    /**
     * Perfekte Diagnose aller Diagnosen testen dabei öffnet sich ein neuer Tab und dem Benutzer wird eine Liste der Fehlgeschlagenen
     * Diagnosen angezeigt. Dies ist ein HOTFIX.
     */
    $scope.testAllPerfectDiagnoses = function(){
        $window.open('https://pocketdoc.herokuapp.com/perfectDiagnosis', '_blank');
    };

    /**
     * Legt eine Kopie der aktuellen Diagnose an.
     */
    $scope.copyDiagnosis = function(){
        diagnosisFactory.copy($scope.diagnosis, $scope.diagnosis, function(answer){
            //Die neue Diagnose den bereits vorhandenen Diagnosen anfügen
            $scope.diagnoses.push(angular.copy(answer));
            /**
             * Die neu hinzugefügte Diagnose suchen und als momentan angezeigte Diagnose setzen.
             * Dies ist nötig weil newDiagnosis nach dieser Methode nicht mehr definiert ist.
             */
            $scope.diagnoses.forEach(function(diagnosis){
                if(diagnosis.diagnosis_id === answer.diagnosis_id){
                    $scope.setDiagnosis(diagnosis);
                }
            });

            // Scoreverteilung neu laden
            $scope.allScoreDistributions = answerToDiagnosisScoreDistributionFactory.query(function () {
                getScoreDistributionsForCurrentDiagnosis();
            });
        });
    };

    /**
     * Spaltenweise über eine Tabelle iterieren ist nicht ganz einfach deshalb haben wir uns dafür entschieden lieber
     * die Daten der verschiedenen Diagnosen Reihenweise aufzubereiten.
     * Header:  [Diagnose1, Diagnose2, Diagnose3]
     * Frage1:  [Score Ja Diagnose1, Score Ja Diagnose2, Score Ja Diagnose3]
     *          [Score Nein Diagnose1, Score Nein Diagnose2, Score Nein Diagnose3]
     * Frage2:  ...
     *
     * Vorgehen:
     * 1. Die RankingMap muss zurückgesetzt werden damit wird sichergestellt, dass sich nicht noch Score Verteilungen von einem
     *    früherigen Test darin verstecken.
     * 2. Über alle Fragen iterieren.
     * 3. Dabei je für die Ja Antwort und die Nein Antwort über die vom Server gelieferte Rangliste iterieren
     *    wenn eine Score Verteilung die selbe ID wie die diagnose der Rangliste welche gerade iteriert wird hat dann wird
     *    diese der rankinglist hinzugefügt, damit sich das ganze nicht verschiebt wenn eine keine Score Verteilung gefunden wird
     *    wird einfach undefined in die RankingList gesetzt. Schlussendlich wird die RankingList der RankingMap unter dem Index der Antwort
     *    JA oder Nein hinzugefügt.
     */
    function getRankingMap(){
        if(angular.isDefined($scope.perfectDiagnosisTest) && angular.isDefined($scope.perfectDiagnosisTest.perfect_diagnosis_ranking)){
            $scope.rankingMap={}; //reset
            $scope.questions.forEach(function(question){

                rankingList = [];
                $scope.perfectDiagnosisTest.perfect_diagnosis_ranking.forEach(function(diagnosis){
                    found = false;
                    $scope.allScoreDistributions.forEach(function(scoreDistribution){
                        if(diagnosis.diagnosis_id === scoreDistribution.diagnosis_id && question.answer_yes.answer_id === scoreDistribution.answer_id){
                            rankingList.push(scoreDistribution);
                            found = true;
                        }
                    });
                    if(!found){
                        rankingList.push(undefined);
                    }
                });
                $scope.rankingMap[question.answer_yes.answer_id]= angular.copy(rankingList);

                rankingList = [];
                $scope.perfectDiagnosisTest.perfect_diagnosis_ranking.forEach(function(diagnosis){
                    found = false;
                    $scope.allScoreDistributions.forEach(function(scoreDistribution){
                        if(diagnosis.diagnosis_id === scoreDistribution.diagnosis_id && question.answer_no.answer_id === scoreDistribution.answer_id){
                            rankingList.push(scoreDistribution);
                            found = true;
                        }
                    });
                    if(!found){
                        rankingList.push(undefined);
                    }
                });
                $scope.rankingMap[question.answer_no.answer_id]= angular.copy(rankingList);
            });
        }
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
                findGermanDesignation();
            }
            $scope.diagnosesLoaded = true;
            /**
             * Antwort Score Verteilungen vom Server laden
             */
            $scope.allScoreDistributions = answerToDiagnosisScoreDistributionFactory.query(function () {
                getScoreDistributionsForCurrentDiagnosis();
            });
        });

        /**
         * Fragen vom Server laden
         */
        $scope.questions = questionFactory.query(function () {
            $scope.questionsLoaded = true;
        });


        $q.all([$scope.questions.$promise, $scope.diagnoses.$promise]).then(function () {
            getPerfectDiagnosisForCurrentDiagnosis();
            getScoreDistributionsForCurrentDiagnosis();
        });
    }

    //Watchers
    /**
     * Aktuelle Diagnose beobachten sobald etwas geändert wird dem Server mitteilen
     */
    $scope.$watch('diagnosis', function(newValue, oldValue){
        if(angular.isDefined(newValue)){
            if(newValue != oldValue && newValue.diagnosis_id === oldValue.diagnosis_id){
                diagnosisFactory.update(newValue);
                newValue.descriptions.forEach(function(description){
                    diagnosisDescriptionFactory.update(description);
                });
                newValue.designations.forEach(function(designation){
                    diagnosisDesignationFactory.update(designation);
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
                    if(angular.equals(newQuestion.question_id, oldQuestion.question_id)){
                        changed=false;
                        //Nun noch überprüfen ob sich der Score geändert hat
                        if(angular.isDefined(newQuestion.distribution_yes) && angular.isDefined(oldQuestion.distribution_yes) && !angular.equals(parseInt(newQuestion.distribution_yes.score,10), parseInt(oldQuestion.distribution_yes.score,10)) && newQuestion.distribution_yes.diagnosis_id === oldQuestion.distribution_yes.diagnosis_id){
                            if(angular.isDefined(newQuestion.distribution_yes.distribution_id) && !angular.equals(newQuestion.distribution_yes.score,"") && newQuestion.distribution_yes.score!==null){
                                newQuestion.distribution_yes.$update();

                                $scope.allScoreDistributions.forEach(function (scoreDistribution) {
                                    if (scoreDistribution.distribution_id === newQuestion.distribution_yes.distribution_id) {
                                        scoreDistribution.score = newQuestion.distribution_yes.score;
                                        changed = true;
                                    }
                                });
                            }
                        }
                        //Nun noch überprüfen ob sich der Score geändert hat
                        if(angular.isDefined(newQuestion.distribution_no) && angular.isDefined(oldQuestion.distribution_no) && !angular.equals(parseInt(newQuestion.distribution_no.score,10), parseInt(oldQuestion.distribution_no.score,10)) && newQuestion.distribution_no.diagnosis_id === oldQuestion.distribution_no.diagnosis_id){
                            if(angular.isDefined(newQuestion.distribution_no.distribution_id)&& !angular.equals(newQuestion.distribution_no.score,"") && newQuestion.distribution_no.score!==null){
                                newQuestion.distribution_no.$update();

                                $scope.allScoreDistributions.forEach(function (scoreDistribution) {
                                    if (scoreDistribution.distribution_id === newQuestion.distribution_no.distribution_id) {
                                        scoreDistribution.score= newQuestion.distribution_no.score;
                                        changed = true;
                                    }
                                });
                            }
                        }
                        if(changed){
                            getRankingMap();
                        }
                    }
                });
            });
        }
    }, true);

    /**
     * Ranking Map beobachten und sobald etwas geändert wird dem server mitteilen
     */
    $scope.$watch('rankingMap', function(newValue, oldValue){
        newScoreDistributions = [];
        //Wir bekommen die alte Version der Syndrome und die neue Version
        if(angular.isDefined(newValue)){
            for(var index in $scope.rankingMap) {
                $scope.rankingMap[index].forEach(function(scoreDistribution){
                    //Nun noch überprüfen ob sich der Score geändert hat
                    if(angular.isDefined(oldValue)){
                        oldValue[index].forEach(function(scoreDistribution2){
                            if(angular.isDefined(scoreDistribution) && angular.isDefined(scoreDistribution2) && scoreDistribution.distribution_id === scoreDistribution2.distribution_id && !angular.equals(parseInt(scoreDistribution.score,10), parseInt(scoreDistribution2.score,10))){
                                scoreDistribution.$update();
                            }
                        });
                    }

                    if(angular.isDefined(scoreDistribution)){
                            newScoreDistributions.push(scoreDistribution);
                    }
                });
            }
            $scope.allScoreDistributions = [];
            $scope.allScoreDistributions = angular.copy(newScoreDistributions);
            getScoreDistributionsForCurrentDiagnosis();
            getRankingMap();
        }
    },true);
});