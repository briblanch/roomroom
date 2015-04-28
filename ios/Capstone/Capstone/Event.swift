//
//  Event.swift
//  Capstone
//
//  Created by Brian.Blanchard on 11/22/14.
//  Copyright (c) 2014 unknowndev. All rights reserved.
//

import UIKit

class Event: NSObject, NSCopying {
    var summary: String
    var start: NSDate
    var end: NSDate
    var creator: String

    init(summary: String, start: NSDate, end: NSDate, creator: String) {
        self.summary = summary
        self.start = start
        self.end = end
        self.creator = creator
    }

    func copyWithZone(zone: NSZone) -> AnyObject {
        let event = Event(summary: self.summary, start:self.start, end: self.end, creator: self.creator)
        return event
    }

    class func parseDateString(dateString: String) -> NSDate? {
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ssZZZ"
        dateFormatter.timeZone = NSTimeZone(abbreviation: "UTC")
        return dateFormatter.dateFromString(dateString)
    }

    class func eventStringForRequest(date: NSDate) -> String {
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ssZZZ"
        return dateFormatter.stringFromDate(date)
    }

    class func displayTimeForDate(date: NSDate) -> String {
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "h:mm a"
        dateFormatter.timeZone = NSTimeZone.systemTimeZone()
        return dateFormatter.stringFromDate(date)
    }
}
