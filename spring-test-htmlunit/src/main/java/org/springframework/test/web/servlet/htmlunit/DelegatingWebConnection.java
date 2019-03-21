/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.springframework.test.web.servlet.htmlunit;

import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import org.springframework.test.web.servlet.htmlunit.matchers.WebRequestMatcher;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Implementation of WebConnection that allows delegating to various WebConnection implementations. For example, if
 * you host your JavaScript on the domain code.jquery.com, you might want to use the following:</p>
 * <pre>
 * WebClient webClient = new WebClient();
 *
 * MockMvc mockMvc = ...
 * MockMvcWebConnection mockConnection = new MockMvcWebConnection(mockMvc);
 *
 * WebRequestMatcher cdnMatcher = new UrlRegexRequestMatcher(".*?//code.jquery.com/.*");
 * WebConnection httpConnection = new HttpWebConnection(webClient);
 * WebConnection webConnection = new DelegatingWebConnection(mockConnection, new DelegateWebConnection(cdnMatcher, httpConnection));
 *
 * webClient.setWebConnection(webConnection);
 *
 * WebClient webClient = new WebClient();
 * webClient.setWebConnection(webConnection);
 * </pre>
 * @author Rob Winch
 */
public final class DelegatingWebConnection implements WebConnection {
	private final List<DelegateWebConnection> connections;
	private final WebConnection defaultConnection;

	public DelegatingWebConnection(WebConnection defaultConnection, List<DelegateWebConnection> connections) {
		Assert.notNull(defaultConnection, "defaultConnection cannot be null");
		Assert.notEmpty(connections, "connections cannot be empty");
		this.connections = connections;
		this.defaultConnection = defaultConnection;
	}

	public DelegatingWebConnection(WebConnection defaultConnection,DelegateWebConnection... connections) {
		this(defaultConnection, Arrays.asList(connections));
	}

	@Override
	public WebResponse getResponse(WebRequest request) throws IOException {
		for(DelegateWebConnection connection : connections) {
			if(connection.getMatcher().matches(request)) {
				return connection.getDelegate().getResponse(request);
			}
		}
		return defaultConnection.getResponse(request);
	}

	public final static class DelegateWebConnection {
		private final WebRequestMatcher matcher;
		private final WebConnection delegate;

		public DelegateWebConnection(WebRequestMatcher matcher, WebConnection delegate) {
			this.matcher = matcher;
			this.delegate = delegate;
		}

		private WebRequestMatcher getMatcher() {
			return matcher;
		}

		private WebConnection getDelegate() {
			return delegate;
		}
	}
}