package kr.kro.dokbaro.server.domain.token

import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import kr.kro.dokbaro.server.configuration.security.TokenBasedAuthorizationFilter
import kr.kro.dokbaro.server.domain.token.jwt.JwtTokenExtractor
import kr.kro.dokbaro.server.domain.token.jwt.JwtTokenGenerator
import kr.kro.dokbaro.server.domain.token.refresh.RefreshTokenGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.Key

@Configuration
class TokenConfig {
	@Bean
	fun authTokenGenerator(key: Key) =
		AuthTokenGenerator(
			JwtTokenGenerator(key),
			RefreshTokenGenerator(),
		)

	@Bean
	fun tokenBasedAuthorizationFilter(
		key: Key,
		@Value("\${login.cookie.access-token-key}") accessTokenKey: String,
	) = TokenBasedAuthorizationFilter(
		JwtTokenExtractor(key),
		accessTokenKey,
	)

	@Bean
	fun key(
		@Value("\${jwt.key}") jwtKey: String,
	): Key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtKey))
}