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
            self.eventTimeLabel.text = "12:00 " + "-" + "1:00"
        }
    }

    @IBOutlet weak var eventTimeLabel: UILabel!
    @IBOutlet weak var eventTitleLabel: UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()
    }
}
