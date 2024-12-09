package kr.kro.dokbaro.server.security.jwt

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders

data class JwtResponse(
	val accessToken: String,
	val refreshToken: String,
)

fun HttpServletResponse.setUpJwtCookie(jwt: JwtResponse) {
	this.addHeader(HttpHeaders.SET_COOKIE, jwt.accessToken)
	this.addHeader(HttpHeaders.SET_COOKIE, jwt.refreshToken)
}