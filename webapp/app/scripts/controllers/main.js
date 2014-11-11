'use strict';

/**
 * @ngdoc function
 * @name capstone.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the capstone
 */

angular.module('capstone')
    .controller('MainCtrl', function($scope, RoomApi) {
        $scope.rooms = [];
        $scope.addRoom = function(room) {
            if(!room.name || !room.calendar) {
                // Show some error
            } else {
                // Make server call
            }
        };
    });
