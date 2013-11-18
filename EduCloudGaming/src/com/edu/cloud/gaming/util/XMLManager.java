package com.edu.cloud.gaming.util;

import java.io.File;
import java.util.ArrayList;

import com.edu.cloud.gaming.dto.EduCloudGamingDTO;
import com.edu.cloud.gaming.dto.GameDTO;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XMLManager {
	
	public XStream initXStream() {
		
		XStream xStream = new XStream(new DomDriver());
		
		xStream.alias("eduCloudGaming", EduCloudGamingDTO.class);
		xStream.alias("game", GameDTO.class);
		
		return xStream;
	}
	
	public EduCloudGamingDTO convertXMLtoEduCloudGamingDTO(String xmlCommunicationPath) {
		
		XStream xStream = this.initXStream();
		
		EduCloudGamingDTO eduCloudGamingDTO = (EduCloudGamingDTO) xStream.fromXML(new File(xmlCommunicationPath));
		
		return eduCloudGamingDTO;
	}
	
	public String convertEduCloudGamingDTOtoXML(EduCloudGamingDTO eduCloudGamingDTO) {
		
		XStream xStream = this.initXStream();
		
		String xml = xStream.toXML(eduCloudGamingDTO);
		
		return xml;
	}
	
	public EduCloudGamingDTO createTestEdu() {
		
		EduCloudGamingDTO eduCloudGamingDTO = new EduCloudGamingDTO();
		ArrayList<GameDTO> gameDTOs = new ArrayList<GameDTO>();
		
		GameDTO gameDTO = new GameDTO();
		gameDTO.setName("Teste1");
		gameDTO.setCommunicationClass("Testec");
		
		gameDTOs.add(gameDTO);
		
		gameDTO = new GameDTO();
		gameDTO.setName("Teste2");
		gameDTO.setCommunicationClass("Testecc");
		
		gameDTOs.add(gameDTO);
		
		eduCloudGamingDTO.setGame(gameDTOs);
		
		return eduCloudGamingDTO;
	}
	
//	public static void main(String[] args) {
//		
//		XMLManager xmlManager = new XMLManager();
//		
//		xmlManager.convertXMLtoEduCloudGamingDTO();
//		
//		xmlManager.convertEduCloudGamingDTOtoXML(xmlManager.createTestEdu());
//	}
}
