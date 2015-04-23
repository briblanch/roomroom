//
//  DateHelpers.swift
//  Capstone
//

import Foundation

extension NSDate: Comparable {

}

public func <=(lhs: NSDate, rhs: NSDate) -> Bool {
    return lhs == rhs || lhs < rhs
}

public func >=(lhs: NSDate, rhs: NSDate) -> Bool {
    return lhs == rhs || lhs > rhs
}

public func >(lhs: NSDate, rhs: NSDate) -> Bool {
    let result = lhs.compare(rhs)
    return result == NSComparisonResult.OrderedDescending
}

public func <(lhs: NSDate, rhs: NSDate) -> Bool {
    let result = lhs.compare(rhs)
    return result == NSComparisonResult.OrderedAscending
}

public func ==(lhs: NSDate, rhs: NSDate) -> Bool {
    return lhs.isEqualToDate(rhs)
}
