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

import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class URIBuilderTest {
    private static final String SCHEME = "http";
    private static final String HOST = "1.1.1.1";
    private static final int PORT = 5984;

    private static final String STRING_WITH_SLASH = "light/couch";
    private static final String STRING_WITH_HYPHEN = "light-couch";
    private static final String STRING_WITH_PLUS = "light+couch";
    private static final String STRING_WITH_AMPERSAND = "light&couch";

    private static final String STRING_WITH_SLASH_ENCODED = "light%2Fcouch";
    private static final String STRING_WITH_PLUS_ENCODED_FOR_QUERY = "light%2Bcouch";
    private static final String STRING_WITH_AMPERSAND_ENCODED_FOR_QUERY = "light%26couch";

    @Test
    public void schemeAndHostTest() {
        URI uri = new URIBuilder()
            .scheme(SCHEME)
            .host(HOST)
            .build();
        assertEquals(SCHEME + "://" + HOST, uri.toString());
    }

    @Test
    public void schemeHostAndPortTest() {
        URI uri = new URIBuilder()
            .scheme(SCHEME)
            .host(HOST)
            .port(PORT)
            .build();
        assertEquals(SCHEME + "://" + HOST + ":" + PORT, uri.toString());
    }

    @Test
    public void schemeAuthorityAndSimplePathTest() {
        URI uri = new URIBuilder()
            .scheme(SCHEME)
            .host(HOST)
            .port(PORT)
            .pathSegment(STRING_WITH_HYPHEN)
            .build();
        assertEquals(SCHEME + "://" + HOST + ":" + PORT + "/" + STRING_WITH_HYPHEN, uri.toString());
    }

    @Test
    public void schemeAuthorityAndCompoundPathTest() {
        URI uri = new URIBuilder()
            .scheme(SCHEME)
            .host(HOST)
            .port(PORT)
            .pathSegment(STRING_WITH_HYPHEN)
            .pathSegment(STRING_WITH_PLUS)
            .pathSegment(STRING_WITH_SLASH)
            .build();
        String expected = SCHEME + "://" + HOST + ":" + PORT
            + "/" + STRING_WITH_HYPHEN
            + "/" + STRING_WITH_PLUS
            + "/" + STRING_WITH_SLASH_ENCODED;
        assertEquals(expected, uri.toString());
    }

    @Test
    public void schemeAuthorityPathAndSingleParamQueryTest() {
        URI uri = new URIBuilder()
            .scheme(SCHEME)
            .host(HOST)
            .port(PORT)
            .pathSegment(STRING_WITH_HYPHEN)
            .pathSegment(STRING_WITH_PLUS)
            .pathSegment(STRING_WITH_SLASH)
            .query(STRING_WITH_HYPHEN, STRING_WITH_PLUS)
            .build();
        String expected = SCHEME + "://" + HOST + ":" + PORT
            + "/" + STRING_WITH_HYPHEN
            + "/" + STRING_WITH_PLUS
            + "/" + STRING_WITH_SLASH_ENCODED
            + "?" + STRING_WITH_HYPHEN + "=" + STRING_WITH_PLUS_ENCODED_FOR_QUERY;
        assertEquals(expected, uri.toString());
    }

    @Test
    public void schemeAuthorityPathAndCompoundParamQueryTest() {
        URI uri = new URIBuilder()
            .scheme(SCHEME)
            .host(HOST)
            .port(PORT)
            .pathSegment(STRING_WITH_HYPHEN)
            .pathSegment(STRING_WITH_PLUS)
            .pathSegment(STRING_WITH_SLASH)
            .query(STRING_WITH_HYPHEN, STRING_WITH_PLUS)
            .query(STRING_WITH_SLASH, STRING_WITH_AMPERSAND)
            .build();
        String expected = SCHEME + "://" + HOST + ":" + PORT
            + "/" + STRING_WITH_HYPHEN
            + "/" + STRING_WITH_PLUS
            + "/" + STRING_WITH_SLASH_ENCODED
            + "?" + STRING_WITH_HYPHEN + "=" + STRING_WITH_PLUS_ENCODED_FOR_QUERY
            + "&" + STRING_WITH_SLASH_ENCODED + "=" + STRING_WITH_AMPERSAND_ENCODED_FOR_QUERY;
        assertEquals(expected, uri.toString());
    }

    @Test
    public void schemeAuthorityPathAndCompoundParamQueryViaParamsTest() {
        Params params = new Params()
            .addParam(STRING_WITH_HYPHEN, STRING_WITH_PLUS)
            .addParam(STRING_WITH_SLASH, STRING_WITH_AMPERSAND);
        URI uri = new URIBuilder()
            .scheme(SCHEME)
            .host(HOST)
            .port(PORT)
            .pathSegment(STRING_WITH_HYPHEN)
            .pathSegment(STRING_WITH_PLUS)
            .pathSegment(STRING_WITH_SLASH)
            .query(params.getParamMap())
            .build();
        String expected = SCHEME + "://" + HOST + ":" + PORT
            + "/" + STRING_WITH_HYPHEN
            + "/" + STRING_WITH_PLUS
            + "/" + STRING_WITH_SLASH_ENCODED
            + "?" + STRING_WITH_HYPHEN + "=" + STRING_WITH_PLUS_ENCODED_FOR_QUERY
            + "&" + STRING_WITH_SLASH_ENCODED + "=" + STRING_WITH_AMPERSAND_ENCODED_FOR_QUERY;
        assertEquals(expected, uri.toString());
    }
}
