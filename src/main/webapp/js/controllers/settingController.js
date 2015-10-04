angular.module('pocketDocApp').controller('settingController', function($scope, settingFactory) {
    //Default Initialisierung von benutzten Variablen
    $scope.settingsLoaded = false;
    $scope.settings = [];

    /**
     * Falls User nicht eingeloggt ist sollen die Daten nicht vom server geladen werden
     */
    if($scope.user.isLogged) {
        /**
         * Settings vom Server laden
         */
        $scope.settings = settingFactory.query(function () {
            if ($scope.settings.length > 0) {
            }
            $scope.settingsLoaded = true;
        });
    }

    /**
     * Settings beobachten und sobald etwas geändert wird dem server mitteilen
     */
    $scope.$watch('settings', function(newValue, oldValue){
        //Wir bekommen die alte Version der Settings und die neue Version
        if(angular.isDefined(newValue)){
            if(newValue != oldValue){
                //Da es nur 2 Settings gibt sollen einfach immer beide erneuert werden sobald eines ändert
                newValue.forEach(function(setting){
                    if(!angular.equals(setting.value,"")){
                        settingFactory.update(setting)
                    };
                });
            }
        }
    }, true)
});