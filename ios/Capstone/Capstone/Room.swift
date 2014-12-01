//
//  Room.swift
//  Capstone
//
//  Created by Brian.Blanchard on 11/19/14.
//  Copyright (c) 2014 unknowndev. All rights reserved.
//

import UIKit

class Room: NSObject {
    var id: String
    var name: String
    var calendar: String
    var capacity: Int

    init(id: String, name: String, calendar: String, capacity: Int) {
        self.id = id
        self.name = name
        self.calendar = calendar
        self.capacity = capacity
    }
}
