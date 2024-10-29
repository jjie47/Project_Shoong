package com.example.demo.controller;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint(value = "/invite/{userId}")
public class InviteController {
	private static Set<Session> clients = Collections.synchronizedSet(new HashSet<>());
	private static Map<String, Session> clients_session = Collections.synchronizedMap(new HashMap<>()); 
	private static Map<Session, String> clients_id = Collections.synchronizedMap(new HashMap<>());
	@OnMessage
	public void onMessage(String message, Session session, @PathParam(value="userId")String userId) throws IOException {
		String[] arr = message.split(":");
		String sender = arr[0];
		if(sender.equals("invite")) {
			String inviteUserId = arr[2];
			synchronized (clients) {
				for(Session client : clients) {
					if(client.equals(clients_session.get(inviteUserId))) {
						// socket_invite.send(`${planId}:${inviteUserId}`);
						System.out.println("Send '"+message+"' From "+ userId +" To "+inviteUserId);
						client.getBasicRemote().sendText(message);
					}
				}
			}
		}
		else if(sender.equals("accept")||sender.equals("refuse")) {
			String members = arr[2];
			String[] membersId = members.split(",");
			List<String> list = Arrays.asList(membersId);
			synchronized (clients) {
				for(Session client : clients) {
					if(list.contains(clients_id.get(client))&&!userId.equals(clients_id.get(client))) {
						// socket_invite.send(`${planId}:${inviteUserId}`);
						System.out.println("Send '"+message+"' From "+ userId +" To "+clients_id.get(client));
						client.getBasicRemote().sendText(message);
					}
				}
			}
		}
	}
	
	@OnOpen
	public void onOpen(Session session, @PathParam(value="userId")String userId) throws IOException {
		System.out.println("접속 : "+session+" / 아이디 : "+userId);
//		synchronized (clients) {
//			for(Session client : clients) {
//				
//			}
//		}
		
		clients.add(session);
		clients_session.put(userId, session);
		clients_id.put(session, userId);
	}
	
	@OnClose
	public void onClose(Session session, @PathParam(value="userId")String userId) throws IOException {
		System.out.println("접속종료 : "+session+" / 아이디 : "+userId);
		synchronized (clients) {
			for(Session client : clients) {
				
			}
		}
	}
}

