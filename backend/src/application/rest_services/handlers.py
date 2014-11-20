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
	#  If there is more than one room, then @rooms will be a list
	if len(rooms) == 0:
	 	pass
	else:
		rooms = [r.to_dict() for r in rooms]
	return rooms

# What data is coming in to change? Will the request data have
# all fields that should be on a room or just the ones its gonna update
def update_room(request_data, room_key):
	room_to_update = Room.get_by_id(room_key)

