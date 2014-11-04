'use strict';

/**
 * @ngdoc function
 * @name capstone.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the capstone
 */
angular.module('capstone')
    .controller('MainCtrl', function ($scope) {
        $scope.awesomeThings = [
            'HTML5 Boilerplate',
            'AngularJS',
            'Karma'
        ];
    });
