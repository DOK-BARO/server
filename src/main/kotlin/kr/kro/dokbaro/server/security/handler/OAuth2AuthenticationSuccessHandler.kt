package kr.kro.dokbaro.server.security.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.kro.dokbaro.server.core.account.application.port.input.RegisterSocialAccountUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterSocialAccountCommand
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificationIdByEmailUserCase
import kr.kro.dokbaro.server.security.SecurityConstants
import kr.kro.dokbaro.server.security.jwt.JwtResponse
import kr.kro.dokbaro.server.security.jwt.JwtTokenGenerator
import kr.kro.dokbaro.server.security.jwt.setUpJwtCookie
import kr.kro.dokbaro.server.security.oauth2.SocialUser
import kr.kro.dokbaro.server.security.oauth2.SocialUserMapperManager
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class OAuth2AuthenticationSuccessHandler(
	private val jwtTokenGenerator: JwtTokenGenerator,
	private val registerSocialAccountUseCase: RegisterSocialAccountUseCase,
	private val socialUserMapperManager: SocialUserMapperManager,
	private val findCertificationIdByEmailUserCase: FindCertificationIdByEmailUserCase,
) : AuthenticationSuccessHandler {
	override fun onAuthenticationSuccess(
		request: HttpServletRequest,
		response: HttpServletResponse,
		authentication: Authentication,
	) {
		val socialUser: SocialUser = socialUserMapperManager.toSocialUser(authentication as OAuth2AuthenticationToken)

		val certificationId: UUID =
			findCertificationIdByEmailUserCase
				.findCertificationIdByEmail(
					email = socialUser.email,
				) ?: registerSocialAccountUseCase.registerSocialAccount(
				RegisterSocialAccountCommand(
					socialId = socialUser.id,
					email = socialUser.email,
					nickname = socialUser.nickname,
					provider = socialUser.provider,
					profileImage = socialUser.profileImage,
				),
			)

		val jwtToken: JwtResponse = jwtTokenGenerator.generate(certificationId)

		response.setUpJwtCookie(jwtToken)
		response.sendRedirect(request.session.getAttribute(SecurityConstants.CLIENT_REDIRECT_URL).toString())
		request.session.removeAttribute(SecurityConstants.CLIENT_REDIRECT_URL)
	}
}