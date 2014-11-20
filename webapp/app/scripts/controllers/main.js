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
        $scope.isEditing = false;
        $scope.rooms = [];

        /*
         * Public Functions
         */
        $scope.addOrUpdateRoom = function(room) {
            if (!room.name || !room.calendar || !room) {
                // Show some error

            } else if ($scope.isEditing) {
                // Make server call to edit room
                $scope.clearInputs();

            } else {
                $scope.clearInputs();
                room.capacity = parseInt(room.capacity);
                RoomApi.addRoom(room).then(function(result) {
                    $scope.rooms.push(result.room);
                });
            }
        };

        $scope.editRoom = function(room) {
            $scope.room = room;
            $scope.isEditing = true;
        };

        $scope.clearInputs = function() {
            $scope.isEditing = false;
            $scope.room = '';
        };

        /*
         * Private Functions
         */
        var getRooms = function() {
            RoomApi.getRooms().then(function(result) {
                $scope.rooms = result.rooms;
                sortRoomsByName();
            });
        };

        var sortRoomsByName = function() {
            $scope.rooms.sort(function(a, b) {
                var roomA = a.name.toLowerCase();
                var roomB = b.name.toLowerCase();

                if (roomA > roomB) {
                    return 1;
                } else if (roomA < roomB) {
                    return -1;
                } else {
                    return 0;
                }
            });
        };

        // Pull for the list of rooms on page load
        getRooms();
    });
