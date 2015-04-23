'use strict';

/**
 * @ngdoc directive
 * @name roomroomApp.directive:fileUpload
 * @description
 * # fileUpload
 */

angular.module('roomroom')
    .directive('fileUpload', function($parse) {
        var fileIsValid = function(file) {
            return file.type.indexOf('image') > -1;
        };

        var clearFile = function(element) {
            element.val('');
        };

        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                var model = $parse(attrs.fileUpload);
                var modelSetter = model.assign;

                element.bind('change', function() {
                    var file = element[0].files[0];

                    if (fileIsValid(file)) {
                        scope.$apply(function() {
                            modelSetter(scope, file);
                        });
                    } else {
                        window.alert('File must be an image');
                        clearFile(element);
                    }
                });

                scope.$on('clearFile', function() {
                    clearFile(element);
                });
            }
        };
    });
