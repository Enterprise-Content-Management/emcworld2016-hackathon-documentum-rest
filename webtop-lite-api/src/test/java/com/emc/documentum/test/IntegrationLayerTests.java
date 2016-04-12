package com.emc.documentum.test;

import java.text.DateFormat;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.emc.documentum.RestSampleUser1Application;
import com.emc.documentum.dtos.DocumentumFolder;

/**
 * Unit Tests for Integration Layer
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestSampleUser1Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0", "management.port=0" })
@DirtiesContext
public abstract class IntegrationLayerTests {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${local.server.port}")
	private int port;

	protected abstract String getAPI();

	@Test
	public void testCabinetsRetrieval() throws Exception {
		DocumentumFolder[] cabinets = null;
		cabinets = retrieveCabinets();
		Assert.assertTrue(getAPI() + " Zero Cabinets Returned", cabinets.length > 0);

	}

	@Test
	public void testCabinetsType() {
		DocumentumFolder[] cabinets = null;
		cabinets = retrieveCabinets();
		for (DocumentumFolder folder : cabinets) {
			Assert.assertTrue(getAPI() + " : Cabinet Type not equal Cabinet", folder.getType().equals("Cabinet"));
		}

	}

	@Test
	public void testCreateThenDeleteFolder() {
		DocumentumFolder [] cabinets = retrieveCabinets();
		if(cabinets.length == 0 || cabinets == null){
			Assert.fail("No Cabinets Available");
		}
		String folderName = "MySampleCreatedFolder" + (Math.random() * 10000);
		ResponseEntity<DocumentumFolder> folderCreationResponse = createFolder(cabinets[0].getId(),folderName);
		Assert.assertTrue("Unable to Create Folder",folderCreationResponse.getStatusCode() == HttpStatus.OK);
		Assert.assertNotNull("Created Folder Not Equal Null", folderCreationResponse.getBody().getId());
		logger.info("Created Folder with Id:- " + folderCreationResponse.getBody().getId());
		ResponseEntity<Void> responseEntity = deleteObject(folderCreationResponse.getBody().getId());
		Assert.assertTrue("unable to delete Folder", responseEntity.getStatusCode() == HttpStatus.OK);
		logger.info("Delete response equal " + responseEntity.getStatusCode());
	}

	private DocumentumFolder[] retrieveCabinets() {
		ResponseEntity<DocumentumFolder[]> entity = new TestRestTemplate().getForEntity(
				"http://localhost:" + this.port + "/" + getAPI() + "/services/get/cabinets", DocumentumFolder[].class);
		return entity.getBody();
	}

	private ResponseEntity<DocumentumFolder> createFolder(String cabinetName, String folderName) {
		ResponseEntity<DocumentumFolder> entity = new TestRestTemplate().exchange("http://localhost:" + this.port
				+ "/" + getAPI() + "/services/folder/create/" + cabinetName + "/" + folderName,HttpMethod.POST,null, DocumentumFolder.class);
		return entity;
	}

	private ResponseEntity<Void> deleteObject(String objectId) {
		ResponseEntity<Void> entity = new TestRestTemplate().exchange(
				"http://localhost:" + this.port + "/" + getAPI() + "/services/delete/object/id/" + objectId, HttpMethod.DELETE,
				null, Void.class);
		return entity;
	}
}
