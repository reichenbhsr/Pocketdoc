angular.module('pocketDocApp').controller('loginController', function($scope, $cookieStore, $resource) {
    //Default variable initialization
    $scope.user = {name:"", password:"", isLogged:false};
    logout = $resource("/logout");
    login = $resource("/login");

    /**
     * User von Cookie Store lesen falls er existiert gerade anmelden
     */
    if(angular.isDefined($cookieStore.get("user"))){
        $scope.user = $cookieStore.get("user");
        login.save($scope.user), function(data){
            $scope.user.isLogged = data.status;
            window.location.reload();
        }
    }

    /**
     * User beim Server anmelden und Cookie erstellen
     */
    $scope.login = function(){
        login.save($scope.user, function(data){
            $scope.user.isLogged = data.status;
            $cookieStore.put("user", $scope.user);
            window.location.reload();
        })
    }

    /**
     * User beim Server abmelden und das Cookie löschen
     */
    $scope.logout = function(){
        $scope.user.name ="";
        $scope.user.password ="";
        logout.save($scope.user, function(data){
            $scope.user.isLogged = !data.status;
            $cookieStore.remove("user");
        })

    }
});