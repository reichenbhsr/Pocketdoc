(function(){

    var backend = angular.module('pocketdocBackend', ['pocketdocData', 'pocketdocFactories']);

    backend.factory('UserService', ['_', 'DataService', 'UtilService', '$cookies', 'loginFactory', 'userFactory', function( _ ,  DataService, UtilService, $cookies, loginFactory, userFactory ){

        var isLoggedIn = function() { // MODIFIED
            return currentUser.user_id !== -1;
        };

        var create = function( data, success, error ) { // MODIFIED
            if ( isLoggedIn() ) {
                UtilService.delay(error, "Ein Benutzer ist aktuell eingeloggt. Bitte zuerst ausloggen.");
            } else {

                var user = buildUserObject(data);

                userFactory.createUser(user, function(result){
                    console.log("User created")
                    var user = JSON.parse(result.user);
                    console.log(user);

                    user.password = data.password;

                    login(user, success, error);
                });
            }
        };

        var get = function( data, success, error ) {    // MODIFIED

            userFactory.getUser(data, function(result){
                if (result.length == 0)
                    error("Id ist ungültig!")
                else
                    success(result);
            });

        };

        var update = function( data, success, error ) { // MODIFIED

            var user = buildUserObject(data);
            userFactory.updateUser(user, function(result){

                currentUser = JSON.parse(result.user);
                success({name: result.name, lang: result.lang});

            });
        };

        var del = function( data, success, error ) {

            var user = buildUserObject(currentUser);
            var name = user.name;
            userFactory.deleteUser(user, function(result){

                currentUser = {
                    user_id: -1,
                    lang: currentUser.lang
                }

                clearSession();

                success({name: name});

            });

        };

        var login = function( data, success, error ) { // MODIFIED

            if ( !data || ( !data.session && !data.email ) ) {
                UtilService.delay(error,{
                    errorType: 2,
                    message: "Keine Daten eingegeben!"
                });
                return;
            }

            console.log(data);
            loginFactory.save(data, function(result){
                console.log(result);

                if (typeof(result.user) !== "undefined"){
                    currentUser = JSON.parse(result.user);

                    saveUserSession(currentUser);
                    success(currentUser);
                }
                else{
                    error({errorType: 1, message: "Benutzereingaben falsch"});
                }

            });

        };

        var logout = function( data, success, error ) {

            var userName = currentUser.name;

            currentUser = {
                user_id : -1,
                lang : currentUser.lang
            };

            clearSession();

            UtilService.delay(success,{name : userName});
        };

        var checkData = function( data, success, error ) {

        };

        var getCurrent = function(){
            return Object.create(currentUser);
        };

        var updateLang = function( data, success, error ) {
            currentUser.lang = data.lang;
            UtilService.delay(success,
                {
                    lang: currentUser.lang
                }
            );
        };

        var isInUse = function( data, success, error ) {
            var users = JSON.parse(localStorage.getItem("users"));

            if (users == null)
                UtilService.delay(success,{inUse: false});
            else {
                var user = $.grep(users, function(e){ return e.email == data.email; });

                UtilService.delay(success,{ inUse: user.length !== 0 });
            }
        };

        /**
         * Create arbitrary sessionID. For demonstration purposes, we create a
         * date that's 24 hours in the future. This is the time in which the
         * session is valid.
         * It's suggested that this is handled on the server, in non-visible
         * code and with security in mind.
         *
         * @name getSesstion
         * @param  {Object} userData [description]
         * @return {String}
         * @author Philipp Christen
         */
        var getSession = function( userData ) {
            var validity = new Date();
            validity.setDate( validity.getDate() + 1 /*days*/ );

            return validity.toString();
        };

        var hasValidSession = function( data ) {
            return data.session && new Date( data.session ) >= new Date();
        };

        var saveUserSession = function( userData ) {
            var toBeSaved = {};
            angular.extend( toBeSaved, userData, { session: getSession( userData ) } );

            $cookies.pocketDocUser = angular.toJson( toBeSaved );
        };

        /**
         * Clears a session ID. Should propagate to the backend.
         *
         * @author Philipp Christen
         */
        var clearSession = function() {
            $cookies.pocketDocUser = null;
        }

        /**
         * The user's language is used often, without need for the whole user.
         * This returns their language or the default value, if necessary.
         *
         * @name getLang
         * @return {String} language locale
         * @author Philipp Christen
         */
        var getLang = function() {
            if ( currentUser.lang && typeof( currentUser.lang ) !== "undefined" ) {
                return currentUser.lang;
            } else {
                return getDefaultLang();
            }
        };

        /**
         * Angular sometimes moves properties into an object's prototype so
         * that they're not copied anymore. Here we take potentially incomplete
         * data and generate a complete user object with all its properties.
         *
         * @name buildUserObject
         * @param  {Object} data
         * @return {Object}
         * @author Philipp Christen
         */
        var buildUserObject = function( data ) {
            return {
                user_id: data.user_id,
                password: data.password,
                lang: data.lang || getDefaultLang(),
                email: data.email,
                name: data.name,
                gender: data.gender,
                age_category: data.age_category
            }
        };

        /**
         * If all else fails, return German as the default language.
         *
         * @name getDefaultLang
         * @return {String}
         * @author Philipp Christen
         */
        var getDefaultLang = function() {
            return 'de';
        }

        // on startup, save the fake data to the localstorage
        if ( !localStorage.getItem("users") ) {
            localStorage.setItem( "users", angular.toJson( DataService.users() ) );
        }

        var currentUser = {
            user_id : -1,
            lang : getDefaultLang()
        };

        return {
            createUser : create,
            getUser : get,
            updateUser : update,
            deleteUser : del,
            loginUser : login,
            logoutUser : logout,
            checkUserData : checkData,
            getCurrentUser : getCurrent,
            updateLanguage : updateLang,
            isEmailInUse : isInUse,
            isLoggedIn: isLoggedIn,
            getSession: getSession,
            getLang: getLang
        };
    }]);

    backend.factory('HistoryService', function(){

        var get = function( data, success, error ) {

        };

        var getEntry = function( data, success, error ) {

        };

        var del = function( data, success, error ) {

        };

        var create = function( data, success, error ) {

        };

        return {
            getUserHistory : get,
            getHistoryEntry : getEntry,
            deleteHistoryEntry : del,
            createHistoryEntry : create
        };
    });

    backend.factory('RunService', [
        'UserService', 'DataService', 'UtilService', '$translate',
        function( UserService,   DataService,   UtilService ,  $translate ){

            var run = {};
            var	nextQuestions = [];
            var	currentQuestion = null;
            var	followUp = null;
            var	user = null;



            /**
             * First question of a run is the question with the ID 0.
             * If it's a followUp, it's of course the question that was defined
             * as the startquestion in the followUp.
             *
             * @name start
             * @param {Object} userData Data concerning the user that is the subject of the diagnosis
             * @param  {Function} success
             * @param  {Function} error
             * @author Philipp Christen
             */
            var start = function( userData, success, error ) {
                var startQuestionID = 0,
                    qData = {};
                user = userData;

                if ( followUp !== null ) {
                    startQuestionID = followUp.startQuestion;
                }

                qData.id = startQuestionID;
                getQ( qData, success, error );
            };

            /**
             * User answered a question.
             *
             * @param  {Object} data    [description]
             * @param  {Function} success
             * @param  {Function} error
             * @author Roman Eichenberger, Philipp Christen
             */
            var answerQ = function( data, success, error ) {
                var currQuestion = currentQuestion;
                var answerObj = UtilService.getElementById(data.answerId, currQuestion.answers );

                nextQuestions = answerObj.next_questions;

                if ( nextQuestions.length === 0 || nextQuestions[0] === -1 ) {
                    UtilService.delay(error, $translate.instant('error_noMoreQuestions') );
                } else {
                    getQ( { id: nextQuestions.pop() }, success, error );
                }
            };

            var getQ = function( questionData, success, error ) {

                var allQuestions = DataService.questions();
                var firstQuestion = UtilService.getElementById( questionData.id, allQuestions );

                var questionResult = {};

                var answerTexts = [];

                currentQuestion = firstQuestion;

                // Set Question Text
                var langId = UtilService.getIdByLocale(UserService.getLang(), DataService.languages());
                var questionText = UtilService.getCurrentLanguageObject(langId, firstQuestion.description);
                questionResult.id = questionData.id;
                questionResult.description = questionText.text;

                // Set Answer Texts
                for(var i = 0; i < firstQuestion.answers.length; i++)
                {
                    var desc = UtilService.getCurrentLanguageObject(langId, firstQuestion.answers[i].desc);
                    answerTexts.push(
                        {
                            id : firstQuestion.answers[i].id,
                            desc : desc.text,
                            style: firstQuestion.answers[i].style || "",
                            diagnosis: firstQuestion.answers[i].diagnosis,
                            action_suggestion: firstQuestion.answers[i].action_suggestion
                        }
                    );
                }

                questionResult.answers = answerTexts;

                UtilService.delay(success, questionResult);
            };

            var addDiagnosis = function( data, success, error ) {

            };

            var change = function( data, success, error ) {
                getQ(
                    {
                        id: data.questionId
                    },
                    function(questionData){
                        answerQ(
                            {
                                answerId: data.answerId
                            },
                            function(answerData){
                                UtilService.delay(success, answerData);
                            },
                            function(errorMsg){
                                UtilService.delay(error, errorMsg);
                            }
                        );
                    },
                    function(errorMsg){
                        UtilService.delay(error, errorMsg);
                    }
                );
            };

            var acceptDiag = function( data, success, error ) {
                // Aktueller Run aufräumen und beenden
                delete nextQuestions;

                // if this was a followUp, delete it now
                if ( followUp !== null ) {
                    //FollowupService.deleteFollowup( followUp.id );
                    delete followUp;
                }
                UtilService.delay(success);
            };

            /**
             * If a followUp is active, it overrides some settings, like the
             * first question of the run.
             *
             * @param  {[type]} followUp [description]
             */
            var setFollowUp = function ( newFollowUp ) {
                followUp = newFollowUp;
            };

            var getFollowUp = function() {
                followUp = typeof(followUp) === "undefined" ? false : followUp;
                return followUp;
            };

            var getUser = function(){
                return user;
            };

            return {
                startRun : start,
                answerQuestion : answerQ,
                getQuestionData : getQ,
                changeAnswer : change,
                acceptDiagnosis : acceptDiag,
                setFollowUp: setFollowUp,
                getFollowUp: getFollowUp,
                getUserData: getUser
            };
        }]);

    backend.factory('DiagnosisService', ['DataService', 'UtilService', 'UserService', function( DataService, UtilService, UserService ){

        var langId = UtilService.getIdByLocale(UserService.getLang(), DataService.languages());

        // Placeholder
        var getAll = function(success, error ) {};

        var getByID = function( diagID, actionID, success, error ) {
            var diagnosisData = {};

            // Get current language again
            langId = UtilService.getIdByLocale(UserService.getLang(), DataService.languages());

            // Set diagnosis
            diagnosisData.diagnosis = getDiagByID( diagID );

            // Set Action Suggestion
            diagnosisData.action_suggestion = getActionByID( actionID );

            if ( diagnosisData.diagnosis && diagnosisData.action_suggestion ) {
                UtilService.delay(success, diagnosisData );
            } else {
                UtilService.delay(error, "Something went wrong while getting the Diagnosis!");
            }
        };

        /**
         * Returns a single translated diagnosis by checking the ID.
         *
         * @param  {Number} ID
         * @return {Object}
         * @author Philipp Christen
         */
        var getDiagByID = function( ID ) {
            var diagnosis = UtilService.getElementById( ID, DataService.diagnoses() );

            return {
                id: ID,
                short_desc : UtilService.getCurrentLanguageObject( langId, diagnosis.short_desc).text,
                description : UtilService.getCurrentLanguageObject( langId, diagnosis.description).text
            };
        };

        /**
         * Returns a single translated action suggestion by checking the ID.
         *
         * @name getActionByID
         * @param  {Number} ID
         * @return {Object}
         * @author Philipp Christen
         */
        var getActionByID = function( ID ) {
            var action_suggestion = UtilService.getElementById( ID, DataService.actionSuggestions() );

            return {
                id: ID,
                description : UtilService.getCurrentLanguageObject( langId, action_suggestion.description).text
            };
        };

        return {
            getAll : getAll,
            getByID : getByID,
            getDiagByID: getDiagByID,
            getActionByID: getActionByID
        };
    }]);

    backend.factory('FollowupService', [ '_', 'DataService', 'UtilService', 'UserService', 'RunService', function(  _ ,  DataService ,  UtilService ,  UserService ,  RunService ){

        var save = function ( followUps ) {
            localStorage.setItem( "followUps", angular.toJson( followUps ) );
        };

        var register = function ( data ) {
            var followUps = getAll();

            // check if other followUps already exist
            if ( followUps != null && followUps.length > 0 ) {
                //if so, get highest ID and add 1.
                data.id = _.max(followUps, function(fUp){ return fUp.id; }).id + 1;
            } else {
                followUps = [];
                data.id = 0;	//else: first followUp!
            }

            followUps.push( data );

            save( followUps );
        };

        /**
         * Marks a followUp as "active" by passing it to the run.
         * It can then be deleted in the followUp-list.
         *
         * @name start
         * @param  {Number} followUpID
         * @author Philipp Christen
         */
        var start = function( followUpID ){
            RunService.setFollowUp( getByID( followUpID ) );
            del( followUpID, function(){}, function(){} );
        };

        /**
         * Removes one followUp from the pile.
         *
         * @name   del
         * @param  {Number} followUpID
         * @param  {Function} success
         * @param  {Function} error
         * @author Philipp Christen
         */
        var del = function(followUpID, success, error ) {
            var followUps = getAll();

            // removes the followUp with the passed ID
            followUps = _.reject( followUps, function(fUp){
                return fUp.id === followUpID;
            });

            save( followUps );

            UtilService.delay(success, followUpID );
        };

        /**
         * Returns all followUps
         *
         * @name   getAll
         * @return {Array}
         * @author Philipp Christen
         */
        var getAll = function(){
            return JSON.parse( localStorage.getItem("followUps") );
        };

        /**
         * Returns a single follow up.
         *
         * @param  {[type]} id id of the follow up
         * @return {Object}    followUp with the passed id
         * @author Philipp Christen
         */
        var getByID = function( id ) {
            return _.find( getAll(), function(fUp) { return fUp.id === id; });
        };

        /**
         * Gets all followUps for a single user.
         *
         * @name   getByUserID
         * @param  {Number} userID
         * @return {Array}
         * @author Philipp Christen
         */
        var getByUserID = function( userID ) {
            var followUps = _.filter( getAll(), function( fUp ){
                return fUp.user === userID;
            });
            return followUps.reverse();
        };

        /**
         * [getStartQuestion description]
         * @param  {[type]} followUpID [description]
         * @return {[type]}            [description]
         */
        var getStartQuestion = function( followUpID ) {
            return getByID( followUpID ).startQuestion;
        };

        /**
         * Marks a new followUp as read.
         *
         * @param  {Number} fUpID
         * @author Philipp Christen
         */
        var markAsRead = function( fUpID ) {
            var followUps = getAll(),
                fUpToChange = getByID( fUpID );

            // removes the followUp with the passed ID
            followUps = _.reject( followUps, function(fUp){
                return fUp.id === fUpID;
            });
            fUpToChange.newest = false;

            followUps.push( fUpToChange );

            save( followUps );

        };


        // on startup, save the fake data to the localstorage if that hasn't
        // happened already
        if ( !localStorage.getItem("followUps") ) {
            save( DataService.followUps() );
        }

        return {
            registerFollowup : register,
            startFollowup : start,
            deleteFollowup : del,
            getFollowupsForUser : getByUserID,
            getAll: getAll,
            getByID: getByID,
            markAsRead: markAsRead
        };
    }]);

    backend.factory('MetaDataService', [ '_', 'DataService', function(  _ ,  DataService  ){
        var getLanguages = function() {
                return DataService.languages();
            },

            getAgeRanges = function() {
                return DataService.ageRanges();
            };

        return {
            getLanguages: getLanguages,
            getAgeRanges: getAgeRanges
        };
    }]);

    backend.factory('UtilService', function(){

        var UtilService = {

            getElementById : function(id, array){
                var obj = $.grep(array, function(e){ return e.id == id; });
                return obj[0];
            },

            getCurrentLanguageObject : function(langId, dataArray){
                var obj = $.grep(dataArray, function(e){ return e.lang == langId; });
                return obj[0];
            },

            getLocaleById : function(langId, dataArray){
                var obj = $.grep(dataArray, function(e){ return e.id == langId; });
                return obj[0].locale;
            },

            getIdByLocale : function(localeId, dataArray){
                var obj = $.grep(dataArray, function(e){ return e.locale == localeId; });
                return obj[0].id;
            },

            delay : function(call, param){
                setTimeout(function(){call(param);}, 500);
            }

        };

        return UtilService;
    });

})();