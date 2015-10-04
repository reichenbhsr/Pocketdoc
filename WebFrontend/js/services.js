(function(){

	var services = angular.module('pocketdocServices', []);
	
	services.factory('DiagnosisData', function(){
	
		var DiagnosisData = {};
		return DiagnosisData;
	
	});
	
	services.factory('FollowUpData', function(){
		var FollowUpData = {};
		return FollowUpData;
	});
	
})();