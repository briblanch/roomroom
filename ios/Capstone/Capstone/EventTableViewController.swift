//
//  EventTableViewController.swift
//  Capstone
//

import UIKit

class EventTableViewController: UITableViewController {

    @IBOutlet weak var roomTitleLabel: UINavigationItem!
    @IBOutlet weak var datePicker: UIDatePicker!
    @IBOutlet weak var dateButton: UIBarButtonItem!

    let cellIdentifer = "eventCell"

    var secondTimer = NSTimer()
    var minuteTimer = NSTimer()

    lazy var roomApi = RoomApiClient()

    var date: NSDate? {
        didSet {
            self.dateButton.title = displayDateText(self.date!)
        }
    }

    var room: Room? {
        didSet {
            self.roomTitleLabel.title = self.room!.name + " - " + self.room!.roomUsed
        }
    }

    var events = [Event]()

    required init(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        self.tableView.registerNib(UINib(nibName: "EventTableViewCell", bundle: nil), forCellReuseIdentifier: cellIdentifer)
        self.tableView.separatorStyle = UITableViewCellSeparatorStyle.None
        self.tableView.rowHeight = EventTableCellHeight

        self.datePicker.datePickerMode = UIDatePickerMode.Date
        self.datePicker.hidden = true
        self.datePicker.date = self.date!

        NSNotificationCenter.defaultCenter().addObserver(self, selector: "dateHasChanged:",
            name: UIApplicationSignificantTimeChangeNotification, object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "appResumed",
            name: UIApplicationDidBecomeActiveNotification, object: nil)


//        self.minuteTimer = NSTimer.scheduledTimerWithTimeInterval(30, target: self, selector: "getEvents", userInfo: nil, repeats: true)
        self.secondTimer = NSTimer.scheduledTimerWithTimeInterval(1, target: self, selector: "everySecond", userInfo: nil, repeats: true)

        self.getEvents()
        self.getRoomStatus()
    }

    override func viewWillDisappear(animated: Bool) {
        self.minuteTimer.invalidate()
        self.secondTimer.invalidate()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // Return the number of rows in the section.
        return events.count
    }

    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier(cellIdentifer, forIndexPath: indexPath) as! EventTableViewCell

        if (self.events.count > 0) {
            cell.event = events[indexPath.row]
        }

        return cell
    }

    func getEvents() {
        roomApi.getEvents(forRoom: self.room!, forDate: self.date!) { (events, error) in
            if (events != nil && error == nil) {
                self.events = events!
                self.filterExpiredEvents()
                self.sortEventsByDate()
            } else {
                // error
            }

            self.tableView.reloadData()
        }
    }
    
    func getRoomStatus() {
        roomApi.getRoomStatus(forRoom: self.room!)
    }

    // MARK: - Event Helpers
    func filterExpiredEvents() -> Bool {
        let startCount = self.events.count;
        let referenceDate = NSDate()

        self.events = self.events.filter {$0.end > referenceDate}

        return startCount > self.events.count
    }

    func sortEventsByDate() {
        self.events.sort {(e1, e2) -> Bool in
            return e1.start < e2.start
        }
    }

    @IBAction func dateWasSelected(sender: UIDatePicker) {
        self.date = sender.date
        self.getEvents()
    }

    @IBAction func dateButtonWasPressed(sender: AnyObject) {
        self.datePicker.hidden = !self.datePicker.hidden
    }

    func displayDateText(date: NSDate) -> String {
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "MMM d, YYYY"
        dateFormatter.timeZone = NSTimeZone.systemTimeZone()
        return dateFormatter.stringFromDate(date)
    }

    // MARK: - Events
    func everySecond() {
        if (self.filterExpiredEvents()) {
            self.tableView.reloadData()
        }
        // TODO: Update time label when implemented
    }

    func dateHasChanged(notifcation: NSNotification) {
        self.date = NSDate()
        self.datePicker.date = self.date!
        self.getEvents()
    }

    func appResumed() {
        self.date = NSDate()
        self.getEvents()
    }

    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
}
