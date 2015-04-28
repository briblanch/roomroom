//
//  Room.swift
//  Capstone
//
//  Created by Brian.Blanchard on 11/19/14.
//  Copyright (c) 2014 unknowndev. All rights reserved.
//

import UIKit

class Room: NSObject, Equatable {
    var id: String
    var name: String
    var calendar: String
    var capacity: Int
    var roomUsed: String
    var motionIp: String?

    init(id: String, name: String, calendar: String, capacity: Int, roomUsed: String) {
        self.id = id
        self.name = name
        self.calendar = calendar
        self.capacity = capacity
        self.roomUsed = roomUsed
    }

    init(id: String, name: String, calendar: String, capacity: Int, roomUsed: String, motionIp: String?) {
        self.id = id
        self.name = name
        self.calendar = calendar
        self.capacity = capacity
        self.roomUsed = roomUsed
        self.motionIp = motionIp!
    }

    override func isEqual(object: AnyObject?) -> Bool {
        return name == object?.name
    }
}

func ==(lhs: Room, rhs: Room) -> Bool {
    return lhs.id == rhs.id
}
