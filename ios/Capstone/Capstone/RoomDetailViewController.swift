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
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.roomTitle.title = room.name
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
