"""
urls.py

URL dispatch route mappings and error handlers

"""
from flask import render_template
from flask import Flask, jsonify, abort, request, make_response, url_for
from application import app
from application import views
from application.rest_services import handlers
import json


## URL dispatch rules
# App Engine warm up handler
# See http://code.google.com/appengine/docs/python/config/appconfig.html#Warming_Requests
app.add_url_rule('/_ah/warmup', 'warmup', view_func=views.warmup)

# Home page, which is where you can add a room
@app.route('/')
def root():
    return app.send_static_file('index.html')

# TODO: change this url to something else
# Adds a room
@app.route('/api/room', methods = ['POST'])
def add_room():
    new_room = handlers.add_room(request.get_json())
    return jsonify( { 'room': new_room } )

# Returns a list of all rooms in the datastore
@app.route('/api/rooms/', methods = ['GET'])
def get_rooms():
    rooms = handlers.get_rooms()
    return jsonify({'rooms': rooms})

# Update a room's info
@app.route('/api/rooms/<room_key>', methods = ['PUT'])
def update_room(room_key):
    updated_room = handlers.update_room(request.get_json(), room_key)
    return jsonify({'room': updated_room})
