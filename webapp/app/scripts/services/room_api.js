'use strict';

/**
 * @ngdoc service
 * @name roomroomApp.RoomApi
 * @description
 * # RoomApi
 * Factory in the roomroomApp.
 */
angular.module('roomroom')
    .factory('RoomApi', function($http, $q) {
        var addRoomUrl = 'api/room';
        var getRoomsUrl = 'api/rooms';
        var updateRoomUrl = 'api/rooms/';
        // Public API here
        return {
            addRoom : function(room) {
                var deferred = $q.defer();

                $http.post(addRoomUrl, room).then(function(result) {
                    deferred.resolve(result.data);
                }, function(result) {
                    deferred.reject(result.data);
                });

                return deferred.promise;
            },
            getRooms: function() {
                var deferred = $q.defer();

                $http.get(getRoomsUrl).then(function(result) {
                    deferred.resolve(result.data);
                });

                return deferred.promise;
            },
            updateRoom: function(room) {
                var deferred = $q.defer();

                $http.put(updateRoomUrl + room.id, room).then(function(result) {
                    deferred.resolve(result.data);
                }, function(result) {
                    deferred.reject(result);
                });

                return deferred.promise;
            }
        };
    });
