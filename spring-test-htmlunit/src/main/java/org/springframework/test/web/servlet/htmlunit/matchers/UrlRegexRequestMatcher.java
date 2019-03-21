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
package org.springframework.test.web.servlet.htmlunit.matchers;

import com.gargoylesoftware.htmlunit.WebRequest;

import java.util.regex.Pattern;

/**
 * <p>
 * An implementation of WebRequestMatcher that allows matching on WebRequest#getUrl().toExternalForm() using a regular expression. For example, if you would like to match on the domain code.jquery.com, you might want to use the following:</p>
 *
 * <pre>
 * WebRequestMatcher cdnMatcher = new UrlRegexRequestMatcher(".*?//code.jquery.com/.*");
 * </pre>
 *
 * @author Rob Winch
 * @see org.springframework.test.web.servlet.htmlunit.DelegatingWebConnection
 */
public final class UrlRegexRequestMatcher implements WebRequestMatcher {
	private Pattern pattern;

	public UrlRegexRequestMatcher(String regex) {
		pattern = Pattern.compile(regex);
	}

	public UrlRegexRequestMatcher(Pattern pattern) {
		this.pattern = pattern;
	}

	@Override
	public boolean matches(WebRequest request) {
		String url = request.getUrl().toExternalForm();
		return pattern.matcher(url).matches();
	}
}
