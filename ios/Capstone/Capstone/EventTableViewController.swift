//
//  EventTableViewController.swift
//  Capstone
//

import UIKit

class EventTableViewController: UITableViewController {

    @IBOutlet weak var roomTitleLabel: UINavigationItem!
    @IBOutlet weak var datePicker: UIDatePicker!
    @IBOutlet weak var dateButton: UIBarButtonItem!

    lazy var roomApi = RoomApiClient()

    var date: NSDate? {
        didSet {
            self.dateButton.title = displayDateText(self.date!)
        }
    }

    var room: Room? {
        didSet {
            self.roomTitleLabel.title = self.room!.name
        }
    }

    var events: [Event] = [Event]() {
        didSet {
            self.tableView.reloadData()
        }
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        self.tableView.registerNib(UINib(nibName: "EventTableViewCell", bundle: nil), forCellReuseIdentifier: "eventCell")
        self.tableView.separatorStyle = UITableViewCellSeparatorStyle.None
        self.tableView.rowHeight = eventTableCellHeight

        self.datePicker.datePickerMode = UIDatePickerMode.Date
        self.datePicker.hidden = true

        self.getEvents()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete method implementation.
        // Return the number of rows in the section.
        return events.count
    }

    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("eventCell", forIndexPath: indexPath) as EventTableViewCell

        cell.event = events[indexPath.row]

        return cell
    }

    func getEvents() {
        roomApi.getEvents(forRoom: self.room!, forDate: self.date!) { (events, error) in
            if (events != nil && error == nil) {
                self.events = events!
            } else {
                // error
            }
        }
    }

    @IBAction func dateWasSelected(sender: UIDatePicker) {
        self.date = sender.date
        self.getEvents()
        self.datePicker.hidden = true
    }

    @IBAction func dateButtonWasPressed(sender: AnyObject) {
        self.datePicker.hidden = false
    }

    func displayDateText(date: NSDate) -> String {
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "MMM d, YYYY"
        dateFormatter.timeZone = NSTimeZone.systemTimeZone()
        return dateFormatter.stringFromDate(date)
    }
}
