//
//  EventTableViewController.swift
//  Capstone
//

import UIKit

class EventTableViewController: UITableViewController {

    @IBOutlet weak var roomTitleLabel: UINavigationItem!

    var date: NSDate? {
        didSet {
            // Make a call to get events for the given date
        }
    }

    var room: Room? {
        didSet {
            self.roomTitleLabel.title = self.room!.name
        }
    }

    var events = [Event]()

    override func viewDidLoad() {
        super.viewDidLoad()
        self.tableView.registerNib(UINib(nibName: "EventTableViewCell", bundle: nil), forCellReuseIdentifier: "eventCell")
        self.tableView.separatorStyle = UITableViewCellSeparatorStyle.None
        self.tableView.rowHeight = eventTableCellHeight
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete method implementation.
        // Return the number of rows in the section.
        return 2
    }

    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("eventCell", forIndexPath: indexPath) as EventTableViewCell

        cell.event = Event()

        return cell
    }

    func formatDateForRequest(date: NSDate) -> String {
        let formatter = NSDateFormatter()
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ssZZZ"
        return formatter.stringFromDate(date);
    }
}
