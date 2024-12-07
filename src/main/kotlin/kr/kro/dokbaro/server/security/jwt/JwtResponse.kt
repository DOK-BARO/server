package kr.kro.dokbaro.server.security.jwt

import jakarta.servlet.http.HttpServletResponse
import kr.kro.dokbaro.server.common.util.UUIDUtils
import org.springframework.http.HttpHeaders
import java.util.UUID

data class JwtResponse(
	val accessToken: String,
	val refreshToken: UUID,
)

fun HttpServletResponse.setUpJwtCookie(jwt: JwtResponse) {
	this.addHeader(HttpHeaders.SET_COOKIE, jwt.accessToken)
	this.addHeader(HttpHeaders.SET_COOKIE, UUIDUtils.uuidToString(jwt.refreshToken))
}