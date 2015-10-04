angular.module('pocketDocApp').factory("settingFactory", function($http, $resource){
    var factory = {};

    var baseUrl = "/setting/:id";

    return $resource(
        baseUrl, { id: '@setting_id' },{
            'update': { method:'PUT' }
        }
    );
});