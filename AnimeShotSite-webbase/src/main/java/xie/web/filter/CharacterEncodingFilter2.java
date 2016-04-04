/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xie.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

public class CharacterEncodingFilter2 extends OncePerRequestFilter {

	private String encoding;

	private boolean forceEncoding = false;

	public CharacterEncodingFilter2() {
		System.out.println("CharacterEncodingFilter2()");
	}

	public CharacterEncodingFilter2(String encoding) {
		this(encoding, false);
		System.out.println("CharacterEncodingFilter2(String encoding)");
	}

	public CharacterEncodingFilter2(String encoding, boolean forceEncoding) {
		this.encoding = encoding;
		this.forceEncoding = forceEncoding;
		System.out.println("CharacterEncodingFilter2(String encoding, boolean forceEncoding)");
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
		System.out.println("setEncoding(String encoding)");
	}

	public void setForceEncoding(boolean forceEncoding) {
		this.forceEncoding = forceEncoding;
		System.out.println("setForceEncoding(boolean forceEncoding)");
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
					throws ServletException, IOException {

		filterChain.doFilter(request, response);
	}

}
