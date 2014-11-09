'use strict';

/**
 * @ngdoc overview
 * @name capstone
 * @description
 * # capstone
 *
 * Main module of the application.
 */
angular
    .module('capstone', [
                'ngAnimate',
                'ngCookies',
                'ngResource',
                'ngRoute',
                'ngSanitize',
                'ngTouch'
    ])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/main.html',
                controller: 'MainCtrl'
            })
            .when('/about', {
                templateUrl: 'views/about.html',
                controller: 'AboutCtrl'
            })
            .when('/testRoute', {
              templateUrl: 'views/testroute.html',
              controller: 'TestrouteCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });
    });
