from application.models import Room
from pprint import pprint
import json

def add_room(request_data):
	room_to_add = Room(name=request_data['name'],
						capacity=request_data['capacity'],
						calendar=request_data['calendar'])

	if (room_to_add):
		room_to_add.put()
		return room_to_add.to_dict()
