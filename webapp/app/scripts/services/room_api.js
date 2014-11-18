'use strict';

/**
 * @ngdoc service
 * @name capstoneApp.RoomApi
 * @description
 * # RoomApi
 * Factory in the capstoneApp.
 */
angular.module('capstone')
    .factory('RoomApi', function($http, $q) {
        var addRoomUrl = 'api/room';
        var getRoomsUrl = 'api/rooms'
        // Public API here
        return {
            addRoom : function(room) {
                var deferred = $q.defer();

                $http.post(addRoomUrl, room).then(function(result) {
                    deferred.resolve(result.data);
                });

                return deferred.promise;
            },
            getRooms: function() {
                var deferred = $q.defer();

                $http.get(getRoomsUrl).then(function(result) {
                    deferred.resolve(result.data);
                })

                return deferred.promise;
            }
        };
    });
