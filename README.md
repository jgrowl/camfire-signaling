camfire-signaling
=================

A Scala Websocket-based WebRTC Signaling Server

# Description

The purpose of this project is to pull out the user end point into a separate project so the media server can be scaled.
The main idea here is that there can be multiple load-balanced signaling server end points. Users will establish a 
websocket connection with one of them. Each signalling server can communicate with each other through a redis broadcaster.

The signaling server will ideally communicate to a separate clustered stream server that will actually manage WebRTC
objects.

