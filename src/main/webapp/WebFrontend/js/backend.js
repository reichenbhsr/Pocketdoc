(function(){

    var backend = angular.module('pocketdocBackend', ['pocketdocData', 'pocketdocFactories']);

    backend.factory('UserService', ['_', 'DataService', 'UtilService', 'LanguageService', '$cookies', 'loginFactory', 'logoutFactory', 'userFactory', function( _ ,  DataService, UtilService, LanguageService, $cookies, loginFactory, logoutFactory, userFactory ){

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
            user.oldPassword = data.oldPassword;
            user.newPassword = data.newPassword;

            userFactory.updateUser(user, function(result){

                if (result.errorType == -1)
                {
                    currentUser = JSON.parse(result.user);
                    currentUser.code = LanguageService.getCodeById(currentUser);
                    success({name: currentUser.name, lang: currentUser.code});
                }
                else if (result.errorType = 1)
                {
                    error({errorType: 1, message: "Passwort ist fehlerhaft!"})
                }

            });
        };

        var del = function( data, success, error ) { //  MODIFIED

            var user = buildUserObject(currentUser);
            var name = user.name;
            userFactory.deleteUser(user, function(result){

                currentUser = {
                    user_id: -1,
                    lang: currentUser.lang,
                    code: currentUser.code
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
            loginFactory.login(data, function(result){
                console.log(result);
                if (result.errorType == -1){
                    currentUser = JSON.parse(result.user);
                    currentUser.code = LanguageService.getCodeById(currentUser);

                    saveUserSession(currentUser);
                    success(currentUser);
                }
                else if (result.errorType == 0)
                {
                    error({errorType: 0, message: "Email Adresse ist nicht registriert!"})
                }
                else if (result.errorType == 1)
                {
                    error({errorType: 1, message: "Password ist fehlerhaft!"})
                }

            });

        };

        var logout = function( data, success, error ) {

            logoutFactory.logout(data, function(result){
                var userName = currentUser.name;

                currentUser = {
                    user_id : -1,
                    lang : currentUser.lang
                };

                clearSession();

                success({name: userName});
            });
        };

        var checkData = function( data, success, error ) {

        };

        var getCurrent = function(){
            return Object.create(currentUser);
        };

        var updateLang = function( data, success, error ) { //  MODIFIED
            currentUser.lang = data.lang;
            if (currentUser.user_id == -1){
                success({lang: LanguageService.getCodeById(data)});
            }
            else{
                update(currentUser, function(){
                    success({lang: LanguageService.getCodeById(data)});
                }, function(){
                    error("Fehler beim Wechsel der Sprache");
                });
            }
        };

        var isInUse = function( data, success, error ) { // MODIFIED

            userFactory.isInUse({checkInUse: data.email}, function(result){
                success({inUse: result.inUse});
            });

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
            if ( currentUser.code && typeof( currentUser.code ) !== "undefined" ) {
                return currentUser.code;
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
                lang: data.lang,
                code: data.code || getDefaultLang(),
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
         * @return {int}
         * @author Philipp Christen
         */
        var getDefaultLang = function() {
            return 1;
        };

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

    backend.factory('LanguageService',[ 'languageFactory', function(languageFactory){

        var languages;

        var get = function( data, success, error ) {

            if (angular.isUndefined(languages))
                getFromServer(data, success, error);
            else
                success(languages);

        };

        var getCode = function(data){
            var res;
            for(var i = 0; i < languages.length; i++){
                if (languages[i].id == data.lang)
                    res = languages[i].code;
            }
            return res;
        };

        var getFromServer = function(data, success, error){

            languageFactory.getLanguages(data, function(result){
                languages = result.languages;
                success(languages);
            });

        };

        return {
            getLanguages : get,
            getCodeById : getCode
        };
    }]);

    backend.factory('RunService', [
        'UserService', 'DataService', 'UtilService', '$translate', 'nextQuestionFactory', 'runFactory',
        function( UserService,   DataService,   UtilService ,  $translate, nextQuestionFactory, runFactory ){

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
                user = userData;
                runFactory.resetRun({Id: userData.user_id}, userData, function(result){

                    getQ( userData, success, error );
                })
            };

            /**
             * User answered a question.
             *
             * @param  {Object} data    [description]
             * @param  {Function} success
             * @param  {Function} error
             * @author Roman Eichenberger, Philipp Christen
             */
            var answerQ = function( data, success, error ) {    // MODIFIED
                var response = {
                    answer: data.answer,
                    question: data.question
                };

                runFactory.sendAnswer({Id: user.user_id}, response, function(result){
                    console.log(result);
                    if (result.ErrorCode == -1){
                        getQ({user_id: user.user_id}, success, error);
                    }
                    else if (result.ErrorCode == 0){
                        error("Fehler beim Speichern der Antwort");
                    }
                    else if (result.ErrorCode == 1){
                        error("Session abgelaufen. Bitte neu starten!")
                    }
                });

            };

            var getQ = function( questionData, success, error ) { // MODIFIED

                console.log(questionData);
                nextQuestionFactory.getNextQuestion(buildUserObject(user), function(result){
                    console.log(result);
                    currentQuestion = result.question;

                    if (user.user_id == -1)
                        user.user_id = result.user.user_id;

                    if (typeof(currentQuestion) == "undefined"){
                        success({diagnosis: result.diagnosis, action_suggestion: result.action_suggestion});
                    }
                    else{
                        var questionResult = {};

                        questionResult.id = currentQuestion.question_id;
                        for (var i = 0; i < currentQuestion.descriptions.length; i++)
                            if(currentQuestion.descriptions[i].language_id == user.lang)
                                questionResult.description = currentQuestion.descriptions[i].description;

                        var answerTexts = [];

                        answerTexts.push(
                            {
                                id : currentQuestion.answer_yes.answer_id,
                                desc : "Ja",
                                style: "md-accent",
                                answer: currentQuestion.answer_yes,
                                type: 0,
                                question: currentQuestion
                            }
                        );

                        answerTexts.push(
                            {
                                id : -1,
                                desc : "Weiss nicht",
                                style: "md-primary",
                                answer: { answer_id: -1},
                                type: 2,
                                question: currentQuestion
                            }
                        );

                        answerTexts.push(
                            {
                                id : currentQuestion.answer_no.answer_id,
                                desc : "Nein",
                                style: "md-warn",
                                answer: currentQuestion.answer_no,
                                type: 1,
                                question: currentQuestion
                            }
                        );

                        questionResult.answers = answerTexts;

                        success({question: questionResult});
                    }

                });

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

            var buildUserObject = function( data ) {
                return {
                    user_id: data.user_id,
                    password: data.password,
                    lang: data.lang,
                    code: data.code || 'de',
                    email: data.email,
                    name: data.name,
                    gender: data.gender,
                    age_category: data.age_category
                }
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

        var langId = 1;//UtilService.getIdByLocale(UserService.getLang(), DataService.languages());

        // Placeholder
        var getAll = function(success, error ) {};

        var getByID = function( diagID, actionID, success, error ) {
            var diagnosisData = {};

            // Get current language again
            langId = 1; //UtilService.getIdByLocale(UserService.getLang(), DataService.languages());

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

            //getCurrentLanguageObject : function(langId, dataArray){
            //    var obj = $.grep(dataArray, function(e){ return e.lang == langId; });
            //    return obj[0];
            //},
            //
            //getLocaleById : function(langId, dataArray){
            //    var obj = $.grep(dataArray, function(e){ return e.id == langId; });
            //    return obj[0].locale;
            //},
            //
            //getIdByLocale : function(localeId, dataArray){
            //    var obj = $.grep(dataArray, function(e){ return e.locale == localeId; });
            //    return obj[0].id;
            //},

            delay : function(call, param){
                setTimeout(function(){call(param);}, 500);
            }

        };

        return UtilService;
    });

})();