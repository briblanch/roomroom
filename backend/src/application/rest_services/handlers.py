from oauth2client.client import SignedJwtAssertionCredentials
from apiclient.discovery import build
from httplib2 import Http

from application.models import Room

from datetime import datetime
from dateutil.parser import parse

import json

CLIENT_EMAIL = '152108792806-a3k1b8eso36ao5slv0atg7g7eaqa36ad@developer.gserviceaccount.com'
GOOGLE_CAL_API = 'https://www.googleapis.com/auth/calendar'
PRIVATE_KEY = open("keyStore.pem").read()

credentials = SignedJwtAssertionCredentials(CLIENT_EMAIL, PRIVATE_KEY, GOOGLE_CAL_API)
http_auth = credentials.authorize(Http())
cal_api = build('calendar', 'v3', http=http_auth)

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
def get_room_events(request_data, room_key, date_string):
	start_time = parse(date_string).replace(hour=0, minute=0, second=0)
	end_time = start_time.replace(hour=23, minute=59, second=29)

	start_time = start_time.isoformat()
	end_time = end_time.isoformat()

	room = Room.get_by_id(int(room_key))
	cal_id = room.calendar

	events = cal_api.events().list(calendarId=cal_id, timeMin=start_time, timeMax=end_time).execute()

	return events['items']
