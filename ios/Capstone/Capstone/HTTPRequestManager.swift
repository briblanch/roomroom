//
//  HTTPRequestManager.swift
//  Capstone
//
//  Created by Brian.Blanchard on 11/20/14.
//  Copyright (c) 2014 unknowndev. All rights reserved.
//

import Foundation
import Alamofire

typealias completionHandler = (AnyObject?, NSError?) -> Void

class HTTPRequestManager: NSObject {

    var baseURL: String? = "http://localhost:8080"

    class var sharedInstance : HTTPRequestManager {
        struct Static {
            static var onceToken : dispatch_once_t = 0
            static var instance : HTTPRequestManager? = nil
        }

        dispatch_once(&Static.onceToken) {
            Static.instance = HTTPRequestManager()
        }

        return Static.instance!
    }

    init(baseURL URL: String?) {
        if let baseURL = URL {
            self.baseURL = baseURL
        }
    }

    convenience override init() {
        self.init(baseURL: nil)
    }

    func GET(URL: String, paramaters: [String: AnyObject]?, completion: completionHandler?) {
        if let requestURL = self.requestURL(URL) {
            Alamofire.request(.GET, requestURL, parameters: paramaters)
                .responseJSON { (_, _, JSON, error) in
                    if (completion != nil) {
                        completion!(JSON, error)
                    }
            }
        }
    }

    private func requestURL(URL: String) -> String? {
        if let requestURL = NSURL(string: URL, relativeToURL: NSURL(string: self.baseURL!)) {
            return requestURL.absoluteString!
        } else {
            return nil
        }
    }
}
