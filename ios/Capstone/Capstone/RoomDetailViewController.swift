//
//  RoomDetailViewController.swift
//  Capstone
//
//  Created by Brian.Blanchard on 11/20/14.
//  Copyright (c) 2014 unknowndev. All rights reserved.
//

import UIKit

class RoomDetailViewController: UIViewController {
    var room: Room!

    @IBOutlet weak var roomTitle: UINavigationItem!
    @IBOutlet weak var roomName: UILabel!
    @IBOutlet weak var roomID: UILabel!
    @IBOutlet weak var roomCalendar: UILabel!
    @IBOutlet weak var roomCapacity: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.displayRoomInfo()
    }

    func displayRoomInfo() {
        self.roomTitle.title = room.name

        self.roomName.text = "Name: " + room.name
        self.roomID.text = "ID: " + room.id
        self.roomCalendar.text = "Calendar: " + room.calendar
        self.roomCapacity.text = "Capacity: " + String(room.capacity)
    }
}
