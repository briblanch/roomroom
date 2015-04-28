//
//  EventTableViewCell.swift
//  Capstone
//
//  Created by Brian.Blanchard on 11/22/14.
//  Copyright (c) 2014 unknowndev. All rights reserved.
//

import UIKit

let EventTableCellHeight = CGFloat(80)

class EventTableViewCell: UITableViewCell {

    var event: Event? {
        didSet {
            self.eventTimeLabel.text = Event.displayTimeForDate(self.event!.start) + " - " +
                                       Event.displayTimeForDate(self.event!.end)
            
            self.eventTitleLabel.text = self.event!.summary
        }
    }

    @IBOutlet weak var eventTimeLabel: UILabel!
    @IBOutlet weak var eventTitleLabel: UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()
    }
}
