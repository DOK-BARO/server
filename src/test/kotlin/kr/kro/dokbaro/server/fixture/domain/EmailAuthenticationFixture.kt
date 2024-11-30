package kr.kro.dokbaro.server.fixture.domain

import kr.kro.dokbaro.server.core.emailauthentication.domain.EmailAuthentication

fun emailAuthenticationFixture(
	id: Long = 0,
	address: String = "asdf@gmail.com",
	code: String = "asdf",
	authenticated: Boolean = false,
	used: Boolean = false,
): EmailAuthentication =
	EmailAuthentication(
		id,
		address,
		code,
		authenticated,
		used,
	)