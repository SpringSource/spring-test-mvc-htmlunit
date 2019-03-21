/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package sample.geb

import geb.Page
import geb.spock.GebReportingSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.geb.pages.*
import sample.geb.pages.CreateMessagePage
import sample.geb.pages.LoginPage
import sample.geb.pages.ViewMessagePage
import spock.lang.Stepwise

/**
 *
 * @author Rob Winch
 *
 */
@Stepwise
class CreateMessagesISpec extends GebReportingSpec {

	def 'login success'() {
		setup:
			to LoginPage
		when:
			login()
			to CreateMessagePage
		then:
			at CreateMessagePage
	}

	def 'missing field with javascript validation displays error'() {
		setup:
			to CreateMessagePage
			at CreateMessagePage
		when:
			submit.click(CreateMessagePage)
		then:
			errors.contains('This field is required.')
	}

	def 'missing field server side validation displays error'() {
		setup:
			to CreateMessagePage
			at CreateMessagePage
		when:
			form.summary = 'Summary'
			submit.click(CreateMessagePage)
		then:
			errors.contains('Message is required.')
	}

	def 'successfully create new message'() {
		setup:
			def expectedSummary = 'Spring Rocks'
			def expectedMessage = 'In case you didn\'t know, Spring Rocks!'
			to CreateMessagePage
			at CreateMessagePage
		when:
			form.summary = expectedSummary
			form.text = expectedMessage
			submit.click(ViewMessagePage)
		then:
			at ViewMessagePage
			success == 'Successfully created a new message'
			id
			date
			summary == expectedSummary
			message == expectedMessage
	}
}