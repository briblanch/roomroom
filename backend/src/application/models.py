"""
models.py

App Engine datastore models

"""

from google.appengine.ext import ndb


class ExampleModel(ndb.Model):
    """Example Model"""
    example_name = ndb.StringProperty(required=True)
    example_description = ndb.TextProperty(required=True)
    added_by = ndb.UserProperty()
    timestamp = ndb.DateTimeProperty(auto_now_add=True)

class Room(ndb.Model):
	name = ndb.StringProperty()
	capacity = ndb.IntegerProperty()
	calendar = ndb.StringProperty()
	type_id = ndb.StringProperty()
	amenities = ndb.StringProperty(repeated=True)
	location_id = ndb.StringProperty()