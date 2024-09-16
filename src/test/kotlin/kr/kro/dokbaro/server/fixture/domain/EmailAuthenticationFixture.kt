package kr.kro.dokbaro.server.fixture.domain

import kr.kro.dokbaro.server.core.emailauthentication.domain.EmailAuthentication

fun emailAuthenticationFixture(
	address: String = "asdf@gmail.com",
	code: String = "asdf",
	authenticated: Boolean = false,
	used: Boolean = false,
	id: Long = 0,
): EmailAuthentication =
	EmailAuthentication(
		address,
		code,
		authenticated,
		used,
		id,
	)