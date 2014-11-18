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
            $scope.room = '';
            if(!room.name || !room.calendar) {
                // Show some error

            } else {
                // Make server call
                room.capacity = parseInt(room.capacity);
                RoomApi.addRoom(room).then(function(result) {
                    $scope.rooms.push(result.room);
                });
            }
        };

        var getRooms = function() {
            RoomApi.getRooms().then(function(result) {
                $scope.rooms = result.rooms;
            });
        }


        // Pull for the list of rooms on page load
        getRooms();
    });
