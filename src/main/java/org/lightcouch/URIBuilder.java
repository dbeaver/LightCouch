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

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Helper class for construction of HTTP request URIs.
 * @since 0.0.2
 * @author Ahmed Yehia
 *
 */
class URIBuilder {
	@Nullable
	private String scheme;
	@Nullable
	private String host;
	@Nullable
	private Integer port;
	@Nullable
	private Collection<String> pathSegments;
	@Nullable
	private Map<String, Object> queryParams;

	public URIBuilder scheme(String scheme) {
		this.scheme = scheme;
		return this;
	}

	public URIBuilder host(String host) {
		this.host = host;
		return this;
	}

	public URIBuilder port(int port) {
		this.port = port;
		return this;
	}

	URIBuilder pathSegment(String pathSegment) {
		if (pathSegments == null) {
			pathSegments = new ArrayList<>();
		}
		pathSegments.add(pathSegment);
		return this;
	}

	public URIBuilder query(String name, Object value) {
		safeQueryParams().put(name, value);
		return this;
	}

	URIBuilder query(Map<String, ?> params) {
		safeQueryParams().putAll(params);
		return this;
	}

	@NotNull
	private Map<String, Object> safeQueryParams() {
		if (queryParams == null) {
			queryParams = new LinkedHashMap<>();
		}
		return queryParams;
	}

	public URI build() {
		assert scheme != null; // mandated by RFC 3986

		StringBuilder builder = new StringBuilder(scheme);
		builder.append(':');
		if (host != null || port != null) {
			builder.append("//");
		}
		if (host != null) {
			builder.append(host);
		}
		if (port != null) {
			builder.append(':').append(port);
		}
		if (pathSegments != null) {
			String path = URLEncodedUtils.formatSegments(pathSegments, StandardCharsets.UTF_8);
			builder.append(path);
		}
		if (queryParams != null) {
			builder.append('?');
			Iterable<NameValuePair> nameValuePairs = queryParams.entrySet().stream()
				.map(entry -> new QueryParameter(entry.getKey(), String.valueOf(entry.getValue())))
				.collect(Collectors.toList());
			String query = URLEncodedUtils.format(nameValuePairs, StandardCharsets.UTF_8);
			builder.append(query);
		}

		try {
			return new URI(builder.toString());
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}
	}

	private static final class QueryParameter implements NameValuePair {
		private final String name;
		private final String value;

		private QueryParameter(String name, String value) {
			this.name = name;
			this.value = value;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getValue() {
			return value;
		}
	}
}
