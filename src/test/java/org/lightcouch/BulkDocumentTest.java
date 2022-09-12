/*
 * Copyright (C) 2011 lightcouch.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lightcouch;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.JsonObject;

@Ignore("Not a unit test! Runs agains a live database")
public class BulkDocumentTest {

	private static CouchDbClient dbClient;

	@BeforeClass
	public static void setUpClass() {
		dbClient = new CouchDbClient();
	}

	@AfterClass
	public static void tearDownClass() {
		dbClient.shutdown();
	}

	@Test
	public void bulkModifyDocs() {
		List<Object> newDocs = new ArrayList<Object>();
		newDocs.add(new Foo());
		newDocs.add(new JsonObject());

		List<Response> responses = dbClient.bulk(newDocs, true);
		
		assertThat(responses.size(), is(2));
	}

	@Test
	public void bulkDocsRetrieve() {
		Response r1 = dbClient.save(new Foo());
		Response r2 = dbClient.save(new Foo());
		
		List<String> keys = Arrays.asList(new String[] { r1.getId(), r2.getId() });
		
		List<Foo> docs = dbClient.view("_all_docs")
				.includeDocs(true)
				.keys(keys)
				.query(Foo.class);
		
		assertThat(docs.size(), is(2));
	}

}
