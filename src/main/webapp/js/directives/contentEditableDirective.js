angular.module('pocketDocApp').directive('contenteditable', function() {
    return {
        require: 'ngModel',
        restrict: 'A',
        link: function(scope, element, attrs, controller) {
            var check_charcount;
            element.bind('blur', function() {
                return scope.$apply(function() {
                    return controller.$setViewValue(element.html());
                });
            });
            element.keydown(function(e) {
                return check_charcount(element.id, attrs.maxLength, e);
            });
            check_charcount = function(content_id, max, e) {
                if (e.which !== 8 && element.text().length > max) {
                    return e.preventDefault();
                }
            };
            return controller.$render = function() {
                return element.html(controller.$viewValue);
            };
        }
    };
});