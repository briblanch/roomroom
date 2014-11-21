//
//  RoomApiClient.swift
//  Capstone
//
//  Created by Brian.Blanchard on 11/20/14.
//  Copyright (c) 2014 unknowndev. All rights reserved.
//

import Foundation

typealias roomListCompletion = ([Room]?, NSError?) -> Void

class RoomApiClient: NSObject {
    func getRooms(completion: roomListCompletion) {
        let URL = "/api/rooms"
    
        HTTPRequestManager.sharedInstance.GET(URL, paramaters: nil) { (responseObject, error) in
            var rooms = [Room]()

            if let responseDict = responseObject as? Dictionary<String, AnyObject> {
                for room in responseDict["rooms"] as Array<Dictionary<String, AnyObject>> {
                    rooms.append(Room(id: room["id"] as String,
                                    name: room["name"] as String,
                                calendar: room["calendar"] as String,
                                capacity: room["capacity"] as Int))
                }
                
                completion(rooms, nil)
            } else {
                completion(nil, error)
            }
        }
    }
}
