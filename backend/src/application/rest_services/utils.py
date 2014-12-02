import datetime

from httplib2 import Http
from oauth2client.client import SignedJwtAssertionCredentials
from apiclient.discovery import build

CLIENT_EMAIL = '152108792806-a3k1b8eso36ao5slv0atg7g7eaqa36ad@developer.gserviceaccount.com'
GOOGLE_CAL_API = 'https://www.googleapis.com/auth/calendar'
PRIVATE_KEY = open("keyStore.pem").read()

def get_calendar_api_obj():
	credentials = SignedJwtAssertionCredentials(CLIENT_EMAIL, PRIVATE_KEY, GOOGLE_CAL_API)
	http_auth = credentials.authorize(Http())
	return build('calendar', 'v3', http=http_auth)


def event_time_restrictions():
	time = datetime.date.today()
	day = time.day
	year = time.year
	timeMin='%s-%s-25T00:00:00Z' % (year, day)  
	timeMax='%s-%s-26T00:00:00Z' % (year, day)
	return timeMin, timeMax