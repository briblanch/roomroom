//
//  RoomViewController.swift
//  Capstone

import UIKit

class RoomViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    // Mark - Outlets
    @IBOutlet weak var backgroundImage: UIImageView!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var roomNameLabel: UILabel!

    // Mark - Instance vars
    var room: Room? {
        didSet {
            self.getEvents()
            roomNameLabel.text = room?.name
        }
    }
    private lazy var events = [Event]()
    private lazy var roomApi = RoomApiClient()
    private let defaults = NSUserDefaults.standardUserDefaults()
    private let cellIdentifier = "eventCell"
    private var date = NSDate()

    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.registerNib(UINib(nibName:"EventTableViewCell", bundle: nil), forCellReuseIdentifier: cellIdentifier)
        tableView.dataSource = self
        tableView.delegate = self
        tableView.separatorStyle = UITableViewCellSeparatorStyle.None
        tableView.rowHeight = EventTableCellHeight
    }

    override func viewWillAppear(animated: Bool) {
        if let defautRoomId = defaults.stringForKey(Constants.ROOM_KEY) {
            if (room == nil) {
                // Get the room by ID
                roomApi.getRooms {(rooms, eroor) in
                    if (rooms != nil) {
                        for room in rooms! {
                            if (room.id == defautRoomId) {
                                self.room = room
                                break
                            }
                        }
                    }
                }
            } else {
                self.getEvents()
            }
        } else {
            // Show modal so rooms can be selected
            showSettingsModal()
        }
    }

    override func prefersStatusBarHidden() -> Bool {
        return true
    }

    // MARK: - Helpers
    func showSettingsModal() {
        self.performSegueWithIdentifier("settingsSegue", sender: self)
    }

    func sortEventsByDate() {
        events.sort {(e1, e2) -> Bool in
            return e1.start < e2.start
        }
    }

    // MARK: - Table view data source
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier(cellIdentifier, forIndexPath: indexPath) as! EventTableViewCell

        cell.event = events[indexPath.row]

        cell.backgroundColor = UIColor.clearColor()

        return cell
    }

    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return events.count
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Data
    func getEvents() {
        roomApi.getEvents(forRoom: self.room!, forDate:date) { (events, error) in
            if (events != nil && error == nil) {
                self.events = events!
                self.sortEventsByDate()
            }

            self.tableView.reloadData()
        }
    }


    // MARK: - IBActions
    @IBAction func unwindToRoomView(segue: UIStoryboardSegue) {}
    @IBAction func settingsButtonWasTapped(sender: AnyObject) {
        showSettingsModal()
    }
}