'use strict';

/**
 * @ngdoc overview
 * @name roomroom
 * @description
 * # roomroom
 *
 * Main module of the application.
 */
angular
    .module('roomroom', [
                'ngAnimate',
                'ngCookies',
                'ngResource',
                'ngRoute',
                'ngSanitize',
                'ngTouch'
    ])
    .config(function ($routeProvider, $locationProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/main.html',
                controller: 'MainCtrl'
            })
            .when('/about', {
                templateUrl: 'views/about.html',
                controller: 'AboutCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });

        $locationProvider.html5Mode(true);
    });
