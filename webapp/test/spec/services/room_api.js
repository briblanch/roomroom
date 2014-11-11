'use strict';

describe('Service: RoomApi', function () {

  // load the service's module
  beforeEach(module('capstoneApp'));

  // instantiate service
  var RoomApi;
  beforeEach(inject(function (_RoomApi_) {
    RoomApi = _RoomApi_;
  }));

  it('should do something', function () {
    expect(!!RoomApi).toBe(true);
  });

});
