//
//  RoomApiClient.swift
//  Capstone
//
//  Created by Brian.Blanchard on 11/20/14.
//  Copyright (c) 2014 unknowndev. All rights reserved.
//

import Foundation

typealias roomListCompletion = ([Room]?, NSError?) -> Void
typealias eventListCompletion = ([Event]?, NSError?) -> Void

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

    func getEvents(forRoom room: Room, forDate date: NSDate, completion: eventListCompletion) {
        let URL = "/api/rooms/events/" + room.id
        let requestDate = Event.eventStringForRequest(date)

        HTTPRequestManager.sharedInstance.GET(URL, paramaters: ["date" : requestDate]) { (responseObject, error) in
            var events = [Event]()

            if let responseDict = responseObject as? Dictionary<String, AnyObject> {
                for event in responseDict["events"] as Array<Dictionary<String, AnyObject>> {
                    let start = event["start"] as Dictionary<String, AnyObject>
                    let end = event["end"] as Dictionary<String, AnyObject>

                    let summary = event["summary"] as String
                    let startDate = Event.parseDateString(start["dateTime"] as String)
                    let endDate = Event.parseDateString(end["dateTime"] as String)

                    events.append(Event(summary: summary, start: startDate!, end: endDate!))
                }

                completion(events, nil)
            } else {
                completion(nil, error)
            }
        }
    }
}
