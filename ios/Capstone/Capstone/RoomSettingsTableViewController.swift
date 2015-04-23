//
//  RoomSettingsTableViewController.swift
//  Capstone

import UIKit

class RoomSettingsTableViewController: UITableViewController {

    // MARK: - Instance vars
    private lazy var roomApi = RoomApiClient()
    private let cellIdentifer = "roomCell"
    private let defaults = NSUserDefaults.standardUserDefaults()
    private var rooms = [Room]()
    private var defaultRoomId: String? {
        didSet {
            defaults.setObject(defaultRoomId, forKey: Constants.ROOM_KEY)
        }
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        if let storedRoomId = defaults.stringForKey(Constants.ROOM_KEY) {
            defaultRoomId = storedRoomId
        }

        roomApi.getRooms { (remoteRooms, error) in
            if (remoteRooms != nil) {
                self.rooms = remoteRooms!
            } else {
                // error
            }

            self.tableView.reloadData()
        }
    }

    // MARK: - Helpers
    func roomForId(id: String) -> Room? {
        for room in rooms {
            if (room.id == id) {
                return room
            }
        }

        return nil
    }

    func closeModal() {
        performSegueWithIdentifier("unwindSettings", sender: self)
    }

    // MARK: - Table view data source
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return rooms.count
    }

    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier(cellIdentifer, forIndexPath: indexPath) as! UITableViewCell

        if (cell.accessoryType == UITableViewCellAccessoryType.Checkmark) {
            cell.accessoryType = UITableViewCellAccessoryType.None
        }

        let room = rooms[indexPath.row]
        cell.textLabel?.text = room.name

        if (room.id == defaultRoomId) {
            cell.accessoryType = UITableViewCellAccessoryType.Checkmark
        }

        return cell
    }

    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        defaultRoomId = rooms[indexPath.row].id
        tableView.reloadData()
        closeModal()
    }

    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        let roomVC = segue.destinationViewController as? RoomViewController
        roomVC!.room = roomForId(defaultRoomId!)
    }
}
