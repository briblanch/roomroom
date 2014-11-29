//
//  RoomTableViewController.swift
//  Capstone
//
//  Created by Brian.Blanchard on 11/19/14.
//  Copyright (c) 2014 unknowndev. All rights reserved.
//

import UIKit

class RoomTableViewController: UITableViewController {

    lazy var rooms = [Room]()
    lazy var roomApi = RoomApiClient()
    private var cellIdentifier = "roomCell"

    override func viewDidLoad() {
        super.viewDidLoad()

        self.getRooms()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }

    // MARK: - Table view data source

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.rooms.count
    }

    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier(self.cellIdentifier, forIndexPath: indexPath) as UITableViewCell

        cell.textLabel.text = self.rooms[indexPath.row].name
        
        return cell
    }

    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        self.performSegueWithIdentifier("roomEventSegue", sender: self)
    }

    @IBAction func refreshButtonWasPresssed(sender: AnyObject) {
        self.getRooms()
    }

    private func getRooms() {
        roomApi.getRooms { (rooms, error) in
            if let remoteRooms = rooms {
                self.rooms = remoteRooms
                self.rooms.sort { $0.name < $1.name }
                self.tableView.reloadData()
            } else {
                self.rooms = []
            }
        }
    }

    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        let destinationVc = segue.destinationViewController as EventTableViewController
        let index = self.tableView.indexPathForSelectedRow()
        
        destinationVc.room = self.rooms[index!.row]
        destinationVc.date = NSDate()
    }

}
