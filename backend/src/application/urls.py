"""
urls.py

URL dispatch route mappings and error handlers

"""
from flask import render_template
from flask import Flask, jsonify, abort, request, make_response, url_for
from application import app
from application import views
from application.rest_services import handlers


## URL dispatch rules
# App Engine warm up handler
# See http://code.google.com/appengine/docs/python/config/appconfig.html#Warming_Requests
app.add_url_rule('/_ah/warmup', 'warmup', view_func=views.warmup)

tasks = [
    {
        'id': 1,
        'title': u'Buy groceries',
        'description': u'Milk, Cheese, Pizza, Fruit, Tylenol',
        'done': False
    },
    {
        'id': 2,
        'title': u'Learn Python',
        'description': u'Need to find a good Python tutorial on the web',
        'done': False
    }
]

@app.route('/')
def root():
    return app.send_static_file('index.html')

@app.route('/todo', methods = ['GET'])
def get_tasks():
    return jsonify( { 'tasks': tasks } )

@app.route('/api/add/room', methods = ['POST'])
def add_room():
	handlers.add_room(request.get_json(force=True))
	return jsonify( { 'tasks': tasks } )


## Error handlers
# Handle 404 errors
@app.errorhandler(404)
def page_not_found(e):
    return render_template('404.html'), 404

# Handle 500 errors
@app.errorhandler(500)
def server_error(e):
    return render_template('500.html'), 500

