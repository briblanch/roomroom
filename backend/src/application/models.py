"""
models.py

App Engine datastore models

"""

from google.appengine.ext import ndb

class Room(ndb.Model):
	name = ndb.StringProperty()
	capacity = ndb.IntegerProperty()
	calendar = ndb.StringProperty()
	type_id = ndb.StringProperty()
	amenities = ndb.StringProperty(repeated=True)
	location_id = ndb.StringProperty()