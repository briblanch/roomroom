from application.models import Room
from pprint import pprint
from httplib2 import Http
import json
import datetime

from oauth2client.client import SignedJwtAssertionCredentials
from apiclient.discovery import build

CLIENT_EMAIL = '152108792806-a3k1b8eso36ao5slv0atg7g7eaqa36ad@developer.gserviceaccount.com'
GOOGLE_CAL_API = 'https://www.googleapis.com/auth/calendar'
PRIVATE_KEY = open("keyStore.pem").read()

def add_room(request_data):
	room_to_add = Room(name=request_data['name'],
						capacity=request_data['capacity'],
						calendar=request_data['calendar'])

	if room_to_add:
		room_key = room_to_add.put()
		room_to_add = room_to_add.to_dict()
		room_to_add['id'] = room_key.id()
		return room_to_add

# Returns all Room entities in the datastore
def get_rooms():
	rooms = Room.query().fetch()

	if len(rooms) == 0:
		return []
	else:
		room_keys = [r.key for r in rooms]
		rooms = [r.to_dict() for r in rooms]
		for room, key in zip(rooms, room_keys):
			room['id'] = str(key.id())

	return rooms

# Updates the info of a room with the info in the request
def update_room(request_data, room_key):
	print "DATA: ", request_data
	print "KEY: ", room_key
	room_to_update = Room.get_by_id(int(room_key))
	room_to_update.name = request_data['name']
	room_to_update.capacity = request_data['capacity']
	room_to_update.calendar = request_data['calendar']
	room_to_update.put()

	room_key = room_to_update.key

	room_to_update = room_to_update.to_dict()
	room_to_update['id'] = str(room_key.id())
	return room_to_update

# Fetches all of the events from the google calendar api for a given room
def get_room_events(request_data, room_key):
	credentials = SignedJwtAssertionCredentials(CLIENT_EMAIL, PRIVATE_KEY, GOOGLE_CAL_API)
	http_auth = credentials.authorize(Http())
	cal_api = build('calendar', 'v3', http=http_auth)

	room = Room.get_by_id(int(room_key))
	cal_id = room.calendar
	
	time = datetime.date.today()
	day = time.day
	year = time.year
	timeMin='%s-%s-25T00:00:00Z' % (year, day)  
	timeMax='%s-%s-26T00:00:00Z' % (year, day)
	
	events_list = list()
	page_token = None
	while True:
    	events = cal_api.events().list(cal_id, pageToken=page_token, timeMin=timeMin, timeMax=timeMax).execute()
     	for event in events['items']:
             events_list.append(event['summary'])
     	page_token = events.get('nextPageToken')
     	if not page_token:
             break


