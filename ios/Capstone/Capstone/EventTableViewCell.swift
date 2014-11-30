//
//  EventTableViewCell.swift
//  Capstone
//
//  Created by Brian.Blanchard on 11/22/14.
//  Copyright (c) 2014 unknowndev. All rights reserved.
//

import UIKit

let eventTableCellHeight = CGFloat(80)

class EventTableViewCell: UITableViewCell {

    var event: Event? {
        didSet {
            self.eventTimeLabel.text = self.displayTimeForDate(self.event!.start) + " - " +
                                       self.displayTimeForDate(self.event!.end)
            
            self.eventTitleLabel.text = self.event!.summary
        }
    }

    @IBOutlet weak var eventTimeLabel: UILabel!
    @IBOutlet weak var eventTitleLabel: UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()
    }

    func displayTimeForDate(date: NSDate) -> String {
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "h:mm"
        dateFormatter.timeZone = NSTimeZone.systemTimeZone()
        return dateFormatter.stringFromDate(date)
    }
}
