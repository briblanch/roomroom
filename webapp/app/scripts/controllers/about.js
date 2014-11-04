'use strict';

/**
 * @ngdoc function
 * @name capstone.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the capstone
 */
angular.module('capstone')
    .controller('AboutCtrl', function ($scope) {
        $scope.awesomeThings = [
            'HTML5 Boilerplate',
            'AngularJS',
            'Karma'
        ];
    });
