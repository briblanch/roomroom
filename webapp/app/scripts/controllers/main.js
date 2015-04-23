'use strict';

/**
 * @ngdoc function
 * @name roomroom.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the roomroom
 */

angular.module('roomroom')
    .controller('MainCtrl', function($scope, $timeout, RoomApi) {
        $scope.roomUpdated = false;
        $scope.roomUpdatedError = false;
        $scope.isEditing = false;
        $scope.rooms = [];

        /*
         * Public Functions
         */
        $scope.addOrUpdateRoom = function(room) {
            if (!room || !room.name || !room.calendar) {
                // Show some error
                $scope.clearInputs();
                return;
            } else {
                room.capacity = parseInt(room.capacity);
                room.background = $scope.file;

                if ($scope.isEditing) {
                    RoomApi.updateRoom(room).then(function(result) {
                        showSuccessAlert();
                    }, function(result) {
                        showErrorAlert();
                    });
                } else {
                    RoomApi.addRoom(room).then(function(result) {
                        $scope.rooms.push(result.room);
                        showSuccessAlert();
                    }, function(result) {
                        showErrorAlert();
                    });
                }

                $scope.clearInputs();
            }
        };

        $scope.editRoom = function(room) {
            $scope.room = room;
            $scope.isEditing = true;
        };

        $scope.clearInputs = function() {
            $scope.room = '';
            $scope.isEditing = false;
            clearFile();
        };

        $scope.$watch('rooms', function() {
            sortRoomsByName();
        });

        /*
         * Private Functions
         */
        var getRooms = function() {
            RoomApi.getRooms().then(function(result) {
                $scope.rooms = result.rooms;
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

        var clearFile = function() {
            $scope.$broadcast('clearFile');
        };

        var showSuccessAlert = function() {
            $scope.roomUpdated = true;
            $timeout(function() {
                $scope.roomUpdated = false;
                $scope.isEditing = false;
            }, 2000, true);
        };

        var showErrorAlert = function() {
            $scope.roomUpdatedError = true;
            $timeout(function() {
                $scope.isEditing = false;
                $scope.roomUpdatedError = false;
            }, 2000, true);
        };

        $scope.showFileError = function() {
            $scope.roomUpdatedError = true;
            $timeout(function() {
                $scope.isEditing = false;
                $scope.roomUpdatedError = false;
            }, 2000, true);
        };

        // Pull for the list of rooms on page load
        getRooms();
    });
