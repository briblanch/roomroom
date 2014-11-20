from application.models import Room
from pprint import pprint
import json

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

	print "LENGTH ", len(rooms)
	# If there is more than one room, then @rooms will be a list
	# otherwise, its a single object 
	if len(rooms) == 1:
	 	room_key = rooms.key
	 	rooms = rooms.to_dict()
	 	rooms['id'] = str(room_key.id())
	elif len(rooms) > 1:
		room_keys = [r.key for r in rooms]
		rooms = [r.to_dict() for r in rooms]
		for room, key in zip(rooms, room_keys):
			room['id'] = str(key.id())
	return rooms

# What data is coming in to change? Will the request data have
# all fields that should be on a room or just the ones its gonna update
def update_room(request_data, room_key):
	room_to_update = Room.get_by_id(room_key)
	room_to_update.name = request_data['name']
	room_to_update.name = request_data['capacity']
	room_to_update.name = request_data['calander']
	room_to_update.put()
	room_to_update = room_to_update.to_dict()
	return room_to_update

