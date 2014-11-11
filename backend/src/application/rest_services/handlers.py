from application import models

def add_room(request_data):
	room_to_add = Room(room_id=request_data.id,
						name=request_data.name,
						capacity=request_data.capacity,
						calander=request_data.calander)

	room_to_add.put()
		