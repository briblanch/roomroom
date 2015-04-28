//
//  RoomViewController.swift
//  Capstone

import UIKit

class RoomViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    // Mark - Outlets
    @IBOutlet weak var currentMeetingLabel: UILabel!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var roomNameLabel: UILabel!
    @IBOutlet weak var currentMeetingTimeLabel: UILabel!
    @IBOutlet weak var currentTimeLabel: UILabel!
    @IBOutlet weak var currentDate: UILabel!
    @IBOutlet weak var inUseLabel: UILabel!
    @IBOutlet weak var backgroundImage: UIImageView!

    // Mark - Timers
    private var secondTimer = NSTimer()
    private var minuteTimer = NSTimer()

    private var currentEvent: Event? {
        didSet {
            if (self.currentEvent != nil) {
                self.currentMeetingTimeLabel.text = Event.displayTimeForDate(self.currentEvent!.start) + " - " +
                        Event.displayTimeForDate(self.currentEvent!.end)

                self.currentMeetingLabel.text = self.currentEvent!.summary

                self.inUseLabel.text = "In Use"
                self.currentMeetingTimeLabel.hidden = false
                self.currentMeetingLabel.hidden = false
            } else {
                self.inUseLabel.text = "Avaliable"
                self.currentMeetingTimeLabel.hidden = true
                self.currentMeetingLabel.hidden = true
            }
        }
    }

    // Mark - Instance vars
    var room: Room? {
        didSet {
            self.getEvents()
            roomNameLabel.text = room?.name
        }
    }

    private lazy var events = [Event]()
    private lazy var filteredEvents = [Event]()
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

        NSNotificationCenter.defaultCenter().addObserver(self, selector: "getEvents",
            name: UIApplicationSignificantTimeChangeNotification, object: nil)

        self.setTimeLabel()
        self.setDayLabel()

        self.secondTimer = NSTimer.scheduledTimerWithTimeInterval(1, target: self, selector: "everySecond", userInfo: nil, repeats: true)
        self.minuteTimer = NSTimer.scheduledTimerWithTimeInterval(60, target: self, selector: "everyMinute", userInfo: nil, repeats: true)
        self.backgroundImage.image = nil
        self.inUseLabel.hidden = true
    }

    override func viewDidAppear(animated: Bool) {
        if let defautRoomId = defaults.stringForKey(Constants.ROOM_KEY) {
            if (room == nil) {
                // Get the room by ID
                roomApi.getRooms {(rooms, eroor) in
                    if (rooms != nil) {
                        for room in rooms! {
                            if (room.id == defautRoomId) {
                                self.room = room
                                self.setBackgroundImage()
                                break
                            }
                        }
                    }
                }
            } else {
                self.getEvents()
            }

            self.setBackgroundImage()
            self.inUseLabel.hidden = false
        } else {
            // Show modal so rooms can be selected
           self.showSettingsModal()
        }
    }

    override func prefersStatusBarHidden() -> Bool {
        return true
    }

    func setBackgroundImage() {
        if (self.room != nil) {
            if (self.room!.name == "Sedona") {
                self.backgroundImage.image = UIImage(named: "Sedona")
            } else {
                self.backgroundImage.image = UIImage(named: "beach")
            }
        }
    }

    // MARK: - Helpers
    func showSettingsModal() {
        self.performSegueWithIdentifier("settingsSegue", sender: self)
    }

    func sortEventsByDate() {
        self.filteredEvents.sort {$0.start < $1.start}
    }

    func filterEvents() {
        let now = NSDate()
        self.filteredEvents = self.events.filter {$0.start > now }
    }

    func setCurrentEvent() {
        let now = NSDate()
        for event in self.events {
            if (event.start <= now && event.end >= now) {
                self.currentEvent = event
                return
            }
        }

        self.currentEvent = nil
    }

    func setTimeLabel() {
        let today = NSDate()
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "h:mm:ss";
        self.currentTimeLabel.text = dateFormatter.stringFromDate(today)
    }

    func setDayLabel() {
        let today = NSDate()
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "EEE, MMMM d, yyyy"
        self.currentDate.text = dateFormatter.stringFromDate(today)
    }

    // MARK: - Table view data source
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier(cellIdentifier, forIndexPath: indexPath) as! EventTableViewCell

        cell.event = self.filteredEvents[indexPath.row]
        cell.backgroundColor = UIColor.clearColor()

        return cell
    }

    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.filteredEvents.count
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
                self.setCurrentEvent()
                self.filterEvents()
                self.sortEventsByDate()
            }

            self.tableView.reloadData()
        }
    }


    // MARK: - IBActions
    @IBAction func unwindToRoomView(segue: UIStoryboardSegue) {
        self.setBackgroundImage()
        self.inUseLabel.hidden = false
    }
    @IBAction func settingsButtonWasTapped(sender: AnyObject) {
        showSettingsModal()
    }

    // MARK: - Timer methods
    func everySecond() {
        self.setTimeLabel()
        self.setCurrentEvent()
    }

    func everyMinute() {
        self.getEvents()
    }

    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
        self.secondTimer.invalidate()
        self.minuteTimer.invalidate()
    }
}