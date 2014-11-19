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
@app.route('/api/rooms/update/<room_key>', methods = ['POST'])
def update_room():
    handlers.update_room(request_data, room_key)