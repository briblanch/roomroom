'use strict';

describe('Controller: TestrouteCtrl', function () {

  // load the controller's module
  beforeEach(module('poopApp'));

  var TestrouteCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    TestrouteCtrl = $controller('TestrouteCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
