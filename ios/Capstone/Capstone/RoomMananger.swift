//
//  RoomMananger.swift
//  Capstone

import UIKit

class RoomMananger: NSObject {
    // MARK: - Instance variables
    var rooms = [Room]()
    private lazy var roomApi = RoomApiClient()

    // MARK: - Singleton
    class var sharedInstance: RoomMananger {
        struct Static {
            static var onceToken: dispatch_once_t = 0
            static var instance: RoomMananger? = nil
        }

        dispatch_once(&Static.onceToken) {
            Static.instance = RoomMananger()
        }

        return Static.instance!
    }

    func getRooms(completion: completionHandler) {

    }
}
